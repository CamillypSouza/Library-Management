import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String enrollment;


    public User(String name){
        this.id = UUID.randomUUID();
        this.name = name;

        UUID randomCode = UUID.randomUUID();
        this.enrollment = String.format("%s-ENROLL", randomCode);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEnrollment() {
        return enrollment;
    }
}
