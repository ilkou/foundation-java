package io.soffa.foundation.commons.lang;


import io.soffa.foundation.commons.StringUtil;
import io.soffa.foundation.commons.TextUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@NoArgsConstructor
@Getter
public class Prefix {

    private String value;

    public Prefix(@NonNull String value) {
        this.value = TextUtil.trimToEmpty(value);
    }

    public void append(@NonNull String value) {
        this.value = StringUtil.prefix(value, this.value);
    }

    public void appendIf(boolean test, @NonNull String value) {
        if (test) {
            append(value);
        }
    }


}
