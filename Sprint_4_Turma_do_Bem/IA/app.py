"""

API Flask
Porta: 8000

"""

import os, json, datetime, uuid, subprocess, sys
import numpy as np
import pandas as pd
import joblib
import oracledb
from flask import Flask, request, jsonify
from flask_cors import CORS

# Inicialização

app = Flask(__name__)
CORS(app)  # permite consumo pelo front React
BASE = os.path.dirname(__file__)
MODELS_DIR = os.path.join(BASE, 'models')
DATA_DIR = os.path.join(BASE, 'data')
SCRIPTS_DIR = os.path.join(BASE, 'scripts')

RESULTADOS_FILE = os.path.join(DATA_DIR, 'resultados.json')

# Configuração do banco Oracle

DB_CONFIG = {
    'user': 'rm568419',
    'password': '250204',
    'host': 'oracle.fiap.com.br',
    'port': 1521,
    'sid': 'orcl',
}

def _conectar_oracle():
    return oracledb.connect(**DB_CONFIG)

# Carregamento dos modelos

def _carregar(nome_arquivo):
    path = os.path.join(MODELS_DIR, nome_arquivo)
    if not os.path.exists(path):
        return None
    return joblib.load(path)


artefato_falta = _carregar('falta.joblib')
artefato_arrec = _carregar('arrecadacao.joblib')

# Utilitários

# Histórico em memória — sem escrita em disco (compatível com Render gratuito)
_historico = []

def _salvar_resultado(tipo: str, entrada: dict, saida: dict):
    _historico.append({
        'id':          str(uuid.uuid4()),
        'tipo':        tipo,
        'dt_predicao': datetime.datetime.now().isoformat(),
        'entrada':     entrada,
        'saida':       saida,
    })

def _erro(msg: str, code: int = 400):
    return jsonify({'sucesso': False, 'erro': msg}), code

# ENDPOINT: /health

@app.route('/health', methods=['GET'])
def health():
    return jsonify({
        'status': 'ok',
        'servico': 'Turma do Bem — API de IA',
        'versao': 'v1.0',
        'modelos': {
            'falta': 'carregado' if artefato_falta  else 'não encontrado',
            'arrecadacao': 'carregado' if artefato_arrec  else 'não encontrado',
        },
        'timestamp': datetime.datetime.now().isoformat(),
    })

# Input JSON (espelha tdb_RegistroIA.entrada_json):
# {
#   "distanciaKm": 15.0,
#   "rendaFamiliar": 900.0,
#   "turnoPref": "MANHA", (ou 0/1/2)
#   "programa": "DENTISTAS_DO_BEM", (ou 0/1)
#   "faltasAnteriores": 2,
#   "totalConsultas": 5,
#   "turnoConsulta": "TARDE",   (ou 0/1/2)
#   "distConsulta": 15.5,
#   "diasAteConsulta": 7
# }
#
# Output JSON:
# {
#   "probabilidadeFalta": 0.78,
#   "risco": "ALTO",
#   "classePrevista": "FALTA",
#   "recomendacao": ""
# }

TURNO_MAP = {'MANHA': 0, 'TARDE': 1, 'NOITE': 2}
PROGRAMA_MAP = {'DENTISTAS_DO_BEM': 0, 'APOLONICAS_DO_BEM': 1}

def _parse_falta(body: dict) -> pd.DataFrame:
    def turno(v):
        if isinstance(v, int):
            return v
        s = str(v).upper()
        return TURNO_MAP.get(s, 1)

    def prog(v):
        if isinstance(v, int):
            return v
        s = str(v).upper()
        return PROGRAMA_MAP.get(s, 0)

    distancia_km = float(body['distanciaKm'])
    renda_familiar = float(body['rendaFamiliar'])
    turno_pref = turno(body.get('turnoPref', 1))
    programa = prog(body.get('programa', 0))
    faltas_ant = int(body['faltasAnteriores'])
    total_consultas = int(body.get('totalConsultas', max(faltas_ant, 1)))
    turno_consulta = turno(body.get('turnoConsulta', 1))
    dist_consulta = float(body.get('distConsulta', distancia_km))
    dias_ate_consulta = int(body.get('diasAteConsulta', 3))

    taxa_falta_hist = faltas_ant / max(total_consultas, 1)
    score_risco = (
        (distancia_km / 50) * 0.4 +
        (1 - min(renda_familiar, 5000) / 5000) * 0.4 +
        taxa_falta_hist * 0.2
    )

    row = {
        'distancia_km': distancia_km,
        'renda_familiar': renda_familiar,
        'turno_preferencial': turno_pref,
        'programa': programa,
        'faltas_anteriores': faltas_ant,
        'total_consultas': total_consultas,
        'turno_consulta': turno_consulta,
        'dist_consulta': dist_consulta,
        'dias_ate_consulta': dias_ate_consulta,
        'taxa_falta_historica': taxa_falta_hist,
        'score_risco': score_risco,
    }
    return pd.DataFrame([row])

