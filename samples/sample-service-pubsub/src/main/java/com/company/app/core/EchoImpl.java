package com.company.app.core;

import io.soffa.foundation.core.RequestContext;
import jakarta.inject.Named;
import org.checkerframework.checker.nullness.qual.NonNull;


@Named
public class EchoImpl implements Echo {

    @Override
    public String handle(@NonNull String input, @NonNull RequestContext context) {
        return input;
    }

}
