"""
Gustavo Rodrigues Siciliano: RM568419
Gustavo de Jesus Silva: RM567926
Samuel Keniti Kina de Lima: 567614

API Flask — Turma do Bem · IA
Porta: 8000

Endpoints:
  GET  /health
  POST /predict/falta
  POST /predict/arrecadacao
  GET  /metricas/falta
  GET  /metricas/arrecadacao
  GET  /resultados
  GET  /paciente/<nome>
  GET  /predict/falta/consulta/<id_consulta>
  POST /retreinar
"""

import os, json, datetime, uuid, subprocess, sys, traceback
import numpy as np
import pandas as pd
import joblib
import oracledb
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

BASE       = os.path.dirname(__file__)
MODELS_DIR = os.path.join(BASE, 'models')
DATA_DIR   = os.path.join(BASE, 'data')
SCRIPTS_DIR = os.path.join(BASE, 'scripts')

DB_CONFIG = {
    'user':     'rm568419',
    'password': '250204',
    'host':     'oracle.fiap.com.br',
    'port':     1521,
    'sid':      'orcl',
}

def _conectar_oracle():
    return oracledb.connect(**DB_CONFIG)

def _carregar(nome_arquivo):
    path = os.path.join(MODELS_DIR, nome_arquivo)
    if not os.path.exists(path):
        return None
    return joblib.load(path)

artefato_falta = _carregar('falta.joblib')
artefato_arrec = _carregar('arrecadacao.joblib')

# Historico em memoria (nao depende de disco — compativel com Render gratuito)
_historico = []

def _salvar_resultado(tipo: str, entrada: dict, saida: dict):
    """Persiste predicao em memoria. No Render gratuito nao ha disco persistente."""
    _historico.append({
        'id':          str(uuid.uuid4()),
        'tipo':        tipo,
        'dt_predicao': datetime.datetime.now().isoformat(),
        'entrada':     entrada,
        'saida':       saida,
    })

def _erro(msg: str, code: int = 400):
    return jsonify({'sucesso': False, 'erro': msg}), code

TURNO_MAP    = {'MANHA': 0, 'TARDE': 1, 'NOITE': 2}
PROGRAMA_MAP = {'DENTISTAS_DO_BEM': 0, 'APOLONICAS_DO_BEM': 1}

# ==============================================================================
# HEALTH
# ==============================================================================

@app.route('/health', methods=['GET'])
def health():
    return jsonify({
        'status':  'ok',
        'servico': 'Turma do Bem — API de IA',
        'versao':  'v1.0',
        'modelos': {
            'falta':       'carregado' if artefato_falta else 'nao encontrado — rode run.py',
            'arrecadacao': 'carregado' if artefato_arrec else 'nao encontrado — rode run.py',
        },
        'timestamp': datetime.datetime.now().isoformat(),
    })

# ==============================================================================
# FALTA
# ==============================================================================

def _turno(v):
    if isinstance(v, int):
        return v
    return TURNO_MAP.get(str(v).strip().upper(), 1)

def _prog(v):
    if isinstance(v, int):
        return v
    return PROGRAMA_MAP.get(str(v).strip().upper(), 0)

def _parse_falta(body: dict) -> pd.DataFrame:
    distancia_km      = float(body['distanciaKm'])
    renda_familiar    = float(body['rendaFamiliar'])
    faltas_ant        = int(body['faltasAnteriores'])
    turno_pref        = _turno(body.get('turnoPref', 1))
    programa          = _prog(body.get('programa', 0))
    total_consultas   = max(int(body.get('totalConsultas', max(faltas_ant, 1))), 1)
    turno_consulta    = _turno(body.get('turnoConsulta', 1))
    dist_consulta     = float(body.get('distConsulta', distancia_km))
    dias_ate_consulta = int(body.get('diasAteConsulta', 3))

    taxa_falta_hist = faltas_ant / total_consultas
    score_risco = (
        (distancia_km / 50) * 0.4 +
        (1 - min(renda_familiar, 5000) / 5000) * 0.4 +
        taxa_falta_hist * 0.2
    )

    return pd.DataFrame([{
        'distancia_km':         distancia_km,
        'renda_familiar':       renda_familiar,
        'turno_preferencial':   turno_pref,
        'programa':             programa,
        'faltas_anteriores':    faltas_ant,
        'total_consultas':      total_consultas,
        'turno_consulta':       turno_consulta,
        'dist_consulta':        dist_consulta,
        'dias_ate_consulta':    dias_ate_consulta,
        'taxa_falta_historica': taxa_falta_hist,
        'score_risco':          score_risco,
    }])

def _risco_falta(prob: float) -> str:
    if prob >= 0.65: return 'ALTO'
    if prob >= 0.35: return 'MEDIO'
    return 'BAIXO'

