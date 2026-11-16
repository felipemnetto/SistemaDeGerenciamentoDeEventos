package sistemaDeGerenciamentoDeEventos.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestaoEventos {
    private final List<Evento> eventos = new ArrayList<>();

    public void cadastrarEvento(Evento evento) {
        boolean idRepetido = eventos.stream().anyMatch(e -> e.getId().equals(evento.getId()));
        if (idRepetido) {
            throw new RegraDeNegocioException("Já existe um evento com o id " + evento.getId());
        }
        eventos.add(evento);
    }

    public List<Evento> getEventos() {
        return List.copyOf(eventos);
    }

    /** Registra a venda inteira ou nada (a validação está em Venda.addIngresso). */
    public Venda registrarVenda(String id, Evento evento, String comprador,
                                LocalDateTime dataHora, List<Ingresso> ingressos) {
        Venda venda = new Venda(id, comprador, dataHora, evento);
        venda.addIngresso(ingressos);
        return venda;
    }
}
