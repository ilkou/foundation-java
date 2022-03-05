package io.soffa.foundation.core.data.model;

public interface EntityLifecycle {



    default void onInsert() {
        // Default implementation
    }

    default void onUpdate() {
        // Default implementation
    }

    default void onSave() {
        // Default implementation
    }
}
