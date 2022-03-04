package io.soffa.foundation.service.data.jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.soffa.foundation.commons.JsonUtil;
import io.soffa.foundation.commons.ObjectFactory;
import io.soffa.foundation.service.data.EntityInfo;
import lombok.SneakyThrows;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Clob;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public final class BeanMapper<T> implements RowMapper<T> {

    private final EntityInfo<T> entityInfo;
    private static final ObjectMapper MAPPER = ObjectFactory.create(true);

    static {
        MAPPER.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
    }

    private BeanMapper(EntityInfo<T> entityInfo) {
        this.entityInfo = entityInfo;
    }

    @SneakyThrows
    @Override
    public T map(ResultSet rs, StatementContext ctx) {
        Map<String, Object> values = new HashMap<>();
        for (Map.Entry<String, String> e : entityInfo.getPropertiesToColumnsMapping().entrySet()) {
            String col = e.getValue();
            Object value = rs.getObject(col);

            if (value == null) {
                values.put(col, null);
                continue;
            }

            String prop = e.getKey();
            Class<?> target = entityInfo.getPropertyType(prop);
            if (value instanceof Clob) {
                value = rs.getString(col);
            }
            if (target.isInstance(value)) {
                values.put(col, value);
                continue;
            }
            boolean convertToMap = entityInfo.isCustomTypeOrMap(prop) && value instanceof String;
            if (convertToMap && JsonUtil.isJson(value.toString())) {
                if (Map.class.isAssignableFrom(target)) {
                    value = JsonUtil.deserializeMap(value.toString());
                }else {
                    value = ObjectFactory.deserializeMap(MAPPER, value.toString());
                }
            }
            values.put(col, value);
        }
        return ObjectFactory.convert(MAPPER, values, entityInfo.getEntityClass());
    }

    public static <T> BeanMapper<T> of(EntityInfo<T> info) {
        return new BeanMapper<>(info);
    }

}
