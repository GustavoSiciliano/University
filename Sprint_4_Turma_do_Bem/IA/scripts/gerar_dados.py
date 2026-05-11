"""
Gustavo Rodrigues Siciliano: RM568419
Gustavo de Jesus Silva: RM567926
Samuel Keniti Kina de Lima: 567614

Lê os dados do banco Oracle e gera os CSVs de treino:
  - data/falta.csv: modelo de previsão de falta (classificação)
  - data/arrecadacao.csv: modelo de previsão de arrecadação (regressão)

"""

import os
import pandas as pd
import oracledb

# Configuração Oracle

DB_CONFIG = {
    'user': 'rm568419',
    'password': '250204',
    'host': 'oracle.fiap.com.br',
    'port': 1521,
    'sid': 'orcl',
}

DATA_DIR = os.path.join(os.path.dirname(__file__), '..', 'data')
os.makedirs(DATA_DIR, exist_ok=True)

# Leitura do banco

def carregar_do_banco():
    """
    Conecta ao Oracle e retorna (df_falta, df_arrec).
    """
    conn = oracledb.connect(**DB_CONFIG)

    # Dataset de falta

    sql_falta = """
        SELECT
            pa.distancia_km,
            pa.renda_familiar,
            pa.turno_preferencial,
            pa.programa,
            (SELECT COUNT(*) FROM tdb_Consulta c2
             WHERE c2.id_paciente = pa.id_paciente
               AND c2.status = 'FALTA')     AS faltas_anteriores,
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

    sql_arrec = """
        SELECT
            c.meta_valor,
            (c.data_fim - c.data_inicio) AS duracao_dias,
            EXTRACT(MONTH FROM c.data_inicio) AS mes_inicio,
            EXTRACT(YEAR  FROM c.data_inicio) AS ano_inicio,
            (SELECT COUNT(*) FROM tdb_Campanha c2
             WHERE c2.data_inicio < c.data_inicio) AS campanhas_anteriores,
            (SELECT COUNT(*) FROM tdb_Doacao d
             WHERE d.id_campanha = c.id_campanha)  AS qtd_doacoes,
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

# Main

def main():
    print("[INFO] Conectando ao banco Oracle...")
    df_falta, df_arrec = carregar_do_banco()

    path_falta = os.path.join(DATA_DIR, 'falta.csv')
    path_arrec = os.path.join(DATA_DIR, 'arrecadacao.csv')

    df_falta.to_csv(path_falta, index=False)
    df_arrec.to_csv(path_arrec, index=False)

    print(f"[OK] falta.csv: {len(df_falta)} registros | shape {df_falta.shape}")
    print(f"[OK] arrecadacao.csv: {len(df_arrec)} registros | shape {df_arrec.shape}")
    print(f"\nDistribuição de faltou:\n{df_falta['faltou'].value_counts()}")
    print(f"\nArrecadação (estatísticas):\n{df_arrec['total_arrecadado'].describe()}")

if __name__ == '__main__':
    main()