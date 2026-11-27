package sistemaDeGerenciamentoDeEventos.dominio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    // usado pela carga dos CSV: só repassa os campos da linha
    private Ingresso criarIngresso(IngressoCsv i, Evento evento) {
        return criarIngresso(i.id(), i.vendaId(), evento, i.modalidade(),
                i.participanteNome(), i.participanteCpf(), i.justificativa());
    }

    // o único switch que escolhe a subclasse de Ingresso
    private Ingresso criarIngresso(String id, String idVenda, Evento evento, String modalidade,
                                   String nome, String cpf, String justificativa) {
        return switch (modalidade) {
            case "INTEIRA" -> new IngressoInteira(nome, cpf, id, idVenda, evento);
            case "MEIA_ENTRADA" -> new IngressoMeiaEntrada(nome, cpf, id, idVenda, justificativa, evento);
            case "VIP" -> new IngressoVip(nome, cpf, id, idVenda, evento);
            case "PROMOCIONAL" -> new IngressoPromocional(nome, cpf, id, idVenda, evento);
            default -> throw new IllegalArgumentException("Modalidade desconhecida: " + modalidade);
        };
    }

    //metodo que filtra os eventos para mostrar na aba de eventos
    public List<Evento> filtrarEventos(String situacao, LocalDate de, LocalDate ate, String local) {
    return eventos.stream()
            .filter(e -> situacao == null || situacao.equals("Todos")
                      || (situacao.equals("Futuros") == e.verificaHoras()))
            .filter(e -> de == null || !e.getData().isBefore(de))
            .filter(e -> ate == null || !e.getData().isAfter(ate))
            .filter(e -> local == null || local.isBlank()
                      || e.getLocal().toLowerCase().contains(local.trim().toLowerCase()))
            .sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getHoraInicio))
            .toList();
    }

    private int proximoIngresso = 1;


    public Ingresso novoIngresso(Evento evento, String modalidade, String nome, String cpf, String justificativa) {
        if (evento == null) {
            throw new RegraDeNegocioException("Selecione o evento.");
        }
        if (modalidade == null) {
            throw new RegraDeNegocioException("Escolha a modalidade.");
        }
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Informe o nome do participante.");
        }
        if (cpf == null || !cpf.trim().matches("\\d{11}")) {
            throw new RegraDeNegocioException("O CPF deve ter exatamente 11 dígitos.");
        }
        if (modalidade.equals("MEIA_ENTRADA") && (justificativa == null || justificativa.isBlank())) {
            throw new RegraDeNegocioException("Informe a justificativa da meia-entrada.");
        }
        return criarIngresso("ING-" + proximoIngresso++, null, evento, modalidade, nome.trim(), cpf.trim(), justificativa);
    }

    public Evento novoEvento(String titulo, String descricao, String local,
            LocalDate data, LocalTime horario_inicio, Integer capacidadeMax, BigDecimal precoBase,
            Integer limiteProm, BigDecimal percentDescontoPromocional){

                if(titulo == null || descricao == null || local == null || data == null
                    || horario_inicio == null || capacidadeMax == null || precoBase == null || limiteProm == null
                    || percentDescontoPromocional == null){
                        throw new RegraDeNegocioException("É preciso preencher todos os dados para cadastrar um evento.");
                    }


                //formata só pra manter o padrao que vem no csv, tipo E001, E002
                String id = proximoIdEvento();

                Evento novo = new Evento(id,titulo.trim(),descricao.trim(),local.trim(),data,horario_inicio,capacidadeMax,precoBase,limiteProm,percentDescontoPromocional);
                eventos.add(novo);
                return novo;
            }

    public String proximoIdEvento() {
        return String.format("E%03d", eventos.size() + 1);
    }

    private int proximaVenda = 1;

    // usado pelo "Pagar" do carrinho. Tudo ou nada: se Venda.addIngresso recusar, nada é registrado.
    // Quem esvazia o carrinho é a tela, só depois que este método voltar sem erro.
    public Venda registrarVenda(Evento evento, String comprador, List<Ingresso> ingressos) {
        if (evento == null) {
            throw new RegraDeNegocioException("Selecione o evento.");
        }
        if (comprador == null || comprador.isBlank()) {
            throw new RegraDeNegocioException("Informe o nome do comprador.");
        }
        Venda venda = new Venda("VEND-" + proximaVenda, comprador.trim(), LocalDateTime.now(), evento);
        venda.addIngresso(ingressos);
        proximaVenda++; // só avança se a venda foi aceita
        return venda;
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
