"""

PREVISÃO DE ARRECADAÇÃO DE CAMPANHAS (regressão).

Modelos avaliados:
  1. Regressão Linear (Ridge)
  2. Random Forest Regressor
  3. XGBoost Regressor

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

# 1. Carregamento e pré-processamento

def carregar_e_preprocessar(path: str):
    df = pd.read_csv(path)
    print(f"[INFO] Dataset carregado: {df.shape}")
    print(f"[INFO] Arrecadação média: R$ {df[TARGET].mean():,.2f}")

    # Sazonalidade (não usa o target — sem leakage)
    sazon = {1:0.85,2:0.80,3:0.88,4:0.90,5:1.20,6:1.00,
              7:0.95,8:0.92,9:0.98,10:1.15,11:1.05,12:1.30}
    df['fator_sazonalidade'] = df['mes_inicio'].map(sazon)

    # Doações por voluntário (não usa o target — sem leakage)
    df['doacoes_por_voluntario'] = (
        df['qtd_doacoes'] / df['qtd_voluntarios'].replace(0, 1)
    )

    # REMOVIDO: pct_meta_media e valor_medio_doacao usavam total_arrecadado
    # para calcular features de X — data leakage que inflava as métricas
    # artificialmente e tornava o modelo inútil em produção.

    features_ext = FEATURES + [
        'fator_sazonalidade',
        'doacoes_por_voluntario',
    ]

    X = df[features_ext]
    y = df[TARGET]
    return X, y, features_ext

# Métricas

def avaliar(nome, pipeline, X_test, y_test, resultados):
    y_pred = pipeline.predict(X_test)

    mae  = mean_absolute_error(y_test, y_pred)
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    r2   = r2_score(y_test, y_pred)
    mape = np.mean(np.abs((y_test - y_pred) / y_test.replace(0, 1))) * 100

    metricas = {
        'MAE': round(mae, 2),
        'RMSE': round(rmse, 2),
        'R2': round(r2, 4),
        'MAPE': round(mape, 2),
    }

    print(f"\n{'─'*50}")
    print(f"  {nome}")
    print(f"{'─'*50}")
    for k, v in metricas.items():
        print(f"  {k:6s}: {v}")

    # Amostra
    sample = pd.DataFrame({'real': y_test.values[:5], 'pred': y_pred[:5]})
    print(f"\n  Amostra (R$):\n{sample.to_string(index=False)}")

    resultados[nome] = metricas
    return r2

# Treino

def treinar():
    print("=" * 60)
    print("MODELO DE ARRECADAÇÃO — TREINO E VALIDAÇÃO")
    print("=" * 60)

    X, y, features = carregar_e_preprocessar(DATA)

    # Remover outliers extremos
    z = np.abs((y - y.mean()) / y.std())
    mask = z < 3
    X, y = X[mask], y[mask]
    print(f"[INFO] Após remoção de outliers: {len(X)} registros")

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42
    )

    # Modelos
    modelos = {
        'Ridge (Regressão Linear)': Pipeline([
            ('scaler', StandardScaler()),
            ('reg', Ridge(alpha=10.0))
        ]),
        'Random Forest': Pipeline([
            ('scaler', StandardScaler()),
            ('reg', RandomForestRegressor(
                n_estimators=200, max_depth=10, min_samples_leaf=3,
                random_state=42, n_jobs=-1
            ))
        ]),
        'XGBoost': Pipeline([
            ('scaler', StandardScaler()),
            ('reg', XGBRegressor(
                n_estimators=200, max_depth=5, learning_rate=0.05,
                subsample=0.8, colsample_bytree=0.8,
                random_state=42, n_jobs=-1
            ))
        ]),
    }

    # Cross-validation
    print("\n[CV] Validação cruzada (5-fold, métrica: R²)")
    cv = KFold(n_splits=5, shuffle=True, random_state=42)
    for nome, pipe in modelos.items():
        scores = cross_val_score(pipe, X_train, y_train, cv=cv, scoring='r2', n_jobs=-1)
        print(f"  {nome:30s}: {scores.mean():.4f} ± {scores.std():.4f}")

    # Avaliação no test set
    print("\n[TEST SET] Métricas finais")
    resultados = {}
    scores_r2  = {}

    for nome, pipe in modelos.items():
        pipe.fit(X_train, y_train)
        scores_r2[nome] = avaliar(nome, pipe, X_test, y_test, resultados)

    # Melhor modelo
    melhor_nome = max(scores_r2, key=scores_r2.get)
    melhor_pipe = modelos[melhor_nome]
    print(f"\n{'='*60}")
    print(f"  MELHOR MODELO: {melhor_nome} (R² = {scores_r2[melhor_nome]:.4f})")
    print(f"{'='*60}")

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

    # CORRIGIDO: nome do arquivo alinhado com o que app.py lê
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
