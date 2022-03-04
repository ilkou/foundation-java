package io.soffa.foundation.service.data;

import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.data.DB;
import io.soffa.foundation.core.data.DataStore;
import io.soffa.foundation.core.data.EntityRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleEntityRepository<E> implements EntityRepository<E> {

    private final DataStore ds;
    private final Class<E> entityClass;

    public SimpleEntityRepository(DataStore ds, Class<E> entityClass) {
        this(ds, entityClass, null);
    }

    public SimpleEntityRepository(DataStore ds, Class<E> entityClass, String tableName) {
        this.ds = ds;
        this.entityClass = entityClass;
        if (TextUtil.isNotEmpty(tableName)) {
            EntityInfo.registerTable(entityClass, tableName);
        }
    }

    public SimpleEntityRepository(DB db, Class<E> entityClass) {
        this(new SimpleDataStore(db), entityClass, null);
    }

    public SimpleEntityRepository(DB db, Class<E> entityClass, String tableName) {
        this(new SimpleDataStore(db), entityClass, tableName);
    }

    @Override
    public long count() {
        return ds.count(entityClass);
    }

    @Override
    public long count(String where, Map<String, Object> binding) {
        return ds.count(entityClass, where, binding);
    }

    @Override
    public List<E> findAll() {
        return ds.findAll(entityClass);
    }

    @Override
    public List<E> find(String where, Map<String, Object> binding) {
        return ds.find(entityClass, where, binding);
    }

    @Override
    public Optional<E> get(String where, Map<String, Object> binding) {
        return ds.get(entityClass, where, binding);
    }

    @Override
    public Optional<E> findById(Object id) {
        return ds.findById(entityClass, id);
    }

    @Override
    public E insert(E entity) {
        return ds.insert(entity);
    }

    @Override
    public E update(E entity) {
        return ds.update(entity);
    }

    @Override
    public int delete(E entity) {
        return ds.delete(entity);
    }

    @Override
    public int delete(String where, Map<String, Object> binding) {
        return ds.delete(entityClass, where, binding);
    }

}
