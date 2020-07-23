package summitmail.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "atconfigs")
public class ATConfig {

    @Id
    @BsonProperty("id")
    private String id;
    @Field("name")
    @Indexed
    private String name;
    @Field("userId")
    private String userId;
    @JsonIgnore
    @Field("apiKey")
    private String apiKey;
    @Field("baseId")
    private String baseId;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;

    public ATConfig() { super(); }

    public String getId() { return id; }

    public String getUserId() { return userId; }
    public void setUserId(String id) { this.userId = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String key) { this.apiKey = key; }
    public boolean hasApiKey() { return this.getApiKey() != null; }

    public String getBaseId() { return baseId; }
    public void setBaseId(String id) { this.baseId = id; }
    public boolean hasBaseId() { return this.getBaseId() != null; }
}
