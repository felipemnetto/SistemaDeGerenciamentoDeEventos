package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Venda {

    private String id, nomeComprador;
    private LocalDateTime dataHora;
    private List<Ingresso> ingressos;
    private Evento evento;
    private BigDecimal valorTotal;

    public Venda(String id,String nome,LocalDateTime dataHora,Evento evento){
        this.id = id;
        this.nomeComprador = nome;
        this.dataHora = dataHora;
        this.evento = evento;
        this.ingressos = new LinkedList<>();
        this.valorTotal = BigDecimal.valueOf(0);
    }

    public String getId() {
        return id;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<Ingresso> getIngressos() {
        return List.copyOf(ingressos);
    }

    public Evento getEvento() {
        return evento;
    }

    public BigDecimal getTotal() {
        return valorTotal;
    }

    public void addIngresso(List<Ingresso> novosIngressos){
        if(novosIngressos == null || novosIngressos.size() == 0){
            throw new RegraDeNegocioException("A venda deve possuir pelo menos um ingresso.");
        }

        if (!evento.aceitaVendas()) {
            throw new RegraDeNegocioException("Não é possível vender ingressos para eventos passados.");
        }

        if(evento.calcularDisponibilidade() < novosIngressos.size()){
            throw new RegraDeNegocioException("Limite de ingressos excedido.");
        }

        Set<String> cpfs = novosIngressos.stream()
            .map(ingresso -> ingresso.getCpfParticipante())
            .collect(Collectors.toSet());


        if(cpfs.size() != novosIngressos.size()){
            throw new RegraDeNegocioException("Não é possivel comprar ingressos que utilizem o mesmo CPF.");
        }

        boolean possuiEventoDiferente = novosIngressos.stream()
                .anyMatch(ingresso -> ingresso.getEvento() != evento);

        if(possuiEventoDiferente){
            throw new RegraDeNegocioException("Não é possível comprar ingressos para eventos diferentes.");
        }

        int quantidadePromocional = (int) novosIngressos.stream()
            .filter(ingresso -> ingresso instanceof IngressoPromocional)
            .count();

        if (quantidadePromocional > evento.disponibilidadePromocional()) {
            throw new RegraDeNegocioException("O limite de ingressos promocionais para este evento foi excedido.");
        }
        BigDecimal totalNovosIngressos = novosIngressos.stream()
                    .map(ingresso -> ingresso.calcularPreco())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!evento.addListaVenda(this)) {
            throw new RegraDeNegocioException("Venda já registrada.");
        }

        ingressos.addAll(novosIngressos);
        valorTotal = valorTotal.add(totalNovosIngressos);
        evento.addListaVenda(this);
    }

    public int quantIngresso(){
        return ingressos.size();
        /* esse metodo e para facilitar o metodo
            de calcular ocupacao la no evento
        */
    }
    public BigDecimal calcularTotal(){
        return valorTotal;
    }

    //se nao for usado pode tirar
    public int quantIngressoPromo(){
        return (int) ingressos.stream()
            .filter(ingresso -> ingresso instanceof IngressoPromocional)
            .count();
    }
}