def _recomendacao_falta(risco: str, dias: int) -> str:
    if risco == 'ALTO':
        return 'Enviar lembrete por SMS/WhatsApp 48h antes e confirmar presenca 24h antes.'
    if risco == 'MEDIO':
        return 'Enviar lembrete por e-mail 48h antes da consulta.'
    return 'Nenhuma acao especial necessaria — paciente com baixo risco de falta.'

@app.route('/predict/falta', methods=['POST'])
def predict_falta():
    if not artefato_falta:
        return _erro('Modelo de falta nao encontrado. Execute run.py primeiro.', 503)

    body = request.get_json(silent=True)
    if not body:
        return _erro('JSON invalido ou ausente.')

    for c in ['distanciaKm', 'rendaFamiliar', 'faltasAnteriores']:
        if c not in body:
            return _erro(f'Campo obrigatorio ausente: {c}')

    try:
        X    = _parse_falta(body)
        X    = X[artefato_falta['features']]
        pipe = artefato_falta['pipeline']

        prob   = float(pipe.predict_proba(X)[0][1])
        risco  = _risco_falta(prob)
        classe = 'FALTA' if prob >= 0.5 else 'NAO_FALTA'

        saida = {
            'sucesso':            True,
            'probabilidadeFalta': round(prob, 4),
            'risco':              risco,
            'classePrevista':     classe,
            'recomendacao':       _recomendacao_falta(risco, body.get('diasAteConsulta', 3)),
            'modeloVersao':       artefato_falta.get('versao', 'v1.0'),
        }

        _salvar_resultado('FALTA', body, saida)
        return jsonify(saida)

    except Exception as e:
        traceback.print_exc()
        return _erro(f'Erro interno: {str(e)}', 500)

# ==============================================================================
# ARRECADACAO
# ==============================================================================

SAZON = {
    1: 0.85, 2: 0.80, 3: 0.88, 4: 0.90,  5: 1.20, 6: 1.00,
    7: 0.95, 8: 0.92, 9: 0.98, 10: 1.15, 11: 1.05, 12: 1.30
}

def _parse_arrec(body: dict) -> pd.DataFrame:
    meta_valor           = float(body['metaValor'])
    duracao_dias         = int(body['duracaoDias'])
    mes_inicio           = int(body['mesInicio'])
    ano_inicio           = int(body.get('anoInicio', 2025))
    campanhas_anteriores = int(body['campanhasAnteriores'])
    qtd_voluntarios      = max(int(body.get('qtdVoluntarios', 5)), 1)
    qtd_doacoes          = max(int(body.get('qtdDoacoes', max(1, int(meta_valor / 300)))), 1)

    fator_sazon     = SAZON.get(mes_inicio, 1.0)
    doacoes_por_vol = qtd_doacoes / qtd_voluntarios

    return pd.DataFrame([{
        'meta_valor':            meta_valor,
        'duracao_dias':          duracao_dias,
        'mes_inicio':            mes_inicio,
        'ano_inicio':            ano_inicio,
        'campanhas_anteriores':  campanhas_anteriores,
        'qtd_doacoes':           qtd_doacoes,
        'qtd_voluntarios':       qtd_voluntarios,
        'fator_sazonalidade':    fator_sazon,
        'doacoes_por_voluntario': doacoes_por_vol,
    }])

def _tendencia(previsto: float, meta: float) -> str:
    pct = previsto / max(meta, 1)
    if pct >= 1.10: return 'ALTA'
    if pct >= 0.85: return 'ESTAVEL'
    return 'BAIXA'

def _recomendacao_arrec(tendencia: str, pct: float) -> str:
    if tendencia == 'ALTA':
        return f'Campanha deve superar a meta ({pct*100:.0f}% previsto). Mantenha a estrategia atual.'
    if tendencia == 'ESTAVEL':
        return f'Campanha proxima da meta ({pct*100:.0f}% previsto). Intensifique a comunicacao na reta final.'
    return f'Risco de nao atingir a meta ({pct*100:.0f}% previsto). Considere ampliar canais de divulgacao.'

