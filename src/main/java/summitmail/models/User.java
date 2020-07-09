package summitmail.models;

import org.springframework.data.annotation.Id;

public class User {

    @Id private String id;

    private String email;
    private String password;
    /* encrypt this pw */

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

}
