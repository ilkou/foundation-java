package io.soffa.foundation.app.gateway;

import io.soffa.foundation.actions.ActionDispatcher;
import io.soffa.foundation.app.core.PingAction;
import io.soffa.foundation.app.core.PingResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

public class AppController implements API {

    private final ActionDispatcher dispatcher;

    @Override
    @GetMapping("ping")
    public PingResponse ping() {
        return dispatcher.dispatch(PingAction.class);
    }

}
