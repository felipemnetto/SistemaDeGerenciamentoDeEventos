package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

import sistemaDeGerenciamentoDeEventos.dominio.Carrinho;

/**
 * A "moldura" comum a todas as telas: barra superior e menu lateral.
 * Não tem @Route: cada tela diz, no seu @Route, que quer ser exibida
 * dentro desta moldura (layout = MainView.class).
 */
public class MainView extends AppLayout {

    public MainView() {
        H1 titulo = new H1("Gestão de Eventos");
        titulo.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        //Criacao do carrinho
        CarrinhoView carrinho = new CarrinhoView();
        Button botaoCarrinho = new Button("Carrinho (0)", VaadinIcon.CART.create(), e -> carrinho.open());
        botaoCarrinho.getStyle().set("margin-left", "auto");
        botaoCarrinho.getStyle().set("margin-right", "var(--lumo-space-m)");
        Carrinho.getInstance().aoMudar(() -> botaoCarrinho.setText("Carrinho ("+ Carrinho.getInstance().tamanho() + ")"));

        SideNav menu = new SideNav();
        menu.addItem(new SideNavItem("Eventos", EventosView.class, VaadinIcon.CALENDAR.create()));
        menu.addItem(new SideNavItem("Novo evento", CadastroEventoView.class, VaadinIcon.PLUS.create()));
        menu.addItem(new SideNavItem("Ingressos", IngressosView.class, VaadinIcon.TICKET.create()));
        menu.addItem(new SideNavItem("Vendas", VendasView.class, VaadinIcon.MONEY.create()));
        menu.addItem(new SideNavItem("Relatórios", RelatoriosView.class, VaadinIcon.CHART.create()));

        addToNavbar(new DrawerToggle(), titulo, botaoCarrinho);
        addToDrawer(menu);
    }
}
