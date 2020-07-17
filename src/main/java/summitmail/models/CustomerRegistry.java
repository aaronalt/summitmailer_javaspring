package summitmail.models;

// import javax.validation.constraints.Email;
// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Size;

public class CustomerRegistry {

    // @NotNull(message = "`name` field is mandatory")
    // @Size(min = 3, message = "`name` must be at least 3 characters long")
    private String name;

    // @NotNull(message = "`email` field is mandatory")
    // @Email(message = "`email` must be an well-formed email address")
    private String email;

    // @NotNull(message = "`country` field is mandatory")
    // @Size(min = 8, message = "`country` must be at least 8 characters long")
    private String country;

    private String website;

    public CustomerRegistry() {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }


}