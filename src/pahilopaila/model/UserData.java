package pahilopaila.model;

public class UserData {
    private int id;
    private String username;
    private String email;
    private String roles;

    public UserData(int id, String username, String email, String roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRoles() {
        return roles;
    }

    public String getName() {
        return username;
    }
}