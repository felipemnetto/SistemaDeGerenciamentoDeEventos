package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public void addIngresso(Ingresso aux){
        total = total.add(aux.calcularPreco());
        ingressos.add(aux);
    }
    public void RemoveIngresso(Ingresso aux){
        total = total.subtract(aux.calcularPreco());
        ingressos.revome(aux);
    }

    public int QuantIngresso(){
        return ingressos.size();
        /* esse metodo e para facilitar o metodo
            de calcular ocupacao la no evento
        */
    }
    public BigDecimal calcularTotal(){

        return Total;

    }
}
