package io.soffa.foundation.core.pubsub;

import io.soffa.foundation.commons.ClassUtil;
import io.soffa.foundation.core.Operation;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.messages.Message;
import org.checkerframework.checker.nullness.qual.NonNull;

import jakarta.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class PubSubClientFactory {

    public static final AtomicLong ASYNC_TIMEOUT_SECONDS = new AtomicLong(30);

    private PubSubClientFactory(){}

    @SuppressWarnings("unchecked")
    public static <I, O, T extends Operation<I, O>> T of(@NotNull Class<T> operationClass, @NonNull String subjet, PubSubClient client) {
        Class<?> returnType = ClassUtil.getClassFromGenericInterface(operationClass, Operation.class, 1);
        return (T) java.lang.reflect.Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{operationClass},
            (proxy, method, args) -> {
                if ("hashCode".equals(method.getName())) {
                    return operationClass.getName().hashCode();
                }
                if ("equals".equals(method.getName())) {
                    return method.equals(args[0]);
                }
                RequestContext context;
                Object input = null;
                boolean hasNoInput = args.length == 1;
                if (hasNoInput) {
                    context = (RequestContext)args[0];
                }else {
                    input = args[0];
                    context = (RequestContext) args[1];
                }
                Message msg = new Message(operationClass.getSimpleName(),input, context);
                return client.request(subjet, msg, returnType).get(ASYNC_TIMEOUT_SECONDS.get(), TimeUnit.SECONDS);
            });
    }

}
