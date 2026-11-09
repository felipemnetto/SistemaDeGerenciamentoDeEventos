package sistemaDeGerenciamentoDeEventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication

/*
   é temporario so para testar uns trecos
   tem que apagar antes de enviar
*/
public class AppTeste {

   public static void main(String[] args) {
      SpringApplication.run(AppTeste.class, args);
      Evento teste =Evento(2002, "amogus", "xique", 10000,45 , 300);
      Venda a1 = Venda(1,"nome",22,teste);
      IngressoInteira b1 = IngressoInteira("nome","299.112.231-23",288,teste);
      a1.addIngresso(b1);
      teste.addListaVenda(a1);
      System.out.println(teste.getVendidosTotal());

   }

}
