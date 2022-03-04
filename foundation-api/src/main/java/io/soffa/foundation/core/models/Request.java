package io.soffa.foundation.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Request {

    private String url;
    private String body;
    private Map<String, Object> params;
    private Map<String, String> headers;

}
