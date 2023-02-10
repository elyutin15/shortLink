package shortlink.demo.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {
    String url;

    @JsonCreator
    public Link(
            @JsonProperty("url") String url
    ) {
        this.url = url;
    }
}
