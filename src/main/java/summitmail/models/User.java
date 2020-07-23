package summitmail.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "users")
public class User {

    @Id
    @BsonProperty("id")
    private String id;
    @Field("name")
    private String name;
    @Field("email")
    private String email;
    @Field("hashedpw")
    @JsonIgnore
    private String hashedpw;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;

    @DBRef
    private List<Customer> customers;
    @DBRef
    private List<ATConfig> airtable_configs;
    @DBRef
    private List<Template> templates;
    @DBRef
    private List<Output> outputs;

    public User() {
        super();
    }

    public String getId() { return id; }

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
