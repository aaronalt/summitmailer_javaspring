package summitmail.models;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "templates")
public class Template {

    @Id
    private String id;
    @Field("userId")
    private String userId;
    @Field("file")
    // check what type is supposed to be for long text/bytes
    private String file;
    @Field("type")
    private String type;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;


    public Template() { super(); }

    public String getID() { return id; }

    public String getUserId() { return userId; }
    public void setUserId(String id) { this.userId = id; }
}
