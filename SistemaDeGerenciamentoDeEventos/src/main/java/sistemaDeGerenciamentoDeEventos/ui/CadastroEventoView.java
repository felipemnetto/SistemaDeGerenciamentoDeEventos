package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "novo-evento", layout = MainView.class)
@PageTitle("Novo evento")
public class CadastroEventoView extends VerticalLayout {

    public CadastroEventoView() {
        add(new H2("Novo evento"));
    }
}
