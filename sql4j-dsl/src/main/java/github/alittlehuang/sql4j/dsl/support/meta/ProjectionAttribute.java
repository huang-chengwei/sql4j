package github.alittlehuang.sql4j.dsl.support.meta;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
public class ProjectionAttribute {

    private final Field field;
    private final Method getter;
    private final Method setter;
    private final String fieldName;

    public ProjectionAttribute(String fieldName, Field field, Method getter, Method setter) {
        this.fieldName = fieldName;
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

    @SneakyThrows
    public void setValue(Object target, Object value) {
        if (setter != null) {
            setter.invoke(target, value);
        }
    }

    @SneakyThrows
    public Object getValue(Object target) {
        return getter.invoke(target);
    }

    public Class<?> getJavaType() {
        return getter.getReturnType();
    }

    public String getFieldName() {
        return fieldName;
    }

}
