package io.soffa.foundation.service.core.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.AppConfig;
import io.soffa.foundation.core.Operation;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.context.DefaultRequestContext;
import io.soffa.foundation.core.metrics.MetricsRegistry;
import io.soffa.foundation.core.openapi.OpenApiBuilder;
import io.soffa.foundation.core.operation.OperationsMapping;
import io.soffa.foundation.core.security.AuthManager;
import io.soffa.foundation.errors.ConfigurationException;
import io.soffa.foundation.errors.ErrorUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Configuration
@OpenAPIDefinition
public class PlatformBeansFactory {

    @Bean
    public RestTemplate createDefaultRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OperationsMapping createOperationsMapping(Set<Operation<?, ?>> operations) {
        return new OperationsMapping(operations);
    }

    @Bean
    @ConditionalOnMissingBean(AuthManager.class)
    public AuthManager createDefaultAuthManager() {
        return new NoopAuthManager();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(AppConfig appConfig,
                                                             @Value("${spring.application.name}") String serviceId) {
        if (TextUtil.isNotEmpty(appConfig.getPkg())) {
            Logger.setRelevantPackage(appConfig.getPkg());
            ErrorUtil.setRelevantPackage(appConfig.getPkg());
        }
        DefaultRequestContext.setServiceName(serviceId);
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app")
    public AppConfig createAppConfig(@Value("${spring.application.name}") String applicationName,
                                     @Value("${app.package:auto}") String packageName,
                                     ApplicationContext context) {
        DefaultRequestContext.setServiceName(applicationName);
        AppConfig conf = new AppConfig(applicationName);
        conf.setPkg(packageName);
        if ("auto".equalsIgnoreCase(packageName)) {
            Map<String, Object> annotatedBeans = context.getBeansWithAnnotation(SpringBootApplication.class);
            if (!annotatedBeans.isEmpty()) {
                conf.setPkg(annotatedBeans.values().iterator().next().getClass().getPackage().getName());
            } else {
                throw new ConfigurationException("app.package definition is missing, should point to your main package");
            }
        }

        return conf;
    }


    @Bean
    @Primary
    public HttpFirewall looseHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setUnsafeAllowAnyHttpMethod(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }


    @Bean
    public OpenAPI createOpenAPI(AppConfig appConfig) {
        //SpringDocUtils.getConfig().addRequestWrapperToIgnore(RequestContext.class);
        OpenApiBuilder builder = new OpenApiBuilder(appConfig.getOpenapi());
        return builder.build();
    }

    @Bean
    @Primary
    public MetricsRegistry createMetricsRegistry(MeterRegistry registry) {
        return new MetricsRegistryImpl(registry);
    }

}
