package cards;

public class CreateUserWithoutPasswordCard {

    private String email;
    private String name;

    public CreateUserWithoutPasswordCard(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public CreateUserWithoutPasswordCard() { }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
