package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Venda {

    private int id;
    private String nomeComprador;
    private LocalDateTime dataHora;
    private List<Ingresso> ingressos;
    private Evento evento;
    private BigDecimal total;

    public Venda(int id,String nome,LocalDateTime dataHora,Evento evento){
        this.id = id;
        nomeComprador = nome;
        this.dataHora = dataHora;
        this.evento = evento;
    }

    public int getId() {
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
        return total;
    }

    public boolean addIngresso(Ingresso aux){
        if(evento.calcularDisponibilidade()-ingressos.size()>0){
            total = total.add(aux.calcularPreco());
            ingressos.add(aux);
            return true;
        }
        return false;
    }
    public void RemoveIngresso(Ingresso aux){
        total = total.subtract(aux.calcularPreco());
        ingressos.remove(aux);
    }

    public int QuantIngresso(){
        return ingressos.size();
        /* esse metodo e para facilitar o metodo
            de calcular ocupacao la no evento
        */
    }
    public BigDecimal calcularTotal(){
        return total;
    }

    //se nao for usado pode tirar
    public int QuantIngressoPromo(){
        int contador = 0;
        int a = ingressos.size();
        for(int i = 0; i<a;i++){
            if(ingressos.get(i) instanceof IngressoPromocional){
                contador++;
            }
        }
        return contador;
    }
}
