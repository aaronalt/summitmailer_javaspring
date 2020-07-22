package summitmail.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "outputs")
public class Output {

    @Id
    @BsonProperty("id")
    private String id;
    @Field("user_id")
    private String user_id;
    @Field("file")
    private String file;
    @CreatedDate
    Date created_at;
    @LastModifiedDate
    Date updated_at;

    public Output() { super(); }

    public String getId() { return id; }

    public String getFile() { return file; }

    public Date getCreatedAtDate() { return created_at; }
    public void setCreatedAtDate(Date date) { this.created_at = date; }

    public Date getUpdatedAtDate() { return updated_at; }
    public void setUpdatedAtDate(Date date) { this.updated_at = date; }

}
