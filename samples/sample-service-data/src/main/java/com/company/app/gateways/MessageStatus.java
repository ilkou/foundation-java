package com.company.app.gateways;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.soffa.foundation.annotations.SnakeCaseModel;

@SnakeCaseModel
public enum MessageStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("sent")
    SENT,
    @JsonProperty("delivered")
    DELIVERED,
    @JsonProperty("failed")
    FAILED;

    public String toSting() {
        return this.name().toLowerCase();
    }
}
