package sistemaDeGerenciamentoDeEventos.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import sistemaDeGerenciamentoDeEventos.dominio.Carrinho;
import sistemaDeGerenciamentoDeEventos.dominio.GestaoEventos;
import sistemaDeGerenciamentoDeEventos.dominio.Ingresso;
import sistemaDeGerenciamentoDeEventos.dominio.RegraDeNegocioException;
import sistemaDeGerenciamentoDeEventos.dominio.Venda;

/**
 * O carrinho como janela (Dialog): não é uma tela com rota, a MainView a abre por um botão.
 * A tela só mostra o Carrinho e chama a GestaoEventos; as regras ficam no domínio.
 */
public class CarrinhoView extends Dialog {
    private final Carrinho carrinho = Carrinho.getInstance();
    private final GestaoEventos gestao = GestaoEventos.getInstance();

    private final Grid<Ingresso> grid = new Grid<>();
    private final TextField comprador = new TextField("Nome do comprador");
    private final Span total = new Span();
    private final Button pagar = new Button("Pagar", VaadinIcon.CHECK.create());
    private final Button esvaziar = new Button("Esvaziar carrinho");
    private final Button fechar = new Button("Fechar");

    public CarrinhoView() {
        setHeaderTitle("Carrinho");
        setWidth("700px");

        configurarGrid();

        comprador.setWidthFull();
        // habilita/desabilita o Pagar conforme o nome é digitado
        comprador.addValueChangeListener(e -> atualizarBotoes());

        pagar.addThemeVariants(ButtonVariant.PRIMARY);
        pagar.addClickListener(e -> pagar());
        esvaziar.addClickListener(e -> {
            carrinho.limpar();
            atualizar();
        });
        fechar.addClickListener(e -> close());

        VerticalLayout conteudo = new VerticalLayout(grid, comprador, total);
        conteudo.setPadding(false);
        add(conteudo);
        getFooter().add(fechar, esvaziar, pagar);
    }

    private void configurarGrid() {
        grid.addColumn(i -> i.getClass().getSimpleName().replace("Ingresso", ""))
                .setHeader("Modalidade").setAutoWidth(true);
        grid.addColumn(i -> i.getNomeParticipante()).setHeader("Participante").setAutoWidth(true);
        grid.addColumn(i -> i.getCpfParticipante()).setHeader("CPF").setAutoWidth(true);
        grid.addColumn(i -> Formatos.dinheiro(i.calcularPreco())).setHeader("Preço");
        grid.addComponentColumn(ingresso -> {
            Button remover = new Button(VaadinIcon.TRASH.create(), e -> {
                carrinho.remover(ingresso);
                atualizar();
            });
            remover.addThemeVariants(ButtonVariant.TERTIARY);
            return remover;
        }).setHeader("Remover");
        grid.setHeight("260px");
    }

    /** Sempre que a janela abre, mostra o estado atual do carrinho. */
    @Override
    public void open() {
        atualizar();
        super.open();
    }

    private void atualizar() {
        grid.setItems(carrinho.getIngressos());
        String evento = carrinho.getEvento() == null ? "" : " - " + carrinho.getEvento().getTitulo();
        setHeaderTitle("Carrinho" + evento);
        total.setText(carrinho.tamanho() + " ingresso(s) - Total: "
                + Formatos.dinheiro(carrinho.total()));
        atualizarBotoes();
    }

    private void atualizarBotoes() {
        boolean temIngressos = carrinho.tamanho() > 0;
        pagar.setEnabled(temIngressos && !comprador.getValue().isBlank());
        esvaziar.setEnabled(temIngressos);
    }

    private void pagar() {
        try {
            Venda venda = gestao.registrarVenda(carrinho.getEvento(), comprador.getValue(),
                    carrinho.getIngressos());
            // só chega aqui se a venda foi aceita: agora pode esvaziar
            carrinho.limpar();
            comprador.clear();
            Notification.show("Venda " + venda.getId() + " registrada. Total: "
                            + Formatos.dinheiro(venda.calcularTotal()),
                    5000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.SUCCESS);
            close();
        } catch (RegraDeNegocioException ex) {
            // nada foi registrado: a janela continua aberta e o carrinho fica como estava
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.ERROR);
        }
    }
}
