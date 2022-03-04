package io.soffa.foundation.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Obj {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private transient String object;

}
