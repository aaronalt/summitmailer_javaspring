package summitmail.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "templates")
public class Template {

    @Id
    private String id;
    @Field("user_id")
    private String user_id;
    @Field("file")
    // check what type is supposed to be for long text/bytes
    private String file;
    @Field("type")
    private String type;
    @CreatedDate
    Date created_at;
    @LastModifiedDate
    Date updated_at;

    public Template() { super(); }

    public String getID() { return id; }

    public String getUserId() { return user_id; }
    public void setUserId(String id) { this.user_id = id; }

    public Date getCreatedAtDate() { return created_at; }
    public void setCreatedAtDate(Date date) { this.created_at = date; }

    public Date getUpdatedAtDate() { return updated_at; }
    public void setUpdatedAtDate(Date date) { this.updated_at = date; }


}
