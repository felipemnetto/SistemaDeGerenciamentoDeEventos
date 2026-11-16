package sistemaDeGerenciamentoDeEventos;

import sistemaDeGerenciamentoDeEventos.dominio.GestaoEventos;

/**
 * Guarda a única GestaoEventos da aplicação (mesmo papel da Sistema do t07).
 * O Vaadin cria uma tela nova a cada F5; se os dados morassem na tela,
 * seriam perdidos. Atributo static: todos os navegadores compartilham os dados.
 */
public final class Sistema {
    private static final GestaoEventos GESTAO = DadosIniciais.criarGestao();

    private Sistema() {
    }

    public static GestaoEventos gestao() {
        return GESTAO;
    }
}
