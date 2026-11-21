package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "vendas", layout = MainView.class)
@PageTitle("Vendas")
public class VendasView extends VerticalLayout {

    public VendasView() {
        add(new H2("Vendas"));
    }
}