def _risco_falta(prob: float) -> str:
    if prob >= 0.65:
        return 'ALTO'
    if prob >= 0.35:
        return 'MEDIO'
    return 'BAIXO'

def _recomendacao_falta(risco: str, dias: int) -> str:
    if risco == 'ALTO':
        return 'Enviar lembrete por SMS/WhatsApp 48h antes e confirmar presença 24h antes.'
    if risco == 'MEDIO':
        return 'Enviar lembrete por e-mail 48h antes da consulta.'
    return 'Nenhuma ação especial necessária — paciente com baixo risco de falta.'

@app.route('/predict/falta', methods=['POST'])
def predict_falta():
    if not artefato_falta:
        return _erro('Modelo de falta não encontrado. Execute treinar_falta.py primeiro.', 503)

    body = request.get_json(silent=True)
    if not body:
        return _erro('JSON inválido ou ausente.')

    campos_obrigatorios = ['distanciaKm', 'rendaFamiliar', 'faltasAnteriores']
    for c in campos_obrigatorios:
        if c not in body:
            return _erro(f'Campo obrigatório ausente: {c}')

    try:
        X = _parse_falta(body)
        X = X[artefato_falta['features']]
        pipe = artefato_falta['pipeline']

        prob = float(pipe.predict_proba(X)[0][1])
        risco = _risco_falta(prob)
        classe = 'FALTA' if prob >= 0.5 else 'NAO_FALTA'

        saida = {
            'sucesso': True,
            'probabilidadeFalta': round(prob, 4),
            'risco': risco,
            'classePrevista': classe,
            'recomendacao': _recomendacao_falta(risco, body.get('diasAteConsulta', 3)),
            'modeloVersao': artefato_falta.get('versao', 'v1.0'),
        }

        _salvar_resultado('FALTA', body, saida)
        return jsonify(saida)

    except KeyError as e:
        return _erro(f'Feature ausente no modelo: {e}')
    except Exception as e:
        return _erro(f'Erro interno: {str(e)}', 500)

#
# Input JSON (espelha tdb_RegistroIA.entrada_json):
# {
#   "metaValor": 20000.0,
#   "duracaoDias": 30,
#   "mesInicio": 5,
#   "anoInicio": 2025,
#   "campanhasAnteriores": 4,
#   "qtdDoacoes": 50,
#   "qtdVoluntarios": 8
# }
#
# Output JSON:
# {
#   "valorPrevisto": 24500.00,
#   "confianca": 0.82,
#   "tendencia":  "ALTA",
#   "recomendacao": ""
# }

SAZON = {1:0.85,2:0.80,3:0.88,4:0.90,5:1.20,6:1.00,
         7:0.95,8:0.92,9:0.98,10:1.15,11:1.05,12:1.30}

def _parse_arrec(body: dict) -> pd.DataFrame:
    meta_valor = float(body['metaValor'])
    duracao_dias = int(body['duracaoDias'])
    mes_inicio = int(body['mesInicio'])
    ano_inicio = int(body.get('anoInicio', 2025))
    campanhas_anteriores = int(body['campanhasAnteriores'])
    qtd_voluntarios = int(body.get('qtdVoluntarios', 5))
    qtd_doacoes = int(body.get('qtdDoacoes', max(1, int(meta_valor / 300))))

    fator_sazon = SAZON.get(mes_inicio, 1.0)
    doacoes_por_vol = qtd_doacoes / max(qtd_voluntarios, 1)

    row = {
        'meta_valor': meta_valor,
        'duracao_dias': duracao_dias,
        'mes_inicio': mes_inicio,
        'ano_inicio': ano_inicio,
        'campanhas_anteriores': campanhas_anteriores,
        'qtd_doacoes': qtd_doacoes,
        'qtd_voluntarios': qtd_voluntarios,
        'fator_sazonalidade': fator_sazon,
        'doacoes_por_voluntario': doacoes_por_vol,
    }
    return pd.DataFrame([row])

