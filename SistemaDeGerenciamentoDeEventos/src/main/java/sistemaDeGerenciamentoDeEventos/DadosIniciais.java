package sistemaDeGerenciamentoDeEventos;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.EventoCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.IngressoCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.ModalidadeEventoCsv;
import sistemaDeGerenciamentoDeEventos.LeitorDadosCsv.VendaCsv;
import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.GestaoEventos;
import sistemaDeGerenciamentoDeEventos.dominio.Ingresso;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoInteira;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoMeiaEntrada;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoPromocional;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoVip;

/**
 * Monta a GestaoEventos com os dados dos arquivos CSV (a pasta "Dados").
 * Mesmo papel da DadosIniciais do t07, mas lendo arquivos em vez de
 * cadastrar objetos "na mão". As vendas passam pelas regras do domínio.
 */
public final class DadosIniciais {
    private static final Path PASTA = Path.of("Dados");

    private DadosIniciais() {
    }

    public static GestaoEventos criarGestao() {
        try {
            GestaoEventos g = new GestaoEventos();

            List<ModalidadeEventoCsv> modalidades =
                    LeitorDadosCsv.lerModalidades(PASTA.resolve("modalidades_evento.csv"));
            List<IngressoCsv> ingressos = LeitorDadosCsv.lerIngressos(PASTA.resolve("ingressos.csv"));

            // Eventos: cada linha de eventos.csv + a linha de modalidades com o mesmo id
            for (EventoCsv e : LeitorDadosCsv.lerEventos(PASTA.resolve("eventos.csv"))) {
                ModalidadeEventoCsv m = modalidades.stream()
                        .filter(x -> x.eventoId().equals(e.id()))
                        .findFirst().orElseThrow();
                g.cadastrarEvento(new Evento(e.id(), e.titulo(), e.descricao(), e.local(),
                        e.data(), e.horarioInicio(), e.capacidade(), e.precoBase(),
                        m.limitePromocional(), BigDecimal.valueOf(m.descontoPromocionalPercentual())));
            }

            // Vendas: cada venda leva os ingressos que têm o seu id
            for (VendaCsv v : LeitorDadosCsv.lerVendas(PASTA.resolve("vendas.csv"))) {
                Evento evento = evento(g, v.eventoId());
                List<Ingresso> doVenda = ingressos.stream()
                        .filter(i -> i.vendaId().equals(v.id()))
                        .map(i -> ingresso(i, evento))
                        .toList();
                g.registrarVenda(v.id(), evento, v.comprador(), v.dataHora(), doVenda);
            }
            return g;
        } catch (IOException ex) {
            throw new UncheckedIOException("Falha ao ler os CSV em " + PASTA.toAbsolutePath(), ex);
        }
    }

    private static Evento evento(GestaoEventos g, String id) {
        return g.getEventos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    /** Escolhe a subclasse de Ingresso pela modalidade escrita no CSV. */
    private static Ingresso ingresso(IngressoCsv i, Evento evento) {
        String nome = i.participanteNome();
        String cpf = i.participanteCpf();
        return switch (i.modalidade()) {
            case "INTEIRA" -> new IngressoInteira(nome, cpf, i.id(), i.vendaId(), evento);
            case "MEIA_ENTRADA" -> new IngressoMeiaEntrada(nome, cpf, i.id(), i.vendaId(),
                    i.justificativa(), evento);
            case "VIP" -> new IngressoVip(nome, cpf, i.id(), i.vendaId(), evento);
            case "PROMOCIONAL" -> new IngressoPromocional(nome, cpf, i.id(), i.vendaId(), evento);
            default -> throw new IllegalArgumentException("Modalidade desconhecida: " + i.modalidade());
        };
    }
}
