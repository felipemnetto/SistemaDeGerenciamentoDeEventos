package sistemaDeGerenciamentoDeEventos.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Formatação de valores para exibição na tela (só a interface usa). */
public final class Formatos {
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale BRASIL = Locale.of("pt", "BR");

    private Formatos() {
    }

    public static String data(LocalDate data) {
        return data == null ? "" : data.format(DATA);
    }

    public static String dinheiro(double valor) {
        return String.format(BRASIL, "R$ %.2f", valor);
    }
}
