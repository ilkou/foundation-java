package io.soffa.foundation.service.data.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.soffa.foundation.commons.ObjectFactory;
import io.soffa.foundation.core.models.Model;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

public class ModelArgumentFactory extends AbstractArgumentFactory<Model> {

    private static final ObjectMapper MAPPER = ObjectFactory.create(true);
    static {
        MAPPER.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
    }

    public ModelArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(Model value, ConfigRegistry config) {
        return (position, statement, ctx) -> {
            statement.setString(position, value == null ? null : ObjectFactory.serialize(MAPPER, value));
        };
    }
}
