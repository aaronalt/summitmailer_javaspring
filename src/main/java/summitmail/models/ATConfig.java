package summitmail.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "atconfigs")
public class ATConfig {

    @Id
    @BsonProperty("id")
    private String id;
    @Field("user_id")
    private String user_id;
    @JsonIgnore
    @Field("api_key")
    private String api_key;
    @Field("base_id")
    private String base_id;
    @Field("created_at")
    @CreatedDate
    Date created_at;
    @Field("updated_at")
    @LastModifiedDate
    Date updated_at;

    public ATConfig() { super(); }

    public String getId() { return id; }

    public String getUserId() { return user_id; }
    public void setUserId(String id) { this.user_id = id; }

    public String getApiKey() { return api_key; }
    public void setApiKey(String key) { this.api_key = key; }

    public String getBaseId() { return base_id; }
    public void setBaseId(String id) { this.base_id = id; }

    public Date getCreatedAtDate() { return created_at; }
    public void setCreatedAtDate(Date date) { this.created_at = date; }

    public Date getUpdatedAtDate() { return updated_at; }
    public void setUpdatedAtDate(Date date) { this.updated_at = date; }
}
