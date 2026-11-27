package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.GestaoEventos;

@Route(value = "", layout = MainView.class)
@PageTitle("Eventos")
public class EventosView extends VerticalLayout {
    private final GestaoEventos gestao = GestaoEventos.getInstance();

    // Componentes da tela
    private final ComboBox<String> situacao = new ComboBox<>("Situação");
    private final DatePicker de = new DatePicker("De");
    private final DatePicker ate = new DatePicker("Até");
    private final TextField local = new TextField("Local");
    private final Grid<Evento> grid = new Grid<>();
    private final VerticalLayout painel = new VerticalLayout();

    public EventosView() {
        configurarFiltros();
        configurarGrid();
        configurarPainel();

        HorizontalLayout filtros = new HorizontalLayout(situacao, de, ate, local);
        HorizontalLayout conteudo = new HorizontalLayout(grid, painel);
        conteudo.setWidthFull();
        conteudo.setFlexGrow(1, grid);

        add(new H2("Eventos"), filtros, conteudo);

        atualizar();
    }

    //filtros dos eventos da tabela
    private void configurarFiltros() {
        situacao.setItems("Todos", "Futuros", "Realizados");
        situacao.setValue("Todos");

        local.setValueChangeMode(ValueChangeMode.EAGER); // filtra enquanto digita
        local.setClearButtonVisible(true);

        // mantem a tabela atualizada
        situacao.addValueChangeListener(e -> atualizar());
        de.addValueChangeListener(e -> atualizar());
        ate.addValueChangeListener(e -> atualizar());
        local.addValueChangeListener(e -> atualizar());
    }

    private void atualizar() {
        grid.setItems(gestao.filtrarEventos(
                situacao.getValue(), de.getValue(), ate.getValue(), local.getValue()));
    }

    // tabela de eventos

    private void configurarGrid() {
        grid.addColumn(Evento::getTitulo).setHeader("Evento").setAutoWidth(true);
        grid.addColumn(e -> Formatos.data(e.getData())).setHeader("Data").setAutoWidth(true);
        grid.addColumn(Evento::getLocal).setHeader("Local").setAutoWidth(true);
        grid.addColumn(Evento::getCapacidadeMax).setHeader("Capacidade");
        grid.addColumn(Evento::calcularDisponibilidade).setHeader("Vagas");

        // ocupacao do evento, barra de progresso e porcentagem
        grid.addComponentColumn(e -> {
            ProgressBar barra = new ProgressBar(); // vai de 0 a 1
            barra.setValue(e.percentualOcupacao());
            barra.setWidth("100px");
            Span texto = new Span(Math.round(e.percentualOcupacao() * 100) + "%");
            return new HorizontalLayout(barra, texto);
        }).setHeader("Ocupação").setAutoWidth(true);

        // Situação
        grid.addComponentColumn(e -> {
            boolean futuro = e.verificaHoras();
            Span etiqueta = new Span(futuro ? "Futuro" : "Realizado");
            etiqueta.getElement().getThemeList().add(futuro ? "badge success" : "badge contrast");
            return etiqueta;
        }).setHeader("Situação");

        // Clicar numa linha mostra os detalhes (getValue() é null quando desmarca)
        grid.asSingleSelect().addValueChangeListener(e -> mostrarDetalhes(e.getValue()));
    }

    // detalhes laterais

    private void configurarPainel() {
        painel.setWidth("320px");
        painel.setPadding(true);
        painel.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        mostrarDetalhes(null);
    }

    private void mostrarDetalhes(Evento e) {
        painel.removeAll();
        if (e == null) {
            painel.add(new Span("Selecione um evento para ver os detalhes."));
            return;
        }
        painel.add(
                new H3(e.getTitulo()),
                new Span(e.getLocal() + " - " + Formatos.data(e.getData()) + " às " + e.getHoraInicio()),
                new Span("Vagas: " + e.calcularDisponibilidade() + " de " + e.getCapacidadeMax()),
                new Span("Promocionais restantes: " + e.disponibilidadePromocional()
                        + " (limite " + e.getLimiteProm()
                        + ", desconto " + e.getPercentDescontoPromocional() + "%)"),
                new Span("Preço base: " + Formatos.dinheiro(e.getPrecoBase())));
    }
}
