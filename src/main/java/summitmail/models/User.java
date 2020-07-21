package summitmail.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Map;

public class User {

    private String name;
    private String email;
    @JsonIgnore
    private String hashedpw;
    private Date created_at;
    private Date updated_at;

    private Map<String, String> preferences;

    public User() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedpw() {
        return hashedpw;
    }

    public void setHashedpw(String hashedpw) {
        this.hashedpw = hashedpw;
    }

    public Date getCreatedAtDate() { return created_at; }

    public void setCreatedAtDate(Date date) { this.created_at = date; }

    public Date getUpdatedAtDate() { return updated_at; }

    public void setUpdatedAtDate(Date date) { this.updated_at = date; }

    /**
     * Checks if user object is empty.
     *
     * @return if no email set, the user is empty.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return this.email == null || "".equals(this.getEmail());
    }
}
