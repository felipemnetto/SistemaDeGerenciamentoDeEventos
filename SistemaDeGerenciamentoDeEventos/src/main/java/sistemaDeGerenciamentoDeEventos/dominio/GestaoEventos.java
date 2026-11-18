package sistemaDeGerenciamentoDeEventos.dominio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.EventoCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.IngressoCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.ModalidadeEventoCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.VendaCsv;

public class GestaoEventos {
    private static GestaoEventos instancia = null;

    private List<Evento> eventos;

    public GestaoEventos() {
        eventos = new ArrayList<>();
        try {
            carregarEventos();
            carregarVendas();
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler os CSV da pasta Dados", e);
        }
    }

    // eventos.csv + modalidades_evento.csv (ligados pelo id do evento)
    private void carregarEventos() throws IOException {
        List<ModalidadeEventoCsv> modalidades =
                LeitorDadosCsv.lerModalidades(Path.of("Dados/modalidades_evento.csv"));

        for (EventoCsv e : LeitorDadosCsv.lerEventos(Path.of("Dados/eventos.csv"))) {
            ModalidadeEventoCsv m = modalidades.stream()
                    .filter(x -> x.eventoId().equals(e.id()))
                    .findFirst().orElseThrow();
            eventos.add(new Evento(e.id(), e.titulo(), e.descricao(), e.local(),
                    e.data(), e.horarioInicio(), e.capacidade(), e.precoBase(),
                    m.limitePromocional(), BigDecimal.valueOf(m.descontoPromocionalPercentual())));
        }
    }

    // vendas.csv + ingressos.csv (ligados pelo id da venda); cada venda passa pelas regras do domínio
    private void carregarVendas() throws IOException {
        List<IngressoCsv> ingressos = LeitorDadosCsv.lerIngressos(Path.of("Dados/ingressos.csv"));

        for (VendaCsv v : LeitorDadosCsv.lerVendas(Path.of("Dados/vendas.csv"))) {
            Evento evento = eventos.stream()
                    .filter(e -> e.getId().equals(v.eventoId()))
                    .findFirst().orElseThrow();
            List<Ingresso> doVenda = ingressos.stream()
                    .filter(i -> i.vendaId().equals(v.id()))
                    .map(i -> criarIngresso(i, evento))
                    .toList();

            Venda venda = new Venda(v.id(), v.comprador(), v.dataHora(), evento);
            venda.addIngresso(doVenda);
        }
    }

    private Ingresso criarIngresso(IngressoCsv i, Evento evento) {
        String nome = i.participanteNome();
        String cpf = i.participanteCpf();
        return switch (i.modalidade()) {
            case "INTEIRA" -> new IngressoInteira(nome, cpf, i.id(), i.vendaId(), evento);
            case "MEIA_ENTRADA" -> new IngressoMeiaEntrada(nome, cpf, i.id(), i.vendaId(), i.justificativa(), evento);
            case "VIP" -> new IngressoVip(nome, cpf, i.id(), i.vendaId(), evento);
            case "PROMOCIONAL" -> new IngressoPromocional(nome, cpf, i.id(), i.vendaId(), evento);
            default -> throw new IllegalArgumentException("Modalidade desconhecida: " + i.modalidade());
        };
    }

    public List<Evento> getEventos() {
        return List.copyOf(eventos);
    }

    public static GestaoEventos getInstance() {
        if (instancia == null) {
            instancia = new GestaoEventos();
        }
        return instancia;
    }
}
