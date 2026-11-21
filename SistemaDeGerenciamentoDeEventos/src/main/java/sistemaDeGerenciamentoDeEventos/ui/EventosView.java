package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
@PageTitle("Eventos")
public class EventosView extends VerticalLayout {

    public EventosView() {
        add(new H2("Eventos"));
    }
}
