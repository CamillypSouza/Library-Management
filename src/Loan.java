import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Loan {
    private final UUID id;
    private final Book book;
    private final User user;
    private final LocalDate loanDate;
    private final LocalDate expectedReturnDate;
    private LocalDate returnDate;


    public Loan(Book book, User user) {
        this.id = UUID.randomUUID();
        this.book = book;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.expectedReturnDate = loanDate.plusWeeks(5);
    }

    public UUID getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public static BigDecimal calculateFine(long daysDelayed, BigDecimal dailyFine) {
        if (dailyFine == null) {
            throw new IllegalArgumentException("A multa diária é obrigatória.");
        }
        if (daysDelayed <= 0 || dailyFine.signum() < 0) {
            return BigDecimal.ZERO;
        }
        return dailyFine.multiply(BigDecimal.valueOf(daysDelayed));
    }
}
