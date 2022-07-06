package github.sql4j.dsl.util;

/**
 * @author ALittleHuang
 */
@SuppressWarnings("unused")
public abstract class Assert {
    private Assert() {
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

}

