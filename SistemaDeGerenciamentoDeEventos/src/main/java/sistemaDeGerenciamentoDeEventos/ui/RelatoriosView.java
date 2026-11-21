package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "relatorios", layout = MainView.class)
@PageTitle("Relatórios")
public class RelatoriosView extends VerticalLayout {

    public RelatoriosView() {
        add(new H2("Relatórios"));
    }
}
