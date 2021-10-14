
package mainpackage.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "url",
    "bio",
    "avatar",
    "avatar_name",
    "cover",
    "cover_name",
    "reputation",
    "reputation_name",
    "created",
    "pro_expiration",
    "user_follow",
    "is_blocked"
})
@Generated("jsonschema2pojo")
public class DataAccountResponse implements Serializable
{

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("bio")
    private Object bio;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("avatar_name")
    private String avatarName;
    @JsonProperty("cover")
    private String cover;
    @JsonProperty("cover_name")
    private String coverName;
    @JsonProperty("reputation")
    private Integer reputation;
    @JsonProperty("reputation_name")
    private String reputationName;
    @JsonProperty("created")
    private Integer created;
    @JsonProperty("pro_expiration")
    private Boolean proExpiration;
    @JsonProperty("user_follow")
    private UserFollowAccountresponse userFollowAccountresponse;
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7329082633415876643L;

}
