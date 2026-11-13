import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

public class Library {
    private static final BigDecimal DAILY_FINE = new BigDecimal("2.00");
    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Loan> activeLoans = new ArrayList<>();

    public void registerBook(Book book) {
        books.add(Objects.requireNonNull(book));
    }

    public void registerUser(User user) {
        users.add(Objects.requireNonNull(user));
    }

    public boolean registerLoan(Loan loan) {
        Objects.requireNonNull(loan);
        if (!books.contains(loan.getBook()) || !users.contains(loan.getUser()) || !loan.getBook().isAvailable()) {
            return false;
        }
        activeLoans.add(loan);
        loan.getBook().setAvailable(false);
        return true;
    }

    public boolean registerReturn(Loan loan, LocalDate returnDate) {
        Objects.requireNonNull(loan);
        Objects.requireNonNull(returnDate);
        if (!activeLoans.remove(loan)) {
            return false;
        }
        loan.setReturnDate(returnDate);
        loan.getBook().setAvailable(true);
        return true;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(new ArrayList<>(books));
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(new ArrayList<>(users));
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(new ArrayList<>(activeLoans));
    }

    public static List<Book> booksByCategory(List<Book> source, String category) {
        return source.stream().filter(book -> Objects.equals(book.getCategory(), category)).collect(Collectors.toList());
    }

    public static BigDecimal calculateFine(long daysDelayed, BigDecimal dailyFine) {
        return Loan.calculateFine(daysDelayed, dailyFine);
    }

    public static boolean isOverdue(Loan loan, LocalDate referenceDate) {
        return loan.getReturnDate() == null && referenceDate.isAfter(loan.getExpectedReturnDate());
    }

    public static List<Loan> overdueLoans(List<Loan> source, LocalDate referenceDate) {
        return source.stream().filter(loan -> isOverdue(loan, referenceDate)).collect(Collectors.toList());
    }

    public Map<String, Long> countBooksByCategory() {
        return books.stream().collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));
    }

    public Map<String, Long> countLoansByUser() {
        return activeLoans.stream().collect(Collectors.groupingBy(loan -> loan.getUser().getName(), Collectors.counting()));
    }

    public long countActiveLoans() {
        return activeLoans.size();
    }

    public long countActiveDelayedLoans(LocalDate referenceDate) {
        return overdueLoans(activeLoans, referenceDate).size();
    }

    public BigDecimal totalFines(LocalDate referenceDate) {
        return overdueLoans(activeLoans, referenceDate).stream()
                .map(loan -> Loan.calculateFine(ChronoUnit.DAYS.between(loan.getExpectedReturnDate(), referenceDate), DAILY_FINE))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long countActiveDelayedLoansImperative(LocalDate referenceDate) {
        long count = 0;
        for (Loan loan : activeLoans) {
            if (isOverdue(loan, referenceDate)) count++;
        }
        return count;
    }

    public String fineReport(LocalDate referenceDate) {
        BigDecimal total = overdueLoans(activeLoans, referenceDate).stream()
                .map(loan -> Loan.calculateFine(ChronoUnit.DAYS.between(loan.getExpectedReturnDate(), referenceDate), DAILY_FINE))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return overdueLoans(activeLoans, referenceDate).stream()
                .map(loan -> String.format("%s - %s: R$ %s", loan.getUser().getName(), loan.getBook().getTitle(),
                        Loan.calculateFine(ChronoUnit.DAYS.between(loan.getExpectedReturnDate(), referenceDate), DAILY_FINE)))
                .reduce((left, right) -> left + System.lineSeparator() + right)
                .map(lines -> lines + System.lineSeparator() + "Total: R$ " + total)
                .orElse("Nenhuma multa registrada. Total: R$ 0.00");
    }

    public String getReport() {
        return activeLoans.stream()
                .map(loan -> String.format("Livro: %s | Usuário: %s | Devolução prevista: %s", loan.getBook().getTitle(), loan.getUser().getName(), loan.getExpectedReturnDate()))
                .reduce((left, right) -> left + System.lineSeparator() + right)
                .orElse("Nenhum empréstimo ativo.");
    }

    public String getStatistics() {
        long availableBooks = books.stream().filter(Book::isAvailable).count();
        long unavailableBooks = books.size() - availableBooks;
        long activeLoanCount = activeLoans.stream().map(loan -> 1L).reduce(0L, Long::sum);
        return String.format("Livros: %d | Disponíveis: %d | Emprestados: %d | Empréstimos ativos: %d | Usuários: %d",
                books.size(), availableBooks, unavailableBooks, activeLoanCount, users.size());
    }
}
