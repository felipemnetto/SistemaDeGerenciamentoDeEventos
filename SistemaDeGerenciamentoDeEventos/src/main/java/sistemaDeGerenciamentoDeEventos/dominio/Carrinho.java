package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private static Carrinho instancia = null;

    private final List<Ingresso> ingressos = new ArrayList<>();
    private Evento evento;
    private final List<Runnable> ouvintes = new ArrayList<>();

    private Carrinho() {
    }

    public static Carrinho getInstance() {
        if (instancia == null) {
            instancia = new Carrinho();
        }
        return instancia;
    }

    public void adicionar(Ingresso ingresso) {
        if (ingresso == null) {
            throw new RegraDeNegocioException("O ingresso não pode ser nulo.");
        }
        if (evento != null && ingresso.getEvento() != evento) {
            throw new RegraDeNegocioException(
                    "Esvazie o carrinho para comprar ingressos de outro evento.");
        }
        evento = ingresso.getEvento();
        ingressos.add(ingresso);
        notificar();
    }

    public void remover(Ingresso ingresso) {
        ingressos.remove(ingresso);
        if (ingressos.isEmpty()) {
            evento = null;
        }
        notificar();
    }

    public void limpar() {
        ingressos.clear();
        evento = null;
        notificar();
    }

    public List<Ingresso> getIngressos() {
        return List.copyOf(ingressos);
    }

    public Evento getEvento() {
        return evento;
    }

    public int tamanho() {
        return ingressos.size();
    }

    public BigDecimal total() {
        return ingressos.stream()
                .map(Ingresso::calcularPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** A MainView registra aqui o que fazer quando o carrinho muda (atualizar o contador do botão). */
    public void aoMudar(Runnable acao) {
        ouvintes.add(acao);
    }

    private void notificar() {
        for (Runnable acao : ouvintes) {
            acao.run();
        }
    }
}
