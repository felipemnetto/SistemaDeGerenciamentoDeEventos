package sistemaDeGerenciamentoDeEventos.ui;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import sistemaDeGerenciamentoDeEventos.dominio.Evento;
import sistemaDeGerenciamentoDeEventos.dominio.Ingresso;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoInteira;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoMeiaEntrada;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoPromocional;
import sistemaDeGerenciamentoDeEventos.dominio.IngressoVip;
import sistemaDeGerenciamentoDeEventos.dominio.RegraDeNegocioException;
import sistemaDeGerenciamentoDeEventos.dominio.Sistema;
import sistemaDeGerenciamentoDeEventos.dominio.Venda;

@Route("")
public class MainView extends VerticalLayout {

    private final Evento eventoAtual;
    private final Map<String, Integer> quantidades = new HashMap<>();
    private final Map<String, BigDecimal> precosTipo = new HashMap<>();
    private final Map<String, String> justificativasMeia = new HashMap<>();

    private final TextField txtNomeComprador = new TextField("Nome do Comprador");
    private final TextField txtCpfComprador = new TextField("CPF do Comprador");

    private final VerticalLayout layoutCarrinhoItems = new VerticalLayout();
    private final Span lblTotalGeral = new Span("Total: R$ 0,00");

    public MainView() {
        List<Evento> eventos = Sistema.getInstance().getEventos(); // [cite: 6]
        this.eventoAtual = eventos.isEmpty() ? null : eventos.get(0);

        getElement().setAttribute("theme", "dark");

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#0e0d0d");
        getStyle().set("padding", "20px");

        VerticalLayout cardPrincipal = new VerticalLayout();
        cardPrincipal.setWidth("580px");
        cardPrincipal.getStyle().set("background-color", "#1e1e1e");
        cardPrincipal.getStyle().set("padding", "30px");
        cardPrincipal.getStyle().set("border-radius", "16px");
        cardPrincipal.getStyle().set("box-shadow", "0 8px 24px rgba(0,0,0,0.5)");
        cardPrincipal.setAlignItems(Alignment.STRETCH);

        String tituloTexto = (eventoAtual != null) ? eventoAtual.getTitulo() : "Compra de Ingresso";
        H2 titulo = new H2(tituloTexto);
        titulo.getStyle().set("color", "#ffffff");
        titulo.getStyle().set("margin-top", "0");
        titulo.getStyle().set("text-align", "center");

        cardPrincipal.add(titulo);

        if (eventoAtual == null) {
            cardPrincipal.add(new Span("Nenhum evento disponivel no momento."));
            add(cardPrincipal);
            return;
        }

        BigDecimal base = eventoAtual.getPrecoBase(); // [cite: 8]
        BigDecimal vip = base.multiply(BigDecimal.valueOf(1.5)); // [cite: 5]
        BigDecimal meia = base.multiply(BigDecimal.valueOf(0.5)); // [cite: 10]
        BigDecimal promocional = base.multiply(eventoAtual.getPercentDescontoPromocional())
                .divide(BigDecimal.valueOf(100)); // [cite: 12]

        precosTipo.put("Inteira", base);
        precosTipo.put("Meia-Entrada", meia);
        precosTipo.put("VIP", vip);
        precosTipo.put("Promocional", promocional);

        for (String tipo : precosTipo.keySet()) {
            quantidades.put(tipo, 0);
        }

        txtNomeComprador.setWidthFull();
        txtCpfComprador.setWidthFull();
        cardPrincipal.add(txtNomeComprador, txtCpfComprador, new Hr());

        for (Map.Entry<String, BigDecimal> entry : precosTipo.entrySet()) {
            cardPrincipal.add(criarLinhaIngresso(entry.getKey(), entry.getValue()));
        }

        cardPrincipal.add(new Hr());

        Span tituloCarrinho = new Span("Carrinho / Resumo");
        tituloCarrinho.getStyle().set("font-weight", "bold");
        tituloCarrinho.getStyle().set("color", "#b0b0b0");

        layoutCarrinhoItems.setPadding(false);
        layoutCarrinhoItems.setSpacing(false);

        lblTotalGeral.getStyle().set("font-size", "1.2em");
        lblTotalGeral.getStyle().set("font-weight", "bold");
        lblTotalGeral.getStyle().set("color", "#4caf50");
        lblTotalGeral.getStyle().set("margin-top", "10px");

        Button btnComprar = new Button("Finalizar Compra", event -> {
            try {
                if (txtNomeComprador.isEmpty() || txtCpfComprador.isEmpty()) {
                    Notification.show("Preencha o Nome e o CPF do comprador!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                int totalIngressos = quantidades.values().stream().mapToInt(Integer::intValue).sum();
                if (totalIngressos == 0) {
                    Notification.show("Adicione pelo menos um ingresso ao carrinho!", 3000,
                            Notification.Position.MIDDLE);
                    return;
                }

                if (quantidades.get("Meia-Entrada") > 0) { // [cite: 10]
                    String motivo = justificativasMeia.get("Meia-Entrada");
                    if (motivo == null || motivo.isEmpty()) {
                        Notification.show("Selecione o motivo da Meia-Entrada!", 4000, Notification.Position.MIDDLE);
                        return;
                    }
                }

                String idVenda = "V" + System.currentTimeMillis();
                Venda novaVenda = new Venda(idVenda, txtNomeComprador.getValue(), LocalDateTime.now(), eventoAtual); // [cite:
                                                                                                                     // 7]

                List<Ingresso> listaIngressos = new ArrayList<>();
                int contador = 1;

                for (Map.Entry<String, Integer> entry : quantidades.entrySet()) {
                    String tipo = entry.getKey();
                    int qtd = entry.getValue();

                    for (int i = 0; i < qtd; i++) {
                        String idIng = "ING-" + contador++;
                        String nomePart = txtNomeComprador.getValue();
                        String cpfPart = txtCpfComprador.getValue();

                        Ingresso ing;
                        switch (tipo) {
                            case "Inteira":
                                ing = new IngressoInteira(nomePart, cpfPart, idIng, idVenda, eventoAtual); // [cite: 9]
                                break;
                            case "Meia-Entrada":
                                ing = new IngressoMeiaEntrada(nomePart, cpfPart, idIng, idVenda,
                                        justificativasMeia.get("Meia-Entrada"), eventoAtual); // [cite: 10]
                                break;
                            case "VIP":
                                ing = new IngressoVip(nomePart, cpfPart, idIng, idVenda, eventoAtual); // [cite: 5]
                                break;
                            case "Promocional":
                                ing = new IngressoPromocional(nomePart, cpfPart, idIng, idVenda, eventoAtual); // [cite:
                                                                                                               // 12]
                                break;
                            default:
                                throw new RegraDeNegocioException("Tipo de ingresso desconhecido."); // [cite: 11]
                        }

                        listaIngressos.add(ing);
                    }
                }

                novaVenda.addIngresso(listaIngressos); // [cite: 7, 11]

                Notification.show("Compra realizada com sucesso! ID: " + idVenda, 4000, Notification.Position.MIDDLE);

                for (String tipo : quantidades.keySet()) {
                    quantidades.put(tipo, 0);
                }
                justificativasMeia.clear();
                atualizarCarrinho();

            } catch (RegraDeNegocioException e) {
                Notification.show("Erro de Regra de Negocio: " + e.getMessage(), 5000, Notification.Position.MIDDLE); // [cite:
                                                                                                                      // 7,
                                                                                                                      // 11]
            } catch (Exception e) {
                Notification.show("Erro: " + e.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });

        btnComprar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnComprar.setWidthFull();
        btnComprar.getStyle().set("margin-top", "15px");

        cardPrincipal.add(tituloCarrinho, layoutCarrinhoItems, lblTotalGeral, btnComprar);
        add(cardPrincipal);

        atualizarCarrinho();
    }

    private VerticalLayout criarLinhaIngresso(String nome, BigDecimal preco) {
        VerticalLayout bloco = new VerticalLayout();
        bloco.setPadding(false);
        bloco.setSpacing(true);

        HorizontalLayout linha = new HorizontalLayout();
        linha.setWidthFull();
        linha.setAlignItems(Alignment.CENTER);
        linha.setJustifyContentMode(JustifyContentMode.BETWEEN);

        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);
        Span lblNome = new Span(nome);
        lblNome.getStyle().set("font-weight", "bold").set("color", "#ffffff");
        Span lblPreco = new Span("R$ " + preco);
        lblPreco.getStyle().set("color", "#aaaaaa").set("font-size", "0.85em");
        info.add(lblNome, lblPreco);

        boolean ehMeia = nome.equals("Meia-Entrada"); // [cite: 10]
        Select<String> selectMotivo = new Select<>();
        selectMotivo.setPlaceholder("Motivo da Meia");
        selectMotivo.setItems("Estudante", "Idoso (60+)", "PCD", "Professor");
        selectMotivo.setWidth("180px");
        selectMotivo.setVisible(false);

        selectMotivo.addValueChangeListener(e -> {
            justificativasMeia.put(nome, e.getValue());
            atualizarCarrinho();
        });

        HorizontalLayout controles = new HorizontalLayout();
        controles.setAlignItems(Alignment.CENTER);

        Button btnMenos = new Button("-", e -> {
            alterarQuantidade(nome, -1);
            int qtd = quantidades.get(nome);
            if (ehMeia) {
                selectMotivo.setVisible(qtd > 0);
                if (qtd == 0) {
                    selectMotivo.clear();
                    justificativasMeia.remove(nome);
                }
            }
        });

        Button btnMais = new Button("+", e -> {
            alterarQuantidade(nome, 1);
            int qtd = quantidades.get(nome);
            if (ehMeia) {
                selectMotivo.setVisible(qtd > 0);
            }
        });

        btnMenos.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnMais.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Span lblQtd = new Span("0");
        lblQtd.getStyle().set("width", "25px").set("text-align", "center").set("color", "#ffffff");

        btnMenos.addClickListener(e -> lblQtd.setText(String.valueOf(quantidades.get(nome))));
        btnMais.addClickListener(e -> lblQtd.setText(String.valueOf(quantidades.get(nome))));

        controles.add(btnMenos, lblQtd, btnMais);
        linha.add(info, controles);
        bloco.add(linha);

        if (ehMeia) {
            HorizontalLayout linhaMotivo = new HorizontalLayout(selectMotivo);
            linhaMotivo.setJustifyContentMode(JustifyContentMode.END);
            linhaMotivo.setWidthFull();
            bloco.add(linhaMotivo);
        }

        return bloco;
    }

    private void alterarQuantidade(String nome, int delta) {
        int atual = quantidades.get(nome);
        int novoValor = atual + delta;
        if (novoValor >= 0) {
            quantidades.put(nome, novoValor);
            atualizarCarrinho();
        }
    }

    private void atualizarCarrinho() {
        layoutCarrinhoItems.removeAll();
        BigDecimal totalGeral = BigDecimal.ZERO;
        boolean temItem = false;

        for (Map.Entry<String, Integer> entry : quantidades.entrySet()) {
            String nome = entry.getKey();
            int qtd = entry.getValue();

            if (qtd > 0) {
                temItem = true;
                BigDecimal subtotal = precosTipo.get(nome).multiply(BigDecimal.valueOf(qtd));
                totalGeral = totalGeral.add(subtotal);

                String detalhe = "";
                if (nome.equals("Meia-Entrada")) {
                    String motivo = justificativasMeia.get(nome);
                    detalhe = (motivo != null) ? " (" + motivo + ")" : " (Pendente)";
                }

                Span itemSpan = new Span(qtd + "x " + nome + detalhe + " - R$ " + subtotal);
                itemSpan.getStyle().set("color", "#e0e0e0").set("font-size", "0.9em");
                layoutCarrinhoItems.add(itemSpan);
            }
        }

        if (!temItem) {
            Span vazio = new Span("Carrinho vazio");
            vazio.getStyle().set("color", "#777777").set("font-style", "italic").set("font-size", "0.9em");
            layoutCarrinhoItems.add(vazio);
        }

        lblTotalGeral.setText("Total: R$ " + totalGeral);
    }
}
