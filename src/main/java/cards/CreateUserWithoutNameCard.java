package cards;

public class CreateUserWithoutNameCard {

    private String email;
    private String password;

    public CreateUserWithoutNameCard(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CreateUserWithoutNameCard() { }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
