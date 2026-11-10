package sistemaDeGerenciamentoDeEventos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.Venda;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoInteira;

/*
   é temporario so para testar uns trecos
   tem que apagar antes de enviar
*/
public class AppTeste {

   public static void main(String[] args) {
      Evento teste = new Evento(2002, "amogus", "xique", 10000, BigDecimal.valueOf(45), 300); // o preço tem que ser BigDecimal
      Venda a1 = new Venda(1,"nome",LocalDateTime.of(2026,11,10,22,00,00),teste); // o dataHora tem que ser LocalDateTime
      IngressoInteira b1 = new IngressoInteira("nome","299.112.231-23",288,teste);
      a1.addIngresso(b1);
      teste.addListaVenda(a1);
      List<Vendas> x = teste.getVendas();
      System.out.println(x.toString());
   }

}
