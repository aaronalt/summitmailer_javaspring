package summitmail.models;

import org.springframework.data.annotation.Id;

public class Client {

    @Id private String id;

    private String name;
    private String country;
    private String website;
    private String email;

    public Client(String name, String country, String website, String email) {
        this.name = name;
        this.country = country;
        this.website = website;
        this.email = email;
    }

    public String getId() {
        return id;
    }

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

}
