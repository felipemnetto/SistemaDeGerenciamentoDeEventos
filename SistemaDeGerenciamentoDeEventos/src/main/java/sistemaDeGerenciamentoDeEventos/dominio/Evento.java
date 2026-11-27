package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Evento{

    private int capacidadeMax, limiteProm, countIngressos, countIngressosProm;
    private String id, titulo, descricao, local;
    private LocalDate data;
    private LocalTime horario_inicio;
    private BigDecimal precoBase, percentDescontoPromocional;
    private List<Venda> vendas;

    public Evento(String id, String titulo, String descricao, String local,
            LocalDate data, LocalTime horario_inicio, int capacidadeMax, BigDecimal precoBase,
            int limiteProm, BigDecimal percentDescontoPromocional){

        if (id == null || id.isBlank() ||
            titulo == null || titulo.isBlank() ||
            descricao == null || descricao.isBlank() ||
            local == null || local.isBlank()) {
            throw new RegraDeNegocioException(
                "Os dados textuais do evento são obrigatórios."
            );
        }
        if (data == null || horario_inicio == null) {
            throw new RegraDeNegocioException(
                "A data e o horário do evento são obrigatórios."
            );
        }
        if (capacidadeMax <= 0 || limiteProm < 0 ||
            limiteProm > capacidadeMax) {
            throw new RegraDeNegocioException(
                "Capacidade ou limite promocional inválido."
            );
        }
        if (precoBase == null || precoBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException(
                "O preço base não pode ser nulo ou negativo."
            );
        }
        if (percentDescontoPromocional == null ||
            percentDescontoPromocional.compareTo(BigDecimal.ZERO) < 0 ||
            percentDescontoPromocional.compareTo(
                new BigDecimal("100")) > 0) {

            throw new RegraDeNegocioException(
                "O desconto deve estar entre 0 e 100%."
            );
        }

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.data = data;
        this.horario_inicio = horario_inicio;
        this.capacidadeMax = capacidadeMax;
        this.precoBase = precoBase;
        this.limiteProm = limiteProm;
        this.percentDescontoPromocional = percentDescontoPromocional;
        this.vendas = new LinkedList<>();
    }

    public boolean addListaVenda(Venda aux){
        if (aux == null) {
            throw new RegraDeNegocioException("A venda não pode ser nula.");
        }
        if (aux.getEvento() != this) {
            throw new RegraDeNegocioException(
                    "A venda pertence a outro evento.");
        }
        if(!vendas.contains(aux)){
            vendas.add(aux);
            countIngressos += aux.quantIngresso();
            countIngressosProm += aux.quantIngressoPromo();
            return true;
        }

        return false;
    }

    public int calcularDisponibilidade(){
        return  capacidadeMax - countIngressos;
    }

    public int disponibilidadePromocional(){
        return limiteProm - countIngressosProm;
    }

    public double percentualOcupacao() {
    return (capacidadeMax - calcularDisponibilidade()) / (double) capacidadeMax;
    }

    public int getLimiteProm() {
        return limiteProm;
    }

    public BigDecimal getPercentDescontoPromocional() {
        return percentDescontoPromocional;
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

    public boolean verificaHoras() {
        return verificaHora(LocalDateTime.now());
    }

    public boolean verificaHora(LocalDateTime momento) {
        return momento.isBefore(LocalDateTime.of(data, horario_inicio));
    }
}
