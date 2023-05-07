package github.alittlehuang.sql4j.dsl.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BasicTypes {

    private static final Set<Class<?>> BASIC_TYPES = new HashSet<>(Arrays.asList(
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
            Timestamp.class,

            Boolean.TYPE,
            Character.TYPE,
            Byte.TYPE,
            Short.TYPE,
            Integer.TYPE,
            Long.TYPE,
            Float.TYPE,
            Double.TYPE
    ));

    public static boolean isBasicType(Object value) {
        return BASIC_TYPES.contains(value.getClass());
    }

    public static boolean isBasicType(Class<?> value) {
        return BASIC_TYPES.contains(value);
    }

}
