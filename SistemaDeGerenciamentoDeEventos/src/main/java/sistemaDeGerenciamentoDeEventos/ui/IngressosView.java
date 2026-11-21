package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "ingressos", layout = MainView.class)
@PageTitle("Ingressos")
public class IngressosView extends VerticalLayout {

    public IngressosView() {
        add(new H2("Ingressos"));
    }
}