@app.route('/predict/arrecadacao', methods=['POST'])
def predict_arrecadacao():
    if not artefato_arrec:
        return _erro('Modelo de arrecadacao nao encontrado. Execute run.py primeiro.', 503)

    body = request.get_json(silent=True)
    if not body:
        return _erro('JSON invalido ou ausente.')

    for c in ['metaValor', 'duracaoDias', 'mesInicio', 'campanhasAnteriores']:
        if c not in body:
            return _erro(f'Campo obrigatorio ausente: {c}')

    try:
        X    = _parse_arrec(body)
        X    = X[artefato_arrec['features']]
        pipe = artefato_arrec['pipeline']

        valor_previsto = max(float(pipe.predict(X)[0]), 0.0)
        meta           = float(body['metaValor'])
        pct            = valor_previsto / max(meta, 1)
        tendencia      = _tendencia(valor_previsto, meta)

        metricas_path = os.path.join(MODELS_DIR, 'metricas_arrecadacao.json')
        confianca = 0.80
        if os.path.exists(metricas_path):
            with open(metricas_path, encoding='utf-8') as f:
                confianca = round(max(0.0, min(1.0, json.load(f).get('r2_melhor', 0.80))), 4)

        saida = {
            'sucesso':       True,
            'valorPrevisto': round(valor_previsto, 2),
            'confianca':     confianca,
            'tendencia':     tendencia,
            'pctMeta':       round(pct, 4),
            'recomendacao':  _recomendacao_arrec(tendencia, pct),
            'modeloVersao':  artefato_arrec.get('versao', 'v1.0'),
        }

        _salvar_resultado('ARRECADACAO', body, saida)
        return jsonify(saida)

    except Exception as e:
        traceback.print_exc()
        return _erro(f'Erro interno: {str(e)}', 500)

# ==============================================================================
# METRICAS
# ==============================================================================

@app.route('/metricas/falta', methods=['GET'])
def metricas_falta():
    path = os.path.join(MODELS_DIR, 'metricas_falta.json')
    if not os.path.exists(path):
        return _erro('Metricas nao encontradas.', 404)
    with open(path, encoding='utf-8') as f:
        return jsonify(json.load(f))

@app.route('/metricas/arrecadacao', methods=['GET'])
def metricas_arrecadacao():
    path = os.path.join(MODELS_DIR, 'metricas_arrecadacao.json')
    if not os.path.exists(path):
        return _erro('Metricas nao encontradas.', 404)
    with open(path, encoding='utf-8') as f:
        return jsonify(json.load(f))

# ==============================================================================
# RESULTADOS
# ==============================================================================

@app.route('/resultados', methods=['GET'])
def resultados():
    historico = list(_historico)
    tipo = request.args.get('tipo', '').upper()
    if tipo in ('FALTA', 'ARRECADACAO'):
        historico = [r for r in historico if r['tipo'] == tipo]
    return jsonify(historico)

# ==============================================================================
# PACIENTE / CONSULTA (requerem Oracle — so funcionam com VPN FIAP)
# ==============================================================================

@app.route('/paciente/<nome>', methods=['GET'])
def buscar_paciente(nome):
    try:
        conn   = _conectar_oracle()
        cursor = conn.cursor()
        cursor.execute("""
            SELECT pa.id_paciente, pe.nome, pa.programa,
                   pa.renda_familiar, pa.distancia_km, pa.turno_preferencial,
                   c.id_consulta, TO_CHAR(c.data_consulta,'YYYY-MM-DD'),
                   c.turno, c.status, c.distancia_km,
                   (c.data_consulta - SYSDATE)
            FROM tdb_Paciente pa
            JOIN tdb_Pessoa pe ON pe.cpf = pa.cpf
            LEFT JOIN tdb_Consulta c ON c.id_paciente = pa.id_paciente
            WHERE UPPER(pe.nome) LIKE UPPER(:nome)
            ORDER BY pa.id_paciente, c.data_consulta DESC
        """, nome=f'%{nome}%')
        rows = cursor.fetchall()
        cols = [d[0].lower() for d in cursor.description]
        conn.close()

        pacientes: dict = {}
        for row in rows:
            r   = dict(zip(cols, row))
            pid = r['id_paciente']
            if pid not in pacientes:
                pacientes[pid] = {
                    'idPaciente':   pid,
                    'nome':         r['nome'],
                    'programa':     r['programa'],
                    'rendaFamiliar': float(r['renda_familiar'] or 0),
                    'distanciaKm':  float(r['distancia_km'] or 0),
                    'turnoPref':    r['turno_preferencial'],
                    'consultas':    [],
                }
            if r['id_consulta']:
                pacientes[pid]['consultas'].append({
                    'idConsulta':     int(r['id_consulta']),
                    'dataConsulta':   r['to_char(c.data_consulta,\'yyyy-mm-dd\')'],
                    'turno':          r['turno'],
                    'status':         r['status'],
                    'distConsulta':   float(r['distancia_km_1'] or 0),
                    'diasAteConsulta': int(r['(c.data_consulta-sysdate)'] or 0),
                })
        return jsonify(list(pacientes.values()))

    except Exception as e:
        traceback.print_exc()
        return _erro(f'Erro ao buscar paciente: {str(e)}', 500)

