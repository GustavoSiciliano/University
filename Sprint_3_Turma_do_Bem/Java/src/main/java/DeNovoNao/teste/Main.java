package DeNovoNao.teste;

import DeNovoNao.dao.MenuGeral;
import DeNovoNao.modelo.*;

public class Main {

    public static void main(String[] args) {

        // Título inicial dos testes
        System.out.println("TESTES DE LOGICA DE NEGOCIO");

        // TESTE 1
        // Aqui testamos a validação de CPF da classe Pessoa
        System.out.println("-- TESTE 1: validarCpf");

        Pessoa p1 = new Pessoa("Ana Lima", "123.456.789-01", "11999999999", "ana@email.com", "2000-05-10");
        Pessoa p2 = new Pessoa("Carlos", "123", null, null, "1990-01-01");

        // Mostra no console se o CPF foi considerado válido ou inválido
        System.out.println("CPF valido (123.456.789-01): " + p1.validarCpf());
        System.out.println("CPF invalido (123): " + p2.validarCpf());

        // TESTE 2
        // Aqui testamos se o cadastro do dentista está correto
        System.out.println("\n-- TESTE 2: cadastroValido");

        Dentista d1 = new Dentista("Dr. Paulo", "987.654.321-00", "11988887777",
                "paulo@fiap.com", "1985-03-20", "CRO-SP 12345", "Ortodontia", "Seg-Sex");

        Dentista d2 = new Dentista("Sem CRO", "111.222.333-44", null,
                null, "1990-06-15", "", "Geral", "Seg");

        // Verifica se o dentista tem cadastro válido e se pode atender
        System.out.println("Dentista com CRO valido: " + d1.cadastroValido());
        System.out.println("Dentista sem CRO: " + d2.cadastroValido());
        System.out.println("Pode atender (d1): " + d1.podeAtender());

        // TESTE 3
        // Aqui testamos o agendamento e cancelamento de consulta
        System.out.println("\n-- TESTE 3: agendarConsulta e cancelarConsulta");

        Paciente pac = new Paciente(1, "Ana Lima", "123.456.789-01",
                "11999999999", "ana@email.com", "2000-05-10", "Rua A, 10", "Unimed");

        Consulta c1 = new Consulta(1, pac, d1, "2025-06-15", null);
        Consulta c2 = new Consulta(2, pac, d1, "2025-07-01", "REALIZADA");
        Consulta c3 = new Consulta(3, null, d1, "2025-08-01", null);

        // Testa consulta normal, cancelamento e erro sem paciente
        System.out.println("Agendar consulta completa: " + c1.agendarConsulta());
        System.out.println("Status apos agendar: " + c1.getStatus());
        System.out.println("Cancelar consulta REALIZADA: " + c2.cancelarConsulta());
        System.out.println("Agendar sem paciente: " + c3.agendarConsulta());

        // TESTE 4
        // Aqui testamos a atualização de prontuário
        System.out.println("\n-- TESTE 4: atualizarProntuario");

        Prontuario pr1 = new Prontuario(1, pac, "Historico inicial", "2025-01-10", null, "");
        Prontuario pr2 = new Prontuario(2, null, "Sem paciente", "2025-01-10", null, "");

        // Testa atualização correta e casos inválidos
        System.out.println("Atualizar com dados validos: " + pr1.atualizarProntuario("Dente extraido", "2025-06-20"));
        System.out.println("Observacao apos update: " + pr1.getObservacoes());
        System.out.println("Atualizar sem paciente: " + pr2.atualizarProntuario("Teste", "2025-06-20"));
        System.out.println("Atualizar com data invalida: " + pr1.atualizarProntuario("Revisao", "20/06/2025"));

        // TESTE 5
        // Aqui testamos se a doação é válida e o cálculo de bônus
        System.out.println("\n-- TESTE 5: validarDoacao e aplicarBonus");

        Doacao doa1 = new Doacao(1, 10, 500.00, "2025-06-01");
        Doacao doa2 = new Doacao(2, 0, -100.00, null);

        // Mostra se a doação foi aceita e o valor final com bônus
        System.out.println("Doacao valida (R$500): " + doa1.validarDoacao());
        System.out.println("Doacao invalida (valor negativo): " + doa2.validarDoacao());
        System.out.printf("Valor com bonus 10%%: R$ %.2f%f", doa1.aplicarBonus(10));

        // TESTE 6
        // Aqui testamos se as datas da campanha estão corretas
        System.out.println("\n-- TESTE 6: datasValidas");

        Campanha camp1 = new Campanha(1, "Sorria SP", "Atendimento gratuito",
                "2025-07-01", "2025-07-31", 1);

        Campanha camp2 = new Campanha(2, "Campanha Invalida", "Teste",
                "2025-07-15", "2025-07-01", 0);

        // Verifica datas e cadastro da campanha
        System.out.println("Datas validas (fim > inicio): " + camp1.datasValidas());
        System.out.println("Datas invalidas (fim < inicio): " + camp2.datasValidas());
        System.out.println("Cadastro valido (camp1): " + camp1.cadastroValido());

        // Final dos testes
        System.out.println("FIM DOS TESTES");

        // Inicia o menu do sistema
        MenuGeral menu = new MenuGeral();
        menu.executarMenu();
    }
}