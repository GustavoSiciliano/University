"""
Lê os dados do banco Oracle para os dois modelos preditivos da Turma do Bem:
  1. Previsão de FALTA em consulta (classificação binária)
  2. Previsão de ARRECADAÇÃO de campanha (regressão)

"""

#Tudo que estiver comentado é para treinar o Modelo com maior porcentual de acerto, ou seja, se o banco estiver off!
#Se possível, só use oq estiver comentado se for realmente necessário, mude a função para (USE_DB=False)

import os
import random
import numpy as np
import pandas as pd

USE_DB = True
N_FALTA = 2_000
N_ARREC = 1_500

random.seed(42)
np.random.seed(42)

DATA_DIR = os.path.join(os.path.dirname(__file__), '..', 'data')
os.makedirs(DATA_DIR, exist_ok=True)

# Leitura do banco de dados (USE_DB = True)
def carregar_do_banco():
    import oracledb
    conn = oracledb.connect(
        user="rm568419",
        password="250204",
        host="oracle.fiap.com.br",
        port=1521,
        sid="orcl"
    )

    # Dataset de Falta
    sql_falta = """
        SELECT
            pa.distancia_km,
            pa.renda_familiar,
            pa.turno_preferencial,
            pa.programa,
            (SELECT COUNT(*) FROM tdb_Consulta c2
             WHERE c2.id_paciente = pa.id_paciente
               AND c2.status = 'FALTA') AS faltas_anteriores,
            (SELECT COUNT(*) FROM tdb_Consulta c3
             WHERE c3.id_paciente = pa.id_paciente) AS total_consultas,
            c.turno AS turno_consulta,
            c.distancia_km AS dist_consulta,
            (c.data_consulta - SYSDATE) AS dias_ate_consulta,
            CASE WHEN c.status = 'FALTA' THEN 1 ELSE 0 END AS faltou
        FROM tdb_Consulta c
        JOIN tdb_Paciente pa ON pa.id_paciente = c.id_paciente
        WHERE c.status IN ('FALTA','REALIZADA','AGENDADA','CANCELADA')
    """

    # Dataset de Arrecadação
    sql_arrec = """
        SELECT
            c.meta_valor,
            (c.data_fim - c.data_inicio) AS duracao_dias,
            EXTRACT(MONTH FROM c.data_inicio) AS mes_inicio,
            EXTRACT(YEAR  FROM c.data_inicio) AS ano_inicio,
            (SELECT COUNT(*) FROM tdb_Campanha c2
             WHERE c2.data_inicio < c.data_inicio) AS campanhas_anteriores,
            (SELECT COUNT(*) FROM tdb_Doacao d
             WHERE d.id_campanha = c.id_campanha) AS qtd_doacoes,
            (SELECT COUNT(DISTINCT pc.id_voluntario)
             FROM tdb_ParticipaCampanha pc
             WHERE pc.id_campanha = c.id_campanha) AS qtd_voluntarios,
            c.total_arrecadado
        FROM tdb_Campanha c
        WHERE c.total_arrecadado IS NOT NULL
    """

    df_falta = pd.read_sql(sql_falta, conn)
    df_arrec = pd.read_sql(sql_arrec, conn)
    conn.close()

    df_falta.columns = [c.lower() for c in df_falta.columns]
    df_arrec.columns = [c.lower() for c in df_arrec.columns]
    return df_falta, df_arrec

# Leitura sintética — use apenas se (USE_DB = False)

def _turno_code(t):
    return {'MANHA': 0, 'TARDE': 1, 'NOITE': 2}.get(t, 1)

def _programa_code(p):
    return 0 if p == 'DENTISTAS_DO_BEM' else 1