def _tendencia(previsto: float, meta: float) -> str:
    pct = previsto / max(meta, 1)
    if pct >= 1.10:
        return 'ALTA'
    if pct >= 0.85:
        return 'ESTAVEL'
    return 'BAIXA'

def _recomendacao_arrec(tendencia: str, pct: float) -> str:
    if tendencia == 'ALTA':
        return f'Campanha deve superar a meta ({pct*100:.0f}% previsto). Mantenha a estratégia atual.'
    if tendencia == 'ESTAVEL':
        return f'Campanha próxima da meta ({pct*100:.0f}% previsto). Intensifique a comunicação na reta final.'
    return f'Risco de não atingir a meta ({pct*100:.0f}% previsto). Considere ampliar canais de divulgação.'

@app.route('/predict/arrecadacao', methods=['POST'])
def predict_arrecadacao():
    if not artefato_arrec:
        return _erro('Modelo de arrecadação não encontrado. Execute treinar_arrecadacao.py primeiro.', 503)

    body = request.get_json(silent=True)
    if not body:
        return _erro('JSON inválido ou ausente.')

    campos_obrigatorios = ['metaValor', 'duracaoDias', 'mesInicio', 'campanhasAnteriores']
    for c in campos_obrigatorios:
        if c not in body:
            return _erro(f'Campo obrigatório ausente: {c}')

    try:
        X = _parse_arrec(body)
        X = X[artefato_arrec['features']]
        pipe = artefato_arrec['pipeline']

        valor_previsto = float(pipe.predict(X)[0])
        valor_previsto = max(valor_previsto, 0.0)
        meta = float(body['metaValor'])
        pct = valor_previsto / max(meta, 1)
        tendencia = _tendencia(valor_previsto, meta)

        # CORRIGIDO: lê metricas_arrecadacao.json (nome alinhado com treinar_arrecadacao.py)
        metricas_path = os.path.join(MODELS_DIR, 'metricas_arrecadacao.json')
        confianca = 0.80
        if os.path.exists(metricas_path):
            with open(metricas_path, encoding='utf-8') as f:
                met = json.load(f)
            confianca = round(max(0.0, min(1.0, met.get('r2_melhor', 0.80))), 4)

        saida = {
            'sucesso': True,
            'valorPrevisto': round(valor_previsto, 2),
            'confianca': confianca,
            'tendencia': tendencia,
            'pctMeta': round(pct, 4),
            'recomendacao': _recomendacao_arrec(tendencia, pct),
            'modeloVersao': artefato_arrec.get('versao', 'v1.0'),
        }

        _salvar_resultado('ARRECADACAO', body, saida)
        return jsonify(saida)

    except KeyError as e:
        return _erro(f'Feature ausente no modelo: {e}')
    except Exception as e:
        return _erro(f'Erro interno: {str(e)}', 500)

# ENDPOINT: /metricas/falta  |  /metricas/arrecadacao

@app.route('/metricas/falta', methods=['GET'])
def metricas_falta():
    path = os.path.join(MODELS_DIR, 'metricas_falta.json')
    if not os.path.exists(path):
        return _erro('Métricas não encontradas. Treine o modelo primeiro.', 404)
    with open(path, encoding='utf-8') as f:
        return jsonify(json.load(f))


@app.route('/metricas/arrecadacao', methods=['GET'])
def metricas_arrecadacao():
    # CORRIGIDO: nome do arquivo alinhado com o que treinar_arrecadacao.py salva
    path = os.path.join(MODELS_DIR, 'metricas_arrecadacao.json')
    if not os.path.exists(path):
        return _erro('Métricas não encontradas. Treine o modelo primeiro.', 404)
    with open(path, encoding='utf-8') as f:
        return jsonify(json.load(f))

# ENDPOINT: /resultados  — histórico de predições para o front puxar

