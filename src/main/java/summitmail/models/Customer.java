package summitmail.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

public class Customer {

    private String id;
    private String name;
    private String country;
    private String website;
    private String email;

    public Customer() {
        super();
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks for if Customer object is empty.
     *
     * @return if no name set, customer is empty.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return this.name == null || "".equals(this.getName());
    }

    public Customer withNewId() {
        setId(new ObjectId().toHexString());
        return this;
    }


}