@app.route('/predict/falta/consulta/<int:id_consulta>', methods=['GET'])
def predict_falta_por_consulta(id_consulta):
    if not artefato_falta:
        return _erro('Modelo de falta nao encontrado.', 503)
    try:
        conn   = _conectar_oracle()
        cursor = conn.cursor()
        cursor.execute("""
            SELECT pa.distancia_km, pa.renda_familiar,
                   pa.turno_preferencial, pa.programa,
                   (SELECT COUNT(*) FROM tdb_Consulta c2
                    WHERE c2.id_paciente = pa.id_paciente
                      AND c2.status = 'FALTA') AS faltas_anteriores,
                   (SELECT COUNT(*) FROM tdb_Consulta c3
                    WHERE c3.id_paciente = pa.id_paciente) AS total_consultas,
                   c.turno, c.distancia_km,
                   (c.data_consulta - SYSDATE)
            FROM tdb_Consulta c
            JOIN tdb_Paciente pa ON pa.id_paciente = c.id_paciente
            WHERE c.id_consulta = :id
        """, id=id_consulta)
        row  = cursor.fetchone()
        cols = [d[0].lower() for d in cursor.description]
        conn.close()

        if not row:
            return _erro(f'Consulta {id_consulta} nao encontrada.', 404)

        r    = dict(zip(cols, row))
        body = {
            'distanciaKm':      float(r['distancia_km'] or 0),
            'rendaFamiliar':    float(r['renda_familiar'] or 0),
            'turnoPref':        r['turno_preferencial'],
            'programa':         r['programa'],
            'faltasAnteriores': int(r['faltas_anteriores'] or 0),
            'totalConsultas':   max(int(r['total_consultas'] or 1), 1),
            'turnoConsulta':    r['turno'],
            'distConsulta':     float(r['distancia_km_1'] or 0),
            'diasAteConsulta':  int(r['(c.data_consulta-sysdate)'] or 0),
        }

        X    = _parse_falta(body)
        X    = X[artefato_falta['features']]
        prob = float(artefato_falta['pipeline'].predict_proba(X)[0][1])
        risco  = _risco_falta(prob)
        classe = 'FALTA' if prob >= 0.5 else 'NAO_FALTA'

        saida = {
            'sucesso':            True,
            'idConsulta':         id_consulta,
            'probabilidadeFalta': round(prob, 4),
            'risco':              risco,
            'classePrevista':     classe,
            'recomendacao':       _recomendacao_falta(risco, body['diasAteConsulta']),
            'modeloVersao':       artefato_falta.get('versao', 'v1.0'),
        }
        _salvar_resultado('FALTA', body, saida)
        return jsonify(saida)

    except Exception as e:
        traceback.print_exc()
        return _erro(f'Erro interno: {str(e)}', 500)

# ==============================================================================
# RETREINAR
# ==============================================================================

@app.route('/retreinar', methods=['POST'])
def retreinar():
    global artefato_falta, artefato_arrec
    try:
        python  = sys.executable
        scripts = [
            ('gerar_dados',         os.path.join(SCRIPTS_DIR, 'gerar_dados.py')),
            ('treinar_falta',       os.path.join(SCRIPTS_DIR, 'treinar_falta.py')),
            ('treinar_arrecadacao', os.path.join(SCRIPTS_DIR, 'treinar_arrecadacao.py')),
        ]
        erros = []
        for nome, path in scripts:
            r = subprocess.run([python, path], capture_output=True)
            if r.returncode != 0:
                erros.append(f'{nome}: {r.stderr.decode(errors="replace").strip()}')

        if erros:
            return _erro('Falha no retreino:\n' + '\n'.join(erros), 500)

        artefato_falta = _carregar('falta.joblib')
        artefato_arrec = _carregar('arrecadacao.joblib')

        return jsonify({
            'sucesso':   True,
            'mensagem':  'Modelos retreinados e recarregados.',
            'timestamp': datetime.datetime.now().isoformat(),
            'modelos': {
                'falta':       'carregado' if artefato_falta else 'nao encontrado',
                'arrecadacao': 'carregado' if artefato_arrec else 'nao encontrado',
            },
        })
    except Exception as e:
        traceback.print_exc()
        return _erro(f'Erro interno: {str(e)}', 500)

# ==============================================================================
# MAIN
# ==============================================================================

if __name__ == '__main__':
    print('=' * 60)
    print(' Turma do Bem — API de IA | Flask | Porta 8000')
    print('=' * 60)
    print(f" Modelo falta:       {'carregado' if artefato_falta else 'nao encontrado'}")
    print(f" Modelo arrecadacao: {'carregado' if artefato_arrec else 'nao encontrado'}")
    print('=' * 60)
    port = int(os.environ.get('PORT', 8000))
    app.run(host='0.0.0.0', port=port, debug=False)
