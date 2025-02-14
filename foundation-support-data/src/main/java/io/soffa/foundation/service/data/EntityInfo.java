package io.soffa.foundation.service.data;

import io.soffa.foundation.annotations.Store;
import io.soffa.foundation.annotations.StoreId;
import io.soffa.foundation.commons.ClassUtil;
import io.soffa.foundation.commons.StringUtil;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.errors.ConfigurationException;
import io.soffa.foundation.errors.TechnicalException;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Getter
public class EntityInfo<T> {

    private static final Map<String, EntityInfo<?>> REGISTRY = new HashMap<>();
    private static final Map<String, String> CUSTOM_TABLES = new HashMap<>();
    private final Class<T> entityClass;
    private String tableName;
    private String idProperty;
    private List<String> updatePairs;
    private final Map<String, String> propertiesToColumnsMapping = new HashMap<>();
    private final List<String> columnsEscaped = new ArrayList<>();
    private final List<String> columns = new ArrayList<>();
    private final List<String> valuesPlaceholder = new ArrayList<>();
    private final Map<String, Class<?>> propertiesTypes = new HashMap<>();
    private final Set<String> customTypes = new HashSet<>();

    public EntityInfo(Class<T> entityClass, String tablePrefix) {
        this.entityClass = entityClass;

        tableName = getTableName(entityClass);
        if (TextUtil.isNotEmpty(tablePrefix)) {
            tableName = tablePrefix + tableName;
        }
        tableName = escapeTableName(tableName);
    }

    public void addProperty(String property, Class<?> type) {
        if (propertiesToColumnsMapping.containsKey(property)) {
            return;
        }
        String column = StringUtil.lowerCamelToLowerUnderscore(property);
        columns.add(property);
        propertiesToColumnsMapping.put(property, column);
        columnsEscaped.add(escapeColumnName(column));
        valuesPlaceholder.add(":" + property);
        propertiesTypes.put(property, type);
        propertiesTypes.put(column, type);

        if (!ClassUtil.isBaseType(type) || Map.class.isAssignableFrom(type)) {
            customTypes.add(property);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public boolean isCustomTypeOrMap(String property) {
        return customTypes.contains(property);
    }

    public static <T> void register(Class<T> entityClass, String tablePrefix) {
        EntityInfo<T> info = new EntityInfo<>(entityClass, tablePrefix);
        // Support
        Field[] fields = FieldUtils.getAllFields(entityClass);

        for (Field field : fields) {
            boolean isTransient = field.getAnnotation(Transient.class) != null || Modifier.isTransient(field.getModifiers());
            if (isTransient) {
                continue;
            }
            int modifier = field.getModifiers();
            if (field.getAnnotation(StoreId.class) != null) {
                info.idProperty = field.getName();
            } else if (field.getAnnotation(Id.class) != null) {
                info.idProperty = field.getName();
            } else {
                boolean isDefaultIdField = info.idProperty == null && "id".equals(field.getName());
                if (isDefaultIdField) {
                    info.idProperty = "id";
                }
            }
            if (!Modifier.isAbstract(modifier) && !Modifier.isStatic(modifier)) {
                info.addProperty(field.getName(), field.getType());
            }
        }

        if (info.idProperty == null) {
            throw new TechnicalException("Unable to locate @Id field on class: %s", entityClass.getName());
        }

        info.afterPropertiesSet();

        REGISTRY.put(entityClass.getName(), info);
    }

    public String getIdColumn() {
        return propertiesToColumnsMapping.get(idProperty);
    }

    private void afterPropertiesSet() {
        List<String> updatePairs = new ArrayList<>();
        for (String property : propertiesToColumnsMapping.keySet()) {
            if (!Objects.equals(property, propertiesToColumnsMapping.get(this.idProperty))) {
                updatePairs.add(TextUtil.format("%s = :%s", escapeColumnName(property), property));
            }
        }
        this.updatePairs = updatePairs;
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityInfo<T> get(T model, String tablePrefix) {
        return get((Class<T>) model.getClass(), tablePrefix);
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityInfo<T> get(Class<T> entityClass, String tablePrefix) {
        String keyName = entityClass.getName();
        if (!REGISTRY.containsKey(keyName)) {
            register(entityClass, tablePrefix);
        }
        return (EntityInfo<T>) REGISTRY.get(keyName);
    }

    public static void registerTable(Class<?> clazz, String tableName) {
        CUSTOM_TABLES.put(clazz.getName(), tableName);
    }

    public Class<?> getPropertyType(String key) {
        return propertiesTypes.get(key);
    }
    // -----------------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    private static String escapeColumnName(String value) {
        return TextUtil.format("`%s`", StringUtil.lowerCamelToLowerUnderscore(value));
    }


    private String getTableName(Class<?> entityClass) {
        String tableName = null;
        Store store = entityClass.getAnnotation(Store.class);
        if (store != null) {
            tableName = store.value();
        }
        if (TextUtil.isNotEmpty(tableName)) {
            return tableName;
        }
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            tableName = table.name();
        }
        if (TextUtil.isNotEmpty(tableName)) {
            return tableName;
        }
        if (!CUSTOM_TABLES.isEmpty() && CUSTOM_TABLES.containsKey(entityClass.getName())) {
            return CUSTOM_TABLES.get(entityClass.getName());
        }

        throw new ConfigurationException("No table name specified for %s, use @Store or @Entity", entityClass.getName());
    }

    private static String escapeTableName(String value) {
        return TextUtil.format("`%s`", value);
    }


}
