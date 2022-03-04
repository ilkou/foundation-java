package com.company.app.core;

import io.soffa.foundation.core.RequestContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.inject.Named;


@Named
public class EchoImpl implements Echo {

    @Override
    public String handle(@NonNull String input, @NonNull RequestContext context) {
        return input;
    }

}
