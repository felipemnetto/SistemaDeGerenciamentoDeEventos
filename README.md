# Sistema de Gestão de Eventos

Sistema para cadastrar e consultar eventos, registrar vendas e organizar ingressos de modalidades diferentes.

## Como executar

No terminal, entre na pasta do projeto Maven:

```text
cd SistemaDeGerenciamentoDeEventos
```

Execute a aplicação:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

## Decisões de modelagem

### Organização das classes

O projeto foi dividido em duas responsabilidades principais:

- `dominio`, que contém as classes e regras de negócio.
- `ui`, que contém as telas, componentes e navegação construídos com Vaadin.

A `GestaoEventos` centraliza a carga dos dados iniciais, a criação dos ingressos e o registro das vendas. Os arquivos CSV são lidos na inicialização e os objetos correspondentes são armazenados em coleções mantidas em memória.

As principais classes do domínio são:

- `Evento`: representa um evento, suas características, capacidade, preço-base e parâmetros da modalidade promocional.
- `Venda`: representa uma operação de compra realizada por um comprador e contém uma coleção de ingressos.
- `Ingresso`: representa os dados comuns de um ingresso individual.
- `IngressoInteira`, `IngressoMeiaEntrada`, `IngressoVip` e `IngressoPromocional`: representam as quatro modalidades disponíveis.
- `Carrinho`: mantém temporariamente os ingressos selecionados pela interface antes da confirmação da venda.
- `GestaoEventos`: coordena os objetos do sistema e expõe operações utilizadas pelas telas.

Cada venda pertence a um único evento e pode conter ingressos de modalidades diferentes. Cada ingresso pertence à venda em que foi informado e contém o nome e o CPF do participante.

### Uso de herança e polimorfismo

`Ingresso` é uma classe abstrata que define os métodos comuns `calcularPreco()` e `permiteEntradaAntecipada()`. As quatro modalidades implementam esses métodos de acordo com suas próprias regras:

- `IngressoInteira` utiliza o preço-base e não permite entrada antecipada.
- `IngressoMeiaEntrada` utiliza 50% do preço-base e exige justificativa de elegibilidade.
- `IngressoVip` utiliza 150% do preço-base e permite entrada antecipada.
- `IngressoPromocional` utiliza o preço-base descontado pelo percentual configurado no evento e não permite entrada antecipada.

O cálculo do preço e a indicação de entrada antecipada são realizados polimorficamente. Assim, a interface e as demais partes do sistema podem tratar os ingressos pela superclasse `Ingresso`, sem precisar identificar a modalidade por meio de testes de tipo.

O `switch` utilizado em `GestaoEventos` é responsável exclusivamente por selecionar a subclasse correta no momento da criação do ingresso. Depois da criação, as regras próprias de preço e entrada antecipada ficam encapsuladas em cada subclasse.

### Atributos e tipos escolhidos

- O identificador do evento, da venda e do ingresso é representado por `String`.
- Datas são representadas por `LocalDate`, horários por `LocalTime` e data/hora da venda por `LocalDateTime`.
- Valores monetários são representados por `BigDecimal`, evitando problemas de precisão em cálculos financeiros.
- O CPF é preservado como texto e validado com exatamente 11 dígitos numéricos. O sistema não implementa a validação dos dígitos verificadores.
- A justificativa é obrigatória somente para ingressos do tipo meia-entrada.

### Eventos

Um evento possui identificador único, título, descrição, data, horário de início, local, capacidade máxima, preço-base, percentual de desconto promocional e limite de ingressos promocionais.

Os construtores e operações do domínio validam os dados principais antes da criação dos objetos. A capacidade máxima não pode ser negativa, o limite promocional não pode ultrapassar a capacidade e os valores de preço e desconto precisam respeitar intervalos válidos.

A disponibilidade geral é calculada pela diferença entre a capacidade máxima e a quantidade de ingressos já registrados no evento. A disponibilidade promocional é calculada pela diferença entre o limite promocional e os ingressos promocionais vendidos.

A ocupação é obtida pela relação entre a quantidade de ingressos vendidos e a capacidade do evento.

Eventos passados podem ser consultados, mas não recebem novas vendas. Esta verificação é feita em relação à data e ao horário de início armazenados no evento.

### Vendas

A venda possui identificador, nome do comprador, data/hora da operação, evento relacionado e uma lista de ingressos.

Os ingressos são adicionados à venda somente após as seguintes validações serem concluídas:

- A venda precisa possuir pelo menos um ingresso.
- O evento precisa estar no futuro no momento da operação.
- O total de ingressos não pode ultrapassar a capacidade disponível.
- O total de ingressos promocionais não pode ultrapassar a cota restante.
- Todos os ingressos precisam pertencer ao evento da venda.
- Um mesmo CPF não pode aparecer mais de uma vez na mesma venda.

Se alguma validação falhar, a venda não é registrada e os ingressos não são incluídos no evento. Dessa forma, uma venda segue o princípio de operação indivisível: ou todos os ingressos são aceitos, ou nenhum é registrado.

O carrinho verifica que todos os ingressos selecionados pertençam ao mesmo evento. Depois que a venda é registrada, a tela responsável por controlar o carrinho pode removê-lo da memória temporária.

### Listas e API de coleções

As classes do domínio utilizam coleções `List` e `LinkedList` para associar eventos, vendas e ingressos.

Exemplos de uso da API de coleções e da Stream API são:

- `filter`, para selecionar eventos conforme situação, período ou local.
- `map`, para transformar registros ou obter informações dos ingressos.
- `sorted`, para ordenar eventos por data e horário.
- `collect(Collectors.toSet())`, para identificar CPFs repetidos em uma venda.
- `anyMatch`, para verificar ingressos associados a outro evento.
- `count`, para calcular a quantidade de ingressos promocionais.
- `reduce`, para somar os preços dos ingressos.

As listas expostas pelo domínio são copiadas ou protegidas por `List.copyOf`, impedindo que a interface altere diretamente a coleção interna.

### Carga inicial dos dados

Os dados iniciais são obtidos da pasta `Dados` por meio das classes `LeitorDadosCsv` e `GestaoEventos`.

Os arquivos utilizados são:

- `eventos.csv`, com os dados básicos dos eventos.
- `modalidades_evento.csv`, com o desconto e o limite promocional de cada evento.
- `vendas.csv`, com o cabeçalho das vendas.
- `ingressos.csv`, com os participantes e as modalidades de cada venda.

Os arquivos são separados por ponto e vírgula, possuem cabeçalho e registram datas no formato ISO. O leitor converte os campos em tipos Java como `LocalDate`, `LocalTime`, `LocalDateTime` e `BigDecimal`.

A carga inicial relaciona os registros pelos identificadores de evento e venda. Os dados lidos são transformados em objetos do domínio e passam pelas validações antes de serem registrados.

### Interface Vaadin

A interface utiliza Vaadin com Spring Boot e uma `AppLayout` compartilhada pelas telas. `MainView` fornece a barra superior, o menu lateral e o carrinho.

As telas já existentes são:

- `EventosView`: lista e filtra eventos, mostra vagas, ocupação e detalhes.
- `CadastroEventoView`: estrutura destinada ao cadastro de novos eventos.
- `IngressosView`: permite selecionar evento e modalidade e adiciona ingressos ao carrinho.
- `CarrinhoView`: apresenta os ingressos selecionados, calcula o total e confirma ou cancela a operação.
- `VendasView`: estrutura destinada à consulta das vendas realizadas.
- `RelatoriosView`: estrutura destinada às consultas gerenciais.

As telas acessam as instâncias compartilhadas de `GestaoEventos` e `Carrinho`. As telas coletam os dados informados pelo usuário, mas as validações comerciais são executadas pelas classes do domínio.

## Registro dos esclarecimentos de requisitos obtidos durante o desenvolvimento

Até o momento, não houve esclarecimentos externos obtidos junto ao professor. As decisões adicionais adotadas pela equipe foram documentadas a partir das regras presentes no enunciado do trabalho.
