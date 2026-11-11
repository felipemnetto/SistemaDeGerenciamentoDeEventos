package sistemaDeGerenciamentoDeEventos.ui;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.Sistema;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        // tema escuro
        getElement().setAttribute("theme", "dark");

        // configurações gerais da tela
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#0e0d0d");
        getStyle().set("padding", "20px");

        // quadrado do meio
        VerticalLayout cardPrincipal = new VerticalLayout();
        cardPrincipal.setWidth("520px");
        cardPrincipal.getStyle().set("background-color", "#1e1e1e");
        cardPrincipal.getStyle().set("padding", "30px");
        cardPrincipal.getStyle().set("border-radius", "16px");
        cardPrincipal.getStyle().set("box-shadow", "0 8px 24px rgba(0,0,0,0.5)");
        cardPrincipal.setAlignItems(Alignment.STRETCH);

        H2 titulo = new H2("Compra de Ingresso");
        titulo.getStyle().set("color", "#ffffff");
        titulo.getStyle().set("margin-top", "0");
        titulo.getStyle().set("text-align", "center");

        cardPrincipal.add(titulo);
        add(cardPrincipal);

        //List<Evento> eventos = Sistema.getInstance().getEventos();
    }
}
