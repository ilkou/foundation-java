package io.soffa.foundation.core.models;

import io.soffa.foundation.annotations.JsonModel;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@JsonModel
public class ObjList<T> extends ObjMeta {

    private final List<T> data;
    private final Boolean hasMore;

    public ObjList(List<T> data) {
        this(data, (Boolean) null);
    }

    public ObjList(List<T> data, Boolean hasMore) {
        super("list");
        this.data = data;
        this.hasMore = hasMore;
    }

    public ObjList(List<T> data, Map<String, Object> metadata) {
        this(data, (Boolean) null);
        super.setMetadata(metadata);
    }
}
