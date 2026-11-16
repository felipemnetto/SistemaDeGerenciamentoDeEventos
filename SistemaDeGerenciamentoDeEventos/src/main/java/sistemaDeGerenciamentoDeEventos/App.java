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

//@SpringBootApplication
public class App {

    public static void main(String[] args) {
        //SpringApplication.run(App.class, args);
        Evento evento1 = new Evento(
            "EVT001",
            "Show de Rock",
            "Show de bandas nacionais.",
            "Arena de Shows",
            LocalDate.of(2027, 3, 15),
            LocalTime.of(20, 0),
            500,
            new BigDecimal("100.00"),
            100,
            new BigDecimal("20.00")
        );

        Venda venda = new Venda(
            "VND001",
            "João Silva",
            LocalDateTime.now(),
            evento1
        );

        Ingresso ingressoInteira = new IngressoInteira(
            "João Silva", "11111111111", "ING001", "VND001", evento1
        );
        Ingresso ingressoMeia = new IngressoMeiaEntrada(
            "Maria Oliveira", "22222222222", "ING002", "VND001",
            "Estudante", evento1
        );
        Ingresso ingressoPromocional = new IngressoPromocional(
            "Pedro Santos", "33333333333", "ING003", "VND001", evento1
        );

        venda.addIngresso(List.of(ingressoInteira, ingressoMeia, ingressoPromocional));

        verificar("Preço do ingresso inteira", new BigDecimal("100.00"),
            ingressoInteira.calcularPreco());
        verificar("Preço do ingresso meia-entrada", new BigDecimal("50.00"),
            ingressoMeia.calcularPreco());
        verificar("Preço do ingresso promocional", new BigDecimal("80.00"),
            ingressoPromocional.calcularPreco());
        verificar("Total da venda", new BigDecimal("230.00"),
            venda.getValorTotal());
        verificar("Quantidade de ingressos", 3, venda.getIngressos().size());

        System.out.println("Teste hard coded do domínio passou.");
        System.out.println("Evento: " + evento1.getTitulo());
        System.out.println("Disponibilidade: " + evento1.calcularDisponibilidade());
        System.out.println("Comprador: " + venda.getNomeComprador());
        System.out.println("Total da venda: " + venda.getValorTotal());
    }

    private static void verificar(String descricao, Object esperado, Object atual) {
        boolean valoresIguais = esperado instanceof BigDecimal && atual instanceof BigDecimal
            ? ((BigDecimal) esperado).compareTo((BigDecimal) atual) == 0
            : esperado.equals(atual);
        if (!valoresIguais) {
            throw new IllegalStateException(
                descricao + " - esperado: " + esperado + ", atual: " + atual
            );
        }
    }
}
