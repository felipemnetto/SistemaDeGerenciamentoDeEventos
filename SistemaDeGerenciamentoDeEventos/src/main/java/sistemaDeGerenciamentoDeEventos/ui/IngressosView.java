package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import sistemaDeGerenciamentoDeEventos.dominio.Carrinho;
import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.GestaoEventos;
import sistemaDeGerenciamentoDeEventos.dominio.Ingresso;
import sistemaDeGerenciamentoDeEventos.dominio.RegraDeNegocioException;

@Route(value = "ingressos", layout = MainView.class)
@PageTitle("Ingressos")
public class IngressosView extends VerticalLayout {
    // domínio: as duas instâncias únicas
    private final GestaoEventos gestao = GestaoEventos.getInstance();
    private final Carrinho carrinho = Carrinho.getInstance();

    // campos do formulário
    private final ComboBox<Evento> evento = new ComboBox<>("Evento");
    private final ComboBox<String> modalidade = new ComboBox<>("Modalidade");
    private final TextField nome = new TextField("Nome do participante");
    private final TextField cpf = new TextField("CPF do participante");
    private final TextField justificativa = new TextField("Justificativa");

    // ação e texto informativo
    private final Button adicionar = new Button("Adicionar ao carrinho", VaadinIcon.CART.create());
    private final Span info = new Span();

    public IngressosView() {
        configurarCampos();
        registrarOuvintes();

        HorizontalLayout linhaEvento = new HorizontalLayout(evento, modalidade);
        linhaEvento.setAlignItems(FlexComponent.Alignment.END);
        HorizontalLayout linhaParticipante = new HorizontalLayout(nome, cpf, justificativa);
        linhaParticipante.setAlignItems(FlexComponent.Alignment.END);

        add(new H2("Ingressos"), linhaEvento, info, linhaParticipante, adicionar);

        // se o carrinho já tem ingressos, abre com o mesmo evento selecionado
        if (carrinho.getEvento() != null) {
            evento.setValue(carrinho.getEvento());
        }
        atualizarTela(); // por último: os componentes já existem
    }

    private void configurarCampos() {
        evento.setItems(gestao.getEventos());
        evento.setItemLabelGenerator(e -> e.getTitulo() + " - " + Formatos.data(e.getData()));
        evento.setWidth("360px");

        modalidade.setItems("INTEIRA", "MEIA_ENTRADA", "VIP", "PROMOCIONAL");
        modalidade.setItemLabelGenerator(m -> m.replace("_", "-"));

        cpf.setMaxLength(11);

        justificativa.setVisible(false);

        adicionar.addThemeVariants(ButtonVariant.PRIMARY);
        adicionar.setEnabled(false);
    }

    private void registrarOuvintes() {
        evento.addValueChangeListener(e -> atualizarTela());
        modalidade.addValueChangeListener(e -> {
            justificativa.setVisible("MEIA_ENTRADA".equals(e.getValue()));
            justificativa.clear(); // não deixa sobrar texto de uma escolha anterior
            atualizarTela();
        });
        adicionar.addClickListener(e -> adicionar());
    }

    private void atualizarTela() {
        adicionar.setEnabled(evento.getValue() != null && modalidade.getValue() != null);

        Evento e = evento.getValue();
        if (e == null) {
            info.setText("");
        } else {
            info.setText("Vagas: " + e.calcularDisponibilidade() + " de " + e.getCapacidadeMax()
                    + " - Promocionais restantes: " + e.disponibilidadePromocional());
        }
    }

    private void adicionar() {
        try {
            Ingresso ingresso = gestao.novoIngresso(evento.getValue(), modalidade.getValue(),
                    nome.getValue(), cpf.getValue(), justificativa.getValue());
            carrinho.adicionar(ingresso);

            Notification.show("Ingresso adicionado ao carrinho (" + carrinho.tamanho() + ")",
                    2500, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.SUCCESS);

            nome.clear();
            cpf.clear();
            justificativa.clear();
            nome.focus();
            atualizarTela();
        } catch (RegraDeNegocioException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }
}
