package github.alittlehuang.sql4j.dsl.support.builder.projection.meta;

import java.lang.reflect.Method;

public interface ProjectionAttribute {

    void setValue(Object target, Object value);

    String getFieldName();


    Method getGetter();
}
