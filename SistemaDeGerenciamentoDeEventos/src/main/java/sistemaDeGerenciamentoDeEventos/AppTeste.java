package sistemaDeGerenciamentoDeEventos;

import java.math.BigDecimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.Venda;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoInteira;

/*
   é temporario so para testar uns trecos
   tem que apagar antes de enviar
*/
public class AppTeste {

   public static void main(String[] args) {
      SpringApplication.run(AppTeste.class, args);
      BigDecimal preco = new BigDecimal(45);
      Evento teste = new Evento(2002, "amogus", "xique", 10000, preco, 300); // o preço tem que ser BigDecimal
      Venda a1 = new Venda(1,"nome",22,teste); // o dataHora tem que ser LocalDateTime
      IngressoInteira b1 = new IngressoInteira("nome","299.112.231-23",288,teste);
      a1.addIngresso(b1);
      teste.addListaVenda(a1);
      System.out.println(teste.getVendas());

   }

}
