package sistemaDeGerenciamentoDeEventos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.Ingresso;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoInteira;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoMeiaEntrada;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoPromocional;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoVip;
import sistemaDeGerenciamentoDeEventos.dominio.Venda;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
