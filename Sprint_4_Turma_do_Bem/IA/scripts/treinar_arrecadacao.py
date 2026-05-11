"""
Gustavo Rodrigues Siciliano: RM568419
Gustavo de Jesus Silva: RM567926
Samuel Keniti Kina de Lima: 567614

Previsão de ARRECADAÇÃO de campanhas — regressão.

Modelos avaliados e comparados automaticamente:
  1. Regressão Linear (Ridge)
  2. Random Forest Regressor
  3. XGBoost Regressor

O melhor por R² é salvo em models/arrecadacao.joblib.
Métricas salvas em models/metricas_arrecadacao.json.
"""

import os, json, warnings
warnings.filterwarnings('ignore')

import numpy as np
import pandas as pd
import joblib

from sklearn.model_selection import train_test_split, KFold, cross_val_score
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import Ridge
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
from xgboost import XGBRegressor

# Caminhos

BASE = os.path.dirname(os.path.dirname(__file__))
DATA = os.path.join(BASE, 'data', 'arrecadacao.csv')
MODELS = os.path.join(BASE, 'models')
os.makedirs(MODELS, exist_ok=True)

# Features e target

FEATURES = [
    'meta_valor',
    'duracao_dias',
    'mes_inicio',
    'ano_inicio',
    'campanhas_anteriores',
    'qtd_doacoes',
    'qtd_voluntarios',
]
TARGET = 'total_arrecadado'

# Pré-processamento

def carregar_e_preprocessar(path: str):
    """
    Lê o CSV e cria features derivadas sem usar o target
    """
    df = pd.read_csv(path)
    print(f"[INFO] Dataset: {df.shape} | arrecadação média: R$ {df[TARGET].mean():,.2f}")

    # Fator sazonal por mês
    sazon = {
        1:0.85, 2:0.80, 3:0.88, 4:0.90,  5:1.20, 6:1.00,
        7:0.95, 8:0.92, 9:0.98, 10:1.15, 11:1.05, 12:1.30
    }
    df['fator_sazonalidade'] = df['mes_inicio'].map(sazon)
    df['doacoes_por_voluntario'] = df['qtd_doacoes'] / df['qtd_voluntarios'].replace(0, 1)

    features = FEATURES + ['fator_sazonalidade', 'doacoes_por_voluntario']
    return df[features], df[TARGET], features

# Avaliação

def avaliar(nome, pipeline, X_test, y_test, resultados):
    """Imprime e armazena métricas de um modelo no test set."""
    y_pred = pipeline.predict(X_test)

    metricas = {
        'MAE': round(mean_absolute_error(y_test, y_pred), 2),
        'RMSE': round(np.sqrt(mean_squared_error(y_test, y_pred)), 2),
        'R2': round(r2_score(y_test, y_pred), 4),
        'MAPE': round(np.mean(np.abs((y_test - y_pred) / y_test.replace(0, 1))) * 100, 2),
    }

    print(f"\n{'─'*50}\n {nome}\n{'─'*50}")
    for k, v in metricas.items():
        print(f"{k:6s}: {v}")

    # Amostra: real vs previsto
    sample = pd.DataFrame({'real': y_test.values[:5], 'pred': y_pred[:5]})
    print(f"\n  Amostra (R$):\n{sample.to_string(index=False)}")

    resultados[nome] = metricas
    return metricas['R2']

# Treino principal

def treinar():
    print("=" * 60)
    print(" MODELO DE ARRECADAÇÃO — TREINO E VALIDAÇÃO")
    print("=" * 60)

    X, y, features = carregar_e_preprocessar(DATA)

    # Remove outliers extremos
    z    = np.abs((y - y.mean()) / y.std())
    mask = z < 3
    X, y = X[mask], y[mask]
    print(f"[INFO] Após remoção de outliers: {len(X)} registros")

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42
    )

    # Definição dos modelos
    modelos = {
        'Ridge (Regressão Linear)': Pipeline([
            ('scaler', StandardScaler()),
            ('reg', Ridge(alpha=10.0)),
        ]),
        'Random Forest': Pipeline([
            ('scaler', StandardScaler()),
            ('reg', RandomForestRegressor(
                n_estimators=200, max_depth=10, min_samples_leaf=3,
                random_state=42, n_jobs=-1,
            )),
        ]),

        # XGBoost Regressor
        'XGBoost': Pipeline([
            ('scaler', StandardScaler()),
            ('reg', XGBRegressor(
                n_estimators=200, max_depth=5, learning_rate=0.05,
                subsample=0.8, colsample_bytree=0.8,
                random_state=42, n_jobs=-1,
            )),
        ]),
    }

    # Cross-validation
    print("\n[CV] Validação cruzada (5-fold, métrica: R²)")
    cv = KFold(n_splits=5, shuffle=True, random_state=42)
    for nome, pipe in modelos.items():
        scores = cross_val_score(pipe, X_train, y_train, cv=cv, scoring='r2', n_jobs=-1)
        print(f"  {nome:30s}: {scores.mean():.4f} ± {scores.std():.4f}")

    # Treino final + avaliação no test set
    print("\n[TEST SET] Métricas finais")
    resultados, scores_r2 = {}, {}
    for nome, pipe in modelos.items():
        pipe.fit(X_train, y_train)
        scores_r2[nome] = avaliar(nome, pipe, X_test, y_test, resultados)

    # Seleciona o melhor por R²
    melhor_nome = max(scores_r2, key=scores_r2.get)
    melhor_pipe = modelos[melhor_nome]
    print(f"\n{'='*60}\n  MELHOR MODELO: {melhor_nome} (R² = {scores_r2[melhor_nome]:.4f})\n{'='*60}")

    # Serialização
    artefato = {
        'pipeline': melhor_pipe,
        'features': features,
        'modelo': melhor_nome,
        'versao': 'v1.0',
    }
    path_model = os.path.join(MODELS, 'arrecadacao.joblib')
    joblib.dump(artefato, path_model)
    print(f"\n[OK] Modelo salvo em: {path_model}")

    # Salva métricas para o endpoint /metricas/arrecadacao
    # r2_melhor é usado pelo app.py como proxy de confiança
    saida = {
        'melhor_modelo': melhor_nome,
        'r2_melhor': scores_r2[melhor_nome],
        'features': features,
        'versao': 'v1.0',
        'modelos': resultados,
    }
    path_met = os.path.join(MODELS, 'metricas_arrecadacao.json')
    with open(path_met, 'w', encoding='utf-8') as f:
        json.dump(saida, f, ensure_ascii=False, indent=2)
    print(f"[OK] Métricas salvas em: {path_met}")

    return melhor_pipe, features

if __name__ == '__main__':
    treinar()