package DeNovoNao.dao;

import java.util.Scanner;

// Classe responsável por controlar o menu principal do sistema
public class MenuGeral {

    private Scanner sc = new Scanner(System.in);

    // Metodo principal que mantém o sistema rodando
    public void executarMenu() {
        int opcao = -1;

        // Enquanto a opção for diferente de 0, o menu continua aberto
        while (opcao != 0) {
            exibirMenuPrincipal();
            opcao = lerInt();

            // Direciona para o submenu escolhido
            switch (opcao) {
                case 1:
                    menuPaciente();
                    break;
                case 2:
                    menuDentista();
                    break;
                case 3:
                    menuVoluntario();
                    break;
                case 4:
                    menuDoador();
                    break;
                case 5:
                    menuDoacao();
                    break;
                case 6:
                    menuConsulta();
                    break;
                case 7:
                    menuProntuario();
                    break;
                case 8:
                    menuProcedimento();
                    break;
                case 9:
                    menuMaterial();
                    break;
                case 10:
                    menuCampanha();
                    break;
                case 0:
                    System.out.println("\n  Encerrando o sistema. Ate logo!\n");
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Mostra o menu principal na tela
    private void exibirMenuPrincipal() {
        System.out.println();
        System.out.println("  +===========================================+");
        System.out.println("  |       SISTEMA ONG - TURMA DO BEM         |");
        System.out.println("  +===========================================+");
        System.out.println("  |  1.  Pacientes                           |");
        System.out.println("  |  2.  Dentistas                           |");
        System.out.println("  |  3.  Voluntarios                         |");
        System.out.println("  |  4.  Doadores                            |");
        System.out.println("  |  5.  Doacoes                             |");
        System.out.println("  |  6.  Consultas                           |");
        System.out.println("  |  7.  Prontuarios                         |");
        System.out.println("  |  8.  Procedimentos                       |");
        System.out.println("  |  9.  Materiais                           |");
        System.out.println("  |  10. Campanhas                           |");
        System.out.println("  |  0.  Sair                                |");
        System.out.println("  +===========================================+");
        System.out.print("  Escolha: ");
    }

    // Menu da parte de pacientes
    private void menuPaciente() {
        PacienteDAO dao = new PacienteDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("PACIENTES");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de dentistas
    private void menuDentista() {
        DentistaDAO dao = new DentistaDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("DENTISTAS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de voluntários
    private void menuVoluntario() {
        VoluntarioDAO dao = new VoluntarioDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("VOLUNTARIOS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de doadores
    private void menuDoador() {
        DoadorDAO dao = new DoadorDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("DOADORES");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de doações
    // Aqui não tem atualizar, só cadastrar, listar e excluir
    private void menuDoacao() {
        DoacaoDAO dao = new DoacaoDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenuSemUpdate("DOACOES");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de consultas
    private void menuConsulta() {
        ConsultaDAO dao = new ConsultaDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("CONSULTAS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de prontuários
    private void menuProntuario() {
        ProntuarioDAO dao = new ProntuarioDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("PRONTUARIOS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de procedimentos
    private void menuProcedimento() {
        ProcedimentoDAO dao = new ProcedimentoDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("PROCEDIMENTOS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de materiais
    private void menuMaterial() {
        MaterialDAO dao = new MaterialDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("MATERIAIS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Menu da parte de campanhas
    private void menuCampanha() {
        CampanhaDAO dao = new CampanhaDAO(sc);
        int op = -1;

        while (op != 0) {
            exibirSubmenu("CAMPANHAS");
            op = lerInt();

            switch (op) {
                case 1:
                    dao.cadastrar();
                    break;
                case 2:
                    dao.listar();
                    break;
                case 3:
                    dao.atualizar();
                    break;
                case 4:
                    dao.excluir();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("  Opcao invalida.");
            }
        }
    }

    // Mostra submenu padrão com cadastrar, listar, atualizar e excluir
    private void exibirSubmenu(String titulo) {
        System.out.println();
        System.out.println("  +---------------------------+");
        System.out.printf("  |  %-26s|\n", titulo);
        System.out.println("  +---------------------------+");
        System.out.println("  |  1. Cadastrar             |");
        System.out.println("  |  2. Listar                |");
        System.out.println("  |  3. Atualizar             |");
        System.out.println("  |  4. Excluir               |");
        System.out.println("  |  0. Voltar                |");
        System.out.println("  +---------------------------+");
        System.out.print("  Escolha: ");
    }

    // Mostra submenu sem opção de atualizar
    private void exibirSubmenuSemUpdate(String titulo) {
        System.out.println();
        System.out.println("  +---------------------------+");
        System.out.printf("  |  %-26s|\n", titulo);
        System.out.println("  +---------------------------+");
        System.out.println("  |  1. Cadastrar             |");
        System.out.println("  |  2. Listar                |");
        System.out.println("  |  3. Excluir               |");
        System.out.println("  |  0. Voltar                |");
        System.out.println("  +---------------------------+");
        System.out.print("  Escolha: ");
    }

    // Faz leitura segura de número inteiro
    // Se o usuário digitar algo errado, retorna -1
    private int lerInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}