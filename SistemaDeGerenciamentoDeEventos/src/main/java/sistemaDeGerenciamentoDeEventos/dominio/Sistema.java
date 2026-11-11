package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Sistema {
    private static Sistema sistema = null;

    private List<Evento> eventos;

    public Sistema(){
        eventos = new LinkedList<>();
        // Carrega os eventos
        eventos.add(new Evento(
                "E001",
                "Festival de Jazz",
                "Apresentações musicais",
                "Auditório Central",
                LocalDate.of(2026, 11, 14),
                LocalTime.of(19, 0),
                300, BigDecimal.valueOf(250),
                120,
                BigDecimal.valueOf(100)));
    }

    public static Sistema getInstance(){
        if (sistema == null){
            sistema = new Sistema();
            return sistema;
        }else{
            return sistema;
        }
    }

    public List<Evento> getEventos(){
        return eventos;
    }

}
