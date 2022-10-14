package cards;

public class CreateUserWithoutEmailCard {

    private String password;
    private String name;

    public CreateUserWithoutEmailCard(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public CreateUserWithoutEmailCard() { }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
