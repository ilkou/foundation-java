package com.company.app.gateways;

import com.company.app.core.EchoAction;
import com.company.app.core.PingAction;
import com.company.app.core.PingResponse;
import com.company.app.core.model.InputData;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.actions.ActionDispatcher;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class AppController implements API {

    private final ActionDispatcher dispatcher;

    @Override
    @GetMapping("ping")
    public PingResponse ping(RequestContext context) {
        return dispatcher.dispatch(PingAction.class, context);
    }

    @Override
    @PostMapping("echo")
    public String echo(@RequestBody String input, RequestContext context) {
        return dispatcher.dispatch(EchoAction.class, input, context);
    }

    @PostMapping("check")
    public String check(@Valid @RequestBody InputData input) {
        return input.getUsername();
    }

}
