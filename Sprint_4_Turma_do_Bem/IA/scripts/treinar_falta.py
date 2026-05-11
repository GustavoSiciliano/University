"""
Gustavo Rodrigues Siciliano: RM568419
Gustavo de Jesus Silva: RM567926
Samuel Keniti Kina de Lima: 567614

Previsão de FALTA em consulta — classificação binária.

Modelos avaliados e comparados automaticamente:
  1. Regressão Logística
  2. Random Forest
  3. XGBoost

O melhor por F1 é salvo em models/falta.joblib.
Métricas salvas em models/metricas_falta.json.
"""

import os, json, warnings
warnings.filterwarnings('ignore')

import numpy as np
import pandas as pd
import joblib

from sklearn.model_selection import train_test_split, StratifiedKFold, cross_val_score
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import (
    accuracy_score, precision_score, recall_score,
    f1_score, roc_auc_score, classification_report,
)
from xgboost import XGBClassifier
from imblearn.over_sampling import SMOTE

# Caminhos 

BASE = os.path.dirname(os.path.dirname(__file__))
DATA = os.path.join(BASE, 'data', 'falta.csv')
MODELS = os.path.join(BASE, 'models')
os.makedirs(MODELS, exist_ok=True)

# Features e target

# Features base vindas do CSV
FEATURES_BASE = [
    'distancia_km',
    'renda_familiar',
    'turno_preferencial',
    'programa',
    'faltas_anteriores',
    'total_consultas',
    'turno_consulta',
    'dist_consulta',
    'dias_ate_consulta',
]
TARGET = 'faltou'

# Pré-processamento

def carregar_e_preprocessar(path: str):
    """
    Lê o CSV, codifica colunas categóricas e cria features derivadas.

    """
    df = pd.read_csv(path)
    print(f"[INFO] Dataset: {df.shape} | faltou={df[TARGET].sum()} ({df[TARGET].mean()*100:.1f}%)")

    # Encoding robusto
    turno_map = {'MANHA': 0, 'TARDE': 1, 'NOITE': 2}
    programa_map = {'DENTISTAS_DO_BEM': 0, 'APOLONICAS_DO_BEM': 1}

    for col, mapa in [('turno_preferencial', turno_map),
                      ('turno_consulta', turno_map),
                      ('programa', programa_map)]:
        if df[col].dtype == object:
            df[col] = df[col].str.strip().str.upper().map(mapa).fillna(1 if col != 'programa' else 0).astype(int)
        else:
            df[col] = df[col].fillna(1 if col != 'programa' else 0).astype(int)

    # Features derivadas
    df['taxa_falta_historica'] = (
        df['faltas_anteriores'] / df['total_consultas'].replace(0, 1)
    )
    df['score_risco'] = (
        (df['distancia_km'] / 50) * 0.4 +
        (1 - df['renda_familiar'] / df['renda_familiar'].max()) * 0.4 +
        df['taxa_falta_historica'] * 0.2
    )

    features = FEATURES_BASE + ['taxa_falta_historica', 'score_risco']
    return df[features], df[TARGET], features

# Avaliação

def avaliar(nome, pipeline, X_test, y_test, resultados):
    """Imprime e armazena métricas de um modelo no test set."""
    y_pred  = pipeline.predict(X_test)
    y_proba = pipeline.predict_proba(X_test)[:, 1]

    roc_auc = round(roc_auc_score(y_test, y_proba), 4) if len(y_test.unique()) > 1 else float('nan')

    metricas = {
        'accuracy': round(accuracy_score(y_test, y_pred), 4),
        'precision': round(precision_score(y_test, y_pred, zero_division=0), 4),
        'recall': round(recall_score(y_test, y_pred, zero_division=0), 4),
        'f1': round(f1_score(y_test, y_pred, zero_division=0), 4),
        'roc_auc': roc_auc,
    }

    print(f"\n{'─'*50}\n {nome}\n{'─'*50}")
    for k, v in metricas.items():
        print(f"  {k:10s}: {v}")
    print(f"\n{classification_report(y_test, y_pred, labels=[0,1], target_names=['Compareceu','Faltou'], zero_division=0)}")

    resultados[nome] = metricas
    return metricas['f1']

# Treino principal

def treinar():
    print("=" * 60)
    print(" MODELO DE FALTA — TREINO E VALIDAÇÃO")
    print("=" * 60)

    X, y, features = carregar_e_preprocessar(DATA)

    # Split estratificado
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )

    # SMOTE
    min_class = y_train.value_counts().min()
    if min_class > 1:
        smote = SMOTE(random_state=42, k_neighbors=min(min_class - 1, 5))
        X_train, y_train = smote.fit_resample(X_train, y_train)
    print(f"\n[SMOTE] Treino balanceado: {y_train.value_counts().to_dict()}")

    # Definição dos modelos
    modelos = {
        'Regressão Logística': Pipeline([
            ('scaler', StandardScaler()),
            ('clf', LogisticRegression(max_iter=1000, random_state=42, class_weight='balanced')),
        ]),
        'Random Forest': Pipeline([
            ('scaler', StandardScaler()),
            ('clf', RandomForestClassifier(
                n_estimators=200, max_depth=8, min_samples_leaf=5,
                random_state=42, n_jobs=-1,
            )),
        ]),

        'XGBoost': Pipeline([
            ('scaler', StandardScaler()),
            ('clf', XGBClassifier(
                n_estimators=200, max_depth=5, learning_rate=0.05,
                subsample=0.8, colsample_bytree=0.8,
                eval_metric='logloss', random_state=42, n_jobs=-1,
            )),
        ]),
    }

    # Cross-validation
    n_splits = max(2, min(5, y_train.value_counts().min()))
    print(f"\n[CV] Validação cruzada ({n_splits}-fold, métrica: F1)")
    cv = StratifiedKFold(n_splits=n_splits, shuffle=True, random_state=42)
    for nome, pipe in modelos.items():
        scores = cross_val_score(pipe, X_train, y_train, cv=cv, scoring='f1', n_jobs=-1)
        print(f"  {nome:25s}: {scores.mean():.4f} ± {scores.std():.4f}")

    # Treino final
    print("\n[TEST SET] Métricas finais")
    resultados, scores_f1 = {}, {}
    for nome, pipe in modelos.items():
        pipe.fit(X_train, y_train)
        scores_f1[nome] = avaliar(nome, pipe, X_test, y_test, resultados)

    # Seleciona o melhor por F1
    melhor_nome = max(scores_f1, key=scores_f1.get)
    melhor_pipe = modelos[melhor_nome]
    print(f"\n{'='*60}\n  MELHOR MODELO: {melhor_nome}  (F1 = {scores_f1[melhor_nome]:.4f})\n{'='*60}")

    # Serialização
    artefato = {
        'pipeline': melhor_pipe,
        'features': features,
        'modelo': melhor_nome,
        'versao': 'v1.0',
    }
    path_model = os.path.join(MODELS, 'falta.joblib')
    joblib.dump(artefato, path_model)
    print(f"\n[OK] Modelo salvo em: {path_model}")

    saida = {
        'melhor_modelo': melhor_nome,
        'f1_melhor': scores_f1[melhor_nome],
        'features': features,
        'versao': 'v1.0',
        'modelos': resultados,
    }
    path_met = os.path.join(MODELS, 'metricas_falta.json')
    with open(path_met, 'w', encoding='utf-8') as f:
        json.dump(saida, f, ensure_ascii=False, indent=2)
    print(f"[OK] Métricas salvas em: {path_met}")

    return melhor_pipe, features

if __name__ == '__main__':
    treinar()