package io.soffa.foundation.core.features.jobs;

import io.soffa.foundation.annotations.Store;
import io.soffa.foundation.core.data.model.EntityModel;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Store("f_pending_jobs")
public class PendingJob implements EntityModel {

    private String id;
    private String operation;
    private String subject;
    private String data;
    private Date createdAt;
    private String lastError;
    private int errorsCount;

    public void failed(String message) {
        lastError = message;
        errorsCount++;
    }


}
