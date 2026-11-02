package sistemaDeGerenciamentoDeEventos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void aplicacaoPodeSerInstanciada() {
        App app = new App();

        assertNotNull(app);
    }
}
