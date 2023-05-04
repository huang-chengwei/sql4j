package github.alittlehuang.sql4j.dsl.support.meta;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class BasicTypes {

    private static final List<Class<?>> BASIC_TYPES = List.of(
            Byte.class,
            Short.class,
            Integer.class,
            Float.class,
            Long.class,
            Double.class,
            Boolean.class,
            BigDecimal.class,
            Date.class,
            String.class,
            Time.class,
            Timestamp.class
    );

    public static int isBasicType(Object value) {
        return BASIC_TYPES.indexOf(value.getClass());
    }


}