@app.route('/resultados', methods=['GET'])
def resultados():
    historico = list(_historico)
    tipo = request.args.get('tipo', '').upper()
    if tipo in ('FALTA', 'ARRECADACAO'):
        historico = [r for r in historico if r['tipo'] == tipo]
    return jsonify(historico)

# ENDPOINT: /paciente/<nome>
#
# Exemplo: GET /paciente/Maria
#
# Output JSON:
# [
#   {
#     "idPaciente": 1,
#     "nome": "Maria Oliveira",
#     "programa": "DENTISTAS_DO_BEM",
#     "rendaFamiliar": 900.0,
#     "distanciaKm": 15.0,
#     "turnoPref": "MANHA",
#     "consultas": [
#       {
#         "idConsulta": 3,
#         "dataConsulta": "2025-04-17",
#         "turno": "NOITE",
#         "status": "FALTA",
#         "distConsulta": 15.0,
#         "diasAteConsulta": 7
#       }
#     ]
#   }
# ]

@app.route('/paciente/<nome>', methods=['GET'])
def buscar_paciente(nome):
    try:
        conn = _conectar_oracle()
        sql = """
            SELECT
                pa.id_paciente,
                pe.nome,
                pa.programa,
                pa.renda_familiar,
                pa.distancia_km,
                pa.turno_preferencial,
                c.id_consulta,
                TO_CHAR(c.data_consulta, 'YYYY-MM-DD') AS data_consulta,
                c.turno,
                c.status,
                c.distancia_km AS dist_consulta,
                (c.data_consulta - SYSDATE) AS dias_ate_consulta
            FROM tdb_Paciente pa
            JOIN tdb_Pessoa pe ON pe.cpf = pa.cpf
            LEFT JOIN tdb_Consulta c ON c.id_paciente = pa.id_paciente
            WHERE UPPER(pe.nome) LIKE UPPER(:nome)
            ORDER BY pa.id_paciente, c.data_consulta DESC
        """
        cursor = conn.cursor()
        cursor.execute(sql, nome=f'%{nome}%')
        rows = cursor.fetchall()
        cols = [d[0].lower() for d in cursor.description]
        conn.close()

        # Agrupa consultas por paciente
        pacientes = {}
        for row in rows:
            r = dict(zip(cols, row))
            pid = r['id_paciente']
            if pid not in pacientes:
                pacientes[pid] = {
                    'idPaciente': pid,
                    'nome': r['nome'],
                    'programa': r['programa'],
                    'rendaFamiliar': float(r['renda_familiar'] or 0),
                    'distanciaKm': float(r['distancia_km'] or 0),
                    'turnoPref': r['turno_preferencial'],
                    'consultas': [],
                }
            if r['id_consulta']:
                pacientes[pid]['consultas'].append({
                    'idConsulta': int(r['id_consulta']),
                    'dataConsulta': r['data_consulta'],
                    'turno': r['turno'],
                    'status': r['status'],
                    'distConsulta': float(r['dist_consulta'] or 0),
                    'diasAteConsulta': int(r['dias_ate_consulta'] or 0),
                })

        return jsonify(list(pacientes.values()))

    except Exception as e:
        return _erro(f'Erro ao buscar paciente: {str(e)}', 500)


# ENDPOINT: /predict/falta/consulta/<id_consulta>
#
# Output JSON: mesmo formato do /predict/falta

