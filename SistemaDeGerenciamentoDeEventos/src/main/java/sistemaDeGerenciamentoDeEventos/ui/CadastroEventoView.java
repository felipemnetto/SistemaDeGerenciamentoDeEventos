package sistemaDeGerenciamentoDeEventos.ui;

import java.util.Locale;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.GestaoEventos;
import sistemaDeGerenciamentoDeEventos.dominio.RegraDeNegocioException;

@Route(value = "novo-evento", layout = MainView.class)
@PageTitle("Novo evento")
public class CadastroEventoView extends VerticalLayout {
    private final GestaoEventos gestao = GestaoEventos.getInstance();

    //componentes

    private final TextField id = new TextField("Identificador");
    private final TextField titulo = new TextField("Título");
    private final TextField descricao = new TextField("Descrição");
    private final TextField local = new TextField("Local");
    private final DatePicker data = new DatePicker("Data");
    private final TimePicker horario_inicio = new TimePicker("Horário de início");
    private final IntegerField capacidadeMax = new IntegerField("Capacidade máxima");
    private final BigDecimalField precoBase = new BigDecimalField("Preço base");
    private final IntegerField limiteProm = new IntegerField("Limite de ingressos promocionais");
    private final BigDecimalField percentDescontoPromocional = new BigDecimalField("Desconto para ingressos promocionais (0-100)");

    private final Button salvar = new Button("Salvar evento", VaadinIcon.CHECK.create());

    public CadastroEventoView() {
        configuraFields();

        FormLayout formulario = new FormLayout(id, titulo, descricao, local, data, horario_inicio,
                capacidadeMax, precoBase, limiteProm, percentDescontoPromocional);
        formulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));

        add(new H2("Novo evento"), formulario, salvar);
    }

    private void configuraFields(){
        id.setReadOnly(true);
        id.setValue(gestao.proximoIdEvento());

        horario_inicio.setLocale(Locale.of("pt", "BR"));

        capacidadeMax.setMin(1);
        limiteProm.setMin(0);
        percentDescontoPromocional.setHelperText("De 0 a 100. Ex.: 20 para 20%");

        salvar.addThemeVariants(ButtonVariant.PRIMARY);
        salvar.addClickListener(e -> salvar());
    }

    private void salvar(){
        try {
            Evento novo = gestao.novoEvento(titulo.getValue(), descricao.getValue(), local.getValue(),
                    data.getValue(), horario_inicio.getValue(), capacidadeMax.getValue(),
                    precoBase.getValue(), limiteProm.getValue(), percentDescontoPromocional.getValue());

            Notification.show("Evento " + novo.getId() + " cadastrado.", 4000,
                    Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.SUCCESS);
            limpar();
        } catch (RegraDeNegocioException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }

    private void limpar(){
        titulo.clear();
        descricao.clear();
        local.clear();
        data.clear();
        horario_inicio.clear();
        capacidadeMax.clear();
        precoBase.clear();
        limiteProm.clear();
        percentDescontoPromocional.clear();
        // o id que acabou de ser usado sai e mostra o próximo
        id.setValue(gestao.proximoIdEvento());
        titulo.focus();
    }
}
