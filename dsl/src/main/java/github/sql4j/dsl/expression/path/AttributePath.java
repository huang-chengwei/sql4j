package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.expression.path.attribute.*;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Objects;

public class AttributePath<T, R>
        extends PathExpression<R>
        implements Attribute<T, R>, Expression<R> {

    public AttributePath(String... path) {
        super(path);
    }

    public static <T, R extends Entity> EntityPath<T, R> exchange(EntityAttribute<T, R> attribute) {
        if (attribute instanceof EntityPath) {
            return (EntityPath<T, R>) attribute;
        }
        return new EntityPath<>(getAttributeName(attribute));
    }

    public static <T, R> AttributePath<T, R> exchange(Attribute<T, R> attribute) {
        if (attribute instanceof AttributePath) {
            return (AttributePath<T, R>) attribute;
        }
        return new AttributePath<>(getAttributeName(attribute));
    }

    public static <R extends Number & Comparable<?>, T> NumberAttribute<T, R>
    fromNumberAttributeBridge(NumberAttribute<T, R> attribute) {
        if (attribute instanceof AttributePath) {
            return attribute;
        }
        return new NumberPath<>(getAttributeName(attribute));
    }


    public static <T> StringAttribute<T> fromStringAttributeBridge(StringAttribute<T> attribute) {
        if (attribute instanceof AttributePath) {
            return attribute;
        }
        return new StringPath<>(getAttributeName(attribute));
    }

    public static <R extends Comparable<?>, T> ComparableAttribute<T, R>
    fromComparableAttributeBridge(ComparableAttribute<T, R> attribute) {
        if (attribute instanceof AttributePath) {
            return attribute;
        }
        return new ComparablePath<>(getAttributeName(attribute));
    }

    public static String toAttrName(String getterName) {
        StringBuilder builder = null;
        if (getterName != null) {
            if (getterName.length() > 3 && getterName.startsWith("get")) {
                builder = new StringBuilder(getterName.substring(3));
            } else if (getterName.length() > 2 && getterName.startsWith("is")) {
                builder = new StringBuilder(getterName.substring(2));
            }
        }
        Objects.requireNonNull(builder, "the function is not getters");
        if (builder.length() == 1) {
            return builder.toString().toLowerCase();
        }
        if (Character.isUpperCase(builder.charAt(1))) {
            return builder.toString();
        }
        builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
        return builder.toString();
    }

    public static String getLambdaMethodName(Serializable lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            return serializedLambda.getImplMethodName();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String getAttributeName(Attribute<?, ?> attribute) {
        return toAttrName(getLambdaMethodName(attribute));
    }

    @Override
    public R map(T t) {
        throw new UnsupportedOperationException();
    }


    public <V extends Entity> EntityPath<T, V> map(EntityAttribute<R, V> attribute) {
        return new EntityPath<>(pathTo(attribute));
    }

    public <V extends Number & Comparable<?>> NumberPath<T, V> map(NumberAttribute<R, V> attribute) {
        return new NumberPath<>(pathTo(attribute));
    }

    public <V extends Comparable<?>> ComparablePath<T, V> map(ComparableAttribute<R, V> attribute) {
        return new ComparablePath<>(pathTo(attribute));
    }

    public StringPath<T> map(StringAttribute<R> attribute) {
        return new StringPath<>(pathTo(attribute));
    }

    public BooleanPath<T> map(BooleanAttribute<R> attribute) {
        return new BooleanPath<>(pathTo(attribute));
    }

    public <V> AttributePath<T, V> map(Attribute<R, V> attribute) {
        return new AttributePath<>(pathTo(attribute));
    }

    private String[] pathTo(Attribute<?, ?> attribute) {
        String[] path = new String[length + 1];
        this.arraycopy(0, path, 0, length);
        path[this.length] = getAttributeName(attribute);
        return path;
    }

}
