import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Book book1 = new Book("O Hobbit", "J. R. R. Tolkien", "Fantasia");
        Book book2 = new Book("Clean Code", "Robert C. Martin", "Programação");
        Book book3 = new Book("Dom Casmurro", "Machado de Assis", "Literatura");
        Book book4 = new Book("Java Básico", "Autor Exemplo", "Programação");
        User user1 = new User("Ana");
        User user2 = new User("Bruno");
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerBook(book3);
        library.registerBook(book4);
        library.registerUser(user1);
        library.registerUser(user2);

        Loan loan1 = new Loan(book1, user1);
        Loan loan2 = new Loan(book2, user2);
        Loan loan3 = new Loan(book4, user1);
        library.registerLoan(loan1);
        library.registerLoan(loan2);
        library.registerLoan(loan3);
        LocalDate referenceDate = loan1.getExpectedReturnDate().plusDays(3);

        System.out.println("=== LIVROS POR CATEGORIA ===");
        Library.booksByCategory(library.getBooks(), "Programação").forEach(book -> System.out.println(book.getTitle()));
        System.out.println("=== ESTATÍSTICAS ===");
        System.out.println("Livros por categoria: " + library.countBooksByCategory());
        System.out.println("Empréstimos ativos por usuário: " + library.countLoansByUser());
        System.out.println("Empréstimos ativos: " + library.countActiveLoans());
        System.out.println("Empréstimos ativos atrasados: " + library.countActiveDelayedLoans(referenceDate));
        System.out.println("Multas totais: R$ " + library.totalFines(referenceDate));
        System.out.println(library.getStatistics());
        System.out.println("=== RELATÓRIO DE MULTAS ===");
        System.out.println(library.fineReport(referenceDate));

        long functionalCount = library.countActiveDelayedLoans(referenceDate);
        long imperativeCount = library.countActiveDelayedLoansImperative(referenceDate);
        System.out.println("=== COMPARAÇÃO ===");
        System.out.println("Funcional: " + functionalCount + "; imperativa: " + imperativeCount);
        System.out.println("Resultados equivalentes: " + (functionalCount == imperativeCount));
        System.out.println("=== EMPRÉSTIMOS ATIVOS ===");
        System.out.println(library.getReport());
        library.registerReturn(loan3, referenceDate);
        System.out.println("Empréstimos ativos após devolução: " + library.countActiveLoans());
        System.out.println("Empréstimos atrasados após devolução: " + library.countActiveDelayedLoans(referenceDate));
    }
}