@app.route('/predict/falta/consulta/<int:id_consulta>', methods=['GET'])
def predict_falta_por_consulta(id_consulta):
    if not artefato_falta:
        return _erro('Modelo de falta não encontrado. Execute treinar_falta.py primeiro.', 503)

    try:
        conn = _conectar_oracle()
        sql = """
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
            WHERE c.id_consulta = :id_consulta
        """
        cursor = conn.cursor()
        cursor.execute(sql, id_consulta=id_consulta)
        row = cursor.fetchone()
        conn.close()

        if not row:
            return _erro(f'Consulta {id_consulta} não encontrada.', 404)

        cols = [d[0].lower() for d in cursor.description]
        r = dict(zip(cols, row))

        body = {
            'distanciaKm': float(r['distancia_km'] or 0),
            'rendaFamiliar': float(r['renda_familiar'] or 0),
            'turnoPref': r['turno_preferencial'],
            'programa': r['programa'],
            'faltasAnteriores': int(r['faltas_anteriores'] or 0),
            'totalConsultas': int(r['total_consultas'] or 1),
            'turnoConsulta': r['turno_consulta'],
            'distConsulta': float(r['dist_consulta'] or 0),
            'diasAteConsulta': int(r['dias_ate_consulta'] or 0),
        }

        X = _parse_falta(body)
        X = X[artefato_falta['features']]
        pipe = artefato_falta['pipeline']

        prob = float(pipe.predict_proba(X)[0][1])
        risco = _risco_falta(prob)
        classe = 'FALTA' if prob >= 0.5 else 'NAO_FALTA'

        saida = {
            'sucesso': True,
            'idConsulta': id_consulta,
            'probabilidadeFalta': round(prob, 4),
            'risco': risco,
            'classePrevista': classe,
            'recomendacao': _recomendacao_falta(risco, body['diasAteConsulta']),
            'modeloVersao': artefato_falta.get('versao', 'v1.0'),
        }

        _salvar_resultado('FALTA', body, saida)
        return jsonify(saida)

    except Exception as e:
        return _erro(f'Erro interno: {str(e)}', 500)


# ENDPOINT: POST /retreinar
# Output JSON:
# {
#   "sucesso": true,
#   "mensagem": "Modelos retreinados com sucesso.",
#   "timestamp": "2025-05-04T12:00:00"
# }

@app.route('/retreinar', methods=['POST'])
def retreinar():
    global artefato_falta, artefato_arrec
    try:
        # detecta Python do venv em Windows e Linux/Mac
        venv_win   = os.path.join(BASE, '..', 'venv', 'Scripts', 'python.exe')
        venv_unix  = os.path.join(BASE, '..', 'venv', 'bin', 'python')
        if os.path.exists(venv_win):
            python = os.path.abspath(venv_win)
        elif os.path.exists(venv_unix):
            python = os.path.abspath(venv_unix)
        else:
            python = sys.executable

        load_script  = os.path.join(SCRIPTS_DIR, 'gerar_dados.py')
        falta_script = os.path.join(SCRIPTS_DIR, 'treinar_falta.py')
        arrec_script = os.path.join(SCRIPTS_DIR, 'treinar_arrecadacao.py')

        erros = []

        # Atualiza os CSVs do banco
        r = subprocess.run([python, load_script], capture_output=True)
        if r.returncode != 0:
            erros.append(f'gerar_dados: {r.stderr.decode(errors="replace").strip()}')

        # Retreina falta
        r = subprocess.run([python, falta_script], capture_output=True)
        if r.returncode != 0:
            erros.append(f'treinar_falta: {r.stderr.decode(errors="replace").strip()}')

        # Retreina arrecadação
        r = subprocess.run([python, arrec_script], capture_output=True)
        if r.returncode != 0:
            erros.append(f'treinar_arrecadacao: {r.stderr.decode(errors="replace").strip()}')

        # retorna erro detalhado se qualquer script falhar
        if erros:
            return _erro('Falha no retreino:\n' + '\n'.join(erros), 500)

        # Recarrega os modelos na memória sem precisar reiniciar a API
        artefato_falta = _carregar('falta.joblib')
        artefato_arrec = _carregar('arrecadacao.joblib')

        return jsonify({
            'sucesso': True,
            'mensagem': 'Modelos retreinados e recarregados com sucesso.',
            'timestamp': datetime.datetime.now().isoformat(),
            'modelos': {
            'falta': 'carregado' if artefato_falta else 'não encontrado',
            'arrecadacao': 'carregado' if artefato_arrec else 'não encontrado',
            },
        })
    except Exception as e:
        return _erro(f'Erro interno: {str(e)}', 500)

# MAIN

if __name__ == '__main__':
    print("=" * 60)
    print(" Turma do Bem — API de IA | Flask | Porta 8000")
    print("=" * 60)
    print(f" Modelo falta: {'carregado' if artefato_falta  else 'não encontrado'}")
    print(f" Modelo arrecadação: {'carregado' if artefato_arrec  else 'não encontrado'}")
    print("=" * 60)
    app.run(host='0.0.0.0', port=8000, debug=False)