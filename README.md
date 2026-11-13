# Sistema de gerenciamento de biblioteca

Projeto em Java para a atividade de Orientação a Objetos e Programação Funcional. O sistema gerencia livros, usuários, empréstimos ativos, devoluções, atrasos, multas, relatórios e estatísticas em memória.

## Requisitos

- Java 8 ou superior.
- Não utiliza banco de dados nem bibliotecas externas.

## Execução

Na raiz do projeto:

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

## Classes

- `Book`: dados do exemplar e disponibilidade.
- `User`: identificador, nome e matrícula.
- `Loan`: livro, usuário e datas do empréstimo; a data efetiva é definida na devolução.
- `Library`: cadastro, empréstimos ativos, devoluções, consultas, estatísticas e relatórios.
- `Main`: cria dados de demonstração e executa o fluxo do sistema.

## Regras de negócio

Cada livro é exemplar único. Um empréstimo só é registrado quando livro e usuário estão cadastrados e o livro está disponível. A devolução registra a data recebida, libera o livro e remove o empréstimo da coleção de ativos. Um empréstimo está atrasado quando permanece sem devolução e a data de referência é posterior à data prevista. A multa é fixa em R$ 2,00 por dia e não é calculada para empréstimos devolvidos.

## Funcionalidades

- Filtrar livros por categoria.
- Contar livros por categoria e empréstimos ativos por usuário.
- Consultar empréstimos ativos e atrasados em uma data de referência.
- Gerar relatório individual e total de multas.
- Exibir relatório de empréstimos e estatísticas.
- Comparar a contagem funcional e imperativa de atrasos.

## Programação funcional, pureza e imutabilidade

`Library.booksByCategory`, `Library.isOverdue`, `Library.overdueLoans` e `Loan.calculateFine` não modificam os objetos ou coleções recebidos. Com entradas iguais, retornam resultados iguais, portanto são exemplos de funções puras e transparência referencial.

`Library.fineReport(data)` usa `filter` por meio de `overdueLoans`, `map` para calcular as multas individuais, e `reduce` para somar os valores e compor as linhas do relatório. `totalFines(data)` também calcula o total com `map` e `reduce`; `getStatistics()` usa `reduce` para contar empréstimos ativos. Os streams funcionais não alteram os empréstimos.

Os getters retornam cópias não modificáveis das listas, protegendo a estrutura interna contra alterações do chamador. As operações de gerenciamento podem alterar o estado da biblioteca: registrar empréstimos/devoluções muda disponibilidade e lista de empréstimos, e a devolução define `returnDate`. Essas ações estão separadas das funções de consulta e cálculo.

`Book.isAvailable` e `Loan.returnDate` são mutáveis por necessidade das regras de gerenciamento. Assim, o sistema demonstra imutabilidade nos processamentos funcionais, mas as entidades não são totalmente imutáveis. O estado da biblioteca também é mutável; chamadas de consulta que recebem implicitamente esse estado devem ser entendidas como observações do estado atual, enquanto as funções estáticas puras dependem apenas dos parâmetros.

## Comparação de paradigmas

`countActiveDelayedLoans(data)` usa operações funcionais. `countActiveDelayedLoansImperative(data)` percorre a mesma lista com laço e condicional. Ambas recebem a mesma data e contam apenas empréstimos ainda ativos e atrasados; o `Main` imprime os resultados para comparação.

## Execução de demonstração

O `Main` cadastra livros e usuários, registra empréstimos, usa uma data de referência posterior às datas previstas para demonstrar atrasos, imprime estatísticas e relatórios, compara os dois paradigmas e registra uma devolução.
