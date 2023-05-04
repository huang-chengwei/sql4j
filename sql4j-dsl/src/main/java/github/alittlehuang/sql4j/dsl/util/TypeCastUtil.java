package github.alittlehuang.sql4j.dsl.util;

public class TypeCastUtil {

    public static <T> T cast(Object o) {
        // noinspection unchecked
        return (T) o;
    }

}
