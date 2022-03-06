package io.soffa.foundation.service.data.jdbi;

import io.soffa.foundation.commons.ClassUtil;
import io.soffa.foundation.commons.Mappers;
import io.soffa.foundation.core.models.VO;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.Optional;
import java.util.function.Function;

public class ObjectArgumentFactory implements ArgumentFactory.Preparable {


    @Override
    public Optional<Function<Object, Argument>> prepare(Type type, ConfigRegistry config) {
        if (ClassUtil.isBaseType(type)) {
            return Optional.empty();
        }
        return Optional.of(this::build);
    }

    private Argument build(Object value) {
        return (position, statement, ctx) -> {
            if (value == null) {
                statement.setNull(position, Types.VARCHAR);
            } else if (value instanceof VO) {
                statement.setString(position, ((VO) value).getValue());
            } else {
                String serialized = Mappers.JSON_FULLACCESS_SNAKE.serialize( value);
                statement.setString(position, serialized);
            }
        };
    }
}