def gerar_falta(n: int) -> pd.DataFrame:
    programas = ['DENTISTAS_DO_BEM', 'APOLONICAS_DO_BEM']
    turnos    = ['MANHA', 'TARDE', 'NOITE']
    rows = []
    for _ in range(n):
        programa          = random.choice(programas)
        turno_pref        = random.choice(turnos)
        turno_consulta    = random.choice(turnos)
        distancia_km      = round(random.uniform(0.5, 50.0), 1)
        renda_familiar    = round(random.uniform(400, 5000), 2)
        faltas_anteriores = random.randint(0, 5)
        total_consultas   = faltas_anteriores + random.randint(1, 10)
        dias_ate_consulta = random.randint(0, 30)
        if distancia_km > 20 or renda_familiar < 1000:
            prob = 0.70
        elif distancia_km > 10 or renda_familiar < 1500:
            prob = 0.35
        else:
            prob = 0.10
        if turno_consulta == 'NOITE':
            prob = min(prob + 0.10, 0.95)
        if faltas_anteriores > 0:
            prob = min(prob + 0.15, 0.95)
        faltou = int(random.random() < prob)
        rows.append({
            'distancia_km':       distancia_km,
            'renda_familiar':     renda_familiar,
            'turno_preferencial': _turno_code(turno_pref),
            'programa':           _programa_code(programa),
            'faltas_anteriores':  faltas_anteriores,
            'total_consultas':    total_consultas,
            'turno_consulta':     _turno_code(turno_consulta),
            'dist_consulta':      distancia_km + round(random.uniform(-2, 2), 1),
            'dias_ate_consulta':  dias_ate_consulta,
            'faltou':             faltou,
        })
    return pd.DataFrame(rows)

def gerar_arrecadacao(n: int) -> pd.DataFrame:
    sazonalidade = {
        1: 0.85, 2: 0.80, 3: 0.88, 4: 0.90, 5: 1.20,
        6: 1.00, 7: 0.95, 8: 0.92, 9: 0.98, 10: 1.15,
        11: 1.05, 12: 1.30
    }
    rows = []
    for _ in range(n):
        meta_valor           = round(random.uniform(5_000, 50_000), 2)
        duracao_dias         = random.randint(7, 60)
        mes_inicio           = random.randint(1, 12)
        ano_inicio           = random.randint(2022, 2025)
        campanhas_anteriores = random.randint(0, 20)
        qtd_voluntarios      = random.randint(1, 15)
        fator_saz  = sazonalidade[mes_inicio]
        fator_exp  = 1 + (campanhas_anteriores * 0.02)
        fator_dur  = 1 + (duracao_dias / 200)
        fator_vol  = 1 + (qtd_voluntarios * 0.015)
        base        = meta_valor * fator_saz * fator_exp * fator_dur * fator_vol
        ruido       = np.random.normal(1.0, 0.15)
        total_arrec = round(max(base * ruido, 0), 2)
        qtd_doacoes = max(1, int(total_arrec / random.uniform(100, 600)))
        rows.append({
            'meta_valor':           meta_valor,
            'duracao_dias':         duracao_dias,
            'mes_inicio':           mes_inicio,
            'ano_inicio':           ano_inicio,
            'campanhas_anteriores': campanhas_anteriores,
            'qtd_doacoes':          qtd_doacoes,
            'qtd_voluntarios':      qtd_voluntarios,
            'total_arrecadado':     total_arrec,
        })
    return pd.DataFrame(rows)

# Main

def main():
    if USE_DB:
        print("[INFO] Lendo dados do banco Oracle...")
        try:
            df_falta, df_arrec = carregar_do_banco()
        except Exception as e:
            # CORRIGIDO: fallback automático para sintético se banco falhar
            print(f"[AVISO] Banco Oracle indisponível ({e}). Usando dados sintéticos.")
            df_falta = gerar_falta(N_FALTA)
            df_arrec  = gerar_arrecadacao(N_ARREC)
    else:
        print("[INFO] Gerando dados sintéticos...")
        df_falta = gerar_falta(N_FALTA)
        df_arrec  = gerar_arrecadacao(N_ARREC)

    path_falta = os.path.join(DATA_DIR, 'falta.csv')
    path_arrec  = os.path.join(DATA_DIR, 'arrecadacao.csv')

    df_falta.to_csv(path_falta, index=False)
    df_arrec.to_csv(path_arrec, index=False)

    print(f"[OK] falta.csv: {len(df_falta)} registros | shape: {df_falta.shape}")
    print(f"[OK] arrecadacao.csv: {len(df_arrec)} registros | shape: {df_arrec.shape}")
    print(f"\nDistribuição de faltou:\n{df_falta['faltou'].value_counts()}")
    print(f"\nArrecadação (estatísticas):\n{df_arrec['total_arrecadado'].describe()}")


if __name__ == '__main__':
    main()
