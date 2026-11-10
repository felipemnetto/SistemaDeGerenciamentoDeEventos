package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Evento{

    private int capacidadeMax, limiteProm;
    private String id, titulo, descricao, local;
    private LocalDate data;
    private LocalTime horario_inicio;
    private BigDecimal precoBase, descontoPromocional;
    private List<Venda> vendas;

    public Evento(String id, String titulo, String Descricao, String local,
            LocalDate data, LocalTime horario_inicio, int capacidadeMax, BigDecimal precoBase){

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.data = data;
        this.horario_inicio = horario_inicio;
        this.capacidadeMax = capacidadeMax;
        this.precoBase = precoBase;
        this.limiteProm = (int) (capacidadeMax * 0.2);
        this.vendas = new LinkedList<>();
    }

    public boolean addListaVenda(Venda aux){
        if(!vendas.contains(aux)){
            vendas.add(aux);
            return true;
        }
        return false;
    }

    public int calcularDisponibilidade(){
        int qtdIngressos = (int) vendas.stream()
                                       .flatMap(venda -> venda.getIngressos().stream())
                                       .count();

        return  capacidadeMax - qtdIngressos;
    }

    public int disponibilidadePromocional(){
        int qtdProm = (int) vendas.stream()
                             .flatMap(venda -> venda.getIngressos().stream())
                             .filter(i -> i instanceof IngressoPromocional)
                             .count();

        return limiteProm - qtdProm;
    }

    public BigDecimal getPrecoBase(){
        return precoBase;
    }

    public String getId() {
        return id;
    }

    public int getCapacidadeMax() {
        return capacidadeMax;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocal() {
        return local;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHoraInicio() {
        return horario_inicio;
    }

    public List<Venda> getVendas() {
        return List.copyOf(vendas);
    }
}
