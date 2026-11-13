import java.util.UUID;

public class Book {
    private final UUID id;
    private final String title;
    private final String author;
    private final String category;
    private boolean isAvailable;


    public Book(String title, String author, String category) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}