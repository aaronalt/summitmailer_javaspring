package summitmail.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.*;
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
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;


    public Output() { super(); }

    public String getId() { return id; }

    public String getFile() { return file; }
}
