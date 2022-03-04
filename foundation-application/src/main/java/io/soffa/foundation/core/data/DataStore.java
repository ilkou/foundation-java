package io.soffa.foundation.core.data;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataStore {

    <E> Optional<E> findById(Class<E> entityClass, Object value);

    <E> long count(Class<E> entityClass);

    <E> long count(@NonNull Class<E> entityClass, @NonNull String where, Map<String, Object> binding);

    <E> int delete (@NonNull Class<E> entityClass, @NonNull String where, Map<String, Object> binding);

    <E> E insert(E entity);

    <E> E update(E entity);

    <E> int delete(E entity);

    <E> List<E> find(Class<E> entityClass, String where, Map<String, Object> binding);

    <E> Optional<E> get(Class<E> entityClass, String where, Map<String, Object> binding);

    <E> List<E> findAll(Class<E> entityClass);
}
