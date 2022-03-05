package io.soffa.foundation.core.features.journal;

import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.data.EntityRepository;
import io.soffa.foundation.errors.ErrorUtil;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Supplier;

public interface JournalRepository extends EntityRepository<Journal> {

    default void log(RequestContext context, @NonNull String kind, @NonNull String subject, @NonNull String event) {
        insert(
            Journal.builder()
                .event(event)
                .subject(subject)
                .kind(kind)
                .build()
                .withContext(context)
        );
    }

    @SneakyThrows
    default <T> T log(RequestContext context, @NonNull String kind, @NonNull String subject, @NonNull String event, Supplier<T> supplier) {
        try {
            T result = supplier.get();
            log(context, kind, subject, event);
            return result;
        } catch (Exception e) {
            insert(
                Journal.builder()
                    .event(event)
                    .subject(subject)
                    .kind(kind)
                    .error(ErrorUtil.loookupOriginalMessage(e))
                    .status("failed")
                    .build()
                    .withContext(context)
            );
            throw e;
        }
    }


}
