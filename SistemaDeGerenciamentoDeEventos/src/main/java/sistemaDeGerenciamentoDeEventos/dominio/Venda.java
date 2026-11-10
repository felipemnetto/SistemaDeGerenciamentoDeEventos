package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    public boolean addIngresso(List<Ingresso> novosIngressos){ // Como vai surgir essa lista?
        if(novosIngressos == null){
            return false;
        }
        if(evento.calcularDisponibilidade() < novosIngressos.size()){
            return false;
        }

        Set<String> cpfs = novosIngressos.stream()
            .map(Ingresso::getCpfParticipante)
            .collect(Collectors.toSet());

        if(cpfs.size() != novosIngressos.size()){
            return false;
        }

        boolean possuiEventoDiferente = novosIngressos.stream()
                .anyMatch(ingresso -> ingresso.getEvento() != evento);

        if(possuiEventoDiferente){
            return false;
        }

        int quantidadePromocional = (int) novosIngressos.stream()
            .filter(ingresso -> ingresso instanceof IngressoPromocional)
            .count();

        if (quantidadePromocional > evento.disponibilidadePromocional()) {
            return false;
        }
        BigDecimal totalNovosIngressos = novosIngressos.stream()
                    .map(Ingresso::calcularPreco)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        ingressos.addAll(novosIngressos);
        valorTotal = valorTotal.add(totalNovosIngressos);
        return true;
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
