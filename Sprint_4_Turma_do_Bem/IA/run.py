"""
Gustavo Rodrigues Siciliano: RM568419
Gustavo de Jesus Silva: RM567926
Samuel Keniti Kina de Lima: 567614

Pipeline completo — execute este arquivo para subir a IA do zero:

  python run.py: treina os modelos e sobe a API
  python run.py --only-api: sobe a API sem retreinar
  python run.py --only-train: apenas treina, não sobe a API

Pré-requisito: banco Oracle acessível com dados nas tabelas tdb_*.
Se o banco estiver fora, gerar_dados.py vai lançar exceção e o treino para.
"""

import sys, os

BASE = os.path.dirname(__file__)
sys.path.insert(0, os.path.join(BASE, 'scripts'))

only_api = '--only-api' in sys.argv
only_train = '--only-train' in sys.argv

if not only_api:
    print("=" * 60)
    print(" Turma do Bem — IA | Pipeline de Treino")
    print("=" * 60)

    # Etapa 1: lê dados reais do Oracle e gera os CSVs
    print("\nEtapa 1/3 — Carregando dados do banco Oracle\n")
    from gerar_dados import main as gerar
    gerar()  # lança exceção se o banco estiver inacessível

    # Etapa 2: treina classificador de falta
    print("\nEtapa 2/3 — Modelo de Falta\n")
    from treinar_falta import treinar as treinar_falta
    treinar_falta()

    # Etapa 3: treina regressor de arrecadação
    print("\nEtapa 3/3 — Modelo de Arrecadação\n")
    from treinar_arrecadacao import treinar as treinar_arrec
    treinar_arrec()

# Sobe a API Flask

if not only_train:
    print("\nIniciando servidor Flask na porta 8000...\n")
    os.chdir(BASE)
    import app as flask_app
    flask_app.app.run(host='0.0.0.0', port=8000, debug=False)
