package github.alittlehuang.sql4j.dsl.support.builder.projection.meta.impl;

import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionAttribute;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

public class DefaultProjectionAttribute implements ProjectionAttribute {

    private final Method getter;
    private final Method setter;
    private final String fieldName;

    public DefaultProjectionAttribute(String fieldName, Method getter, Method setter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.setter = setter;
    }

    @SneakyThrows
    public void setValue(Object target, Object value) {
        if (setter != null) {
            setter.invoke(target, value);
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Method getGetter() {
        return getter;
    }

}
