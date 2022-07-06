package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.util.Array;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class ConstantArray<T> implements Array<T> {

    private final Object[] values;

    private ConstantArray(Object[] values, boolean copy) {
        if (copy) {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, (this.values), 0, values.length);
        } else {
            this.values = values;
        }
    }

    @SuppressWarnings("unchecked")
    public ConstantArray(T... values) {
        this(Objects.requireNonNull(values), true);
    }

    public ConstantArray(List<? extends T> values) {
        this.values = values.toArray();
    }

    public static <T> ConstantArray<T> from(Array<T> array) {
        if (array instanceof ConstantArray) {
            return (ConstantArray<T>) array;
        } else {
            Object[] objects = new Object[array.length()];
            int index = 0;
            for (T t : array) {
                objects[index++] = t;
            }
            return new ConstantArray<>(objects, false);
        }
    }

    @Override
    public int length() {
        return values.length;
    }

    public ConstantArray<T> concat(T t) {
        Object[] objects = new Object[values.length + 1];
        System.arraycopy(values, 0, objects, 0, values.length);
        objects[values.length] = t;
        return new ConstantArray<>(objects, false);
    }

    public ConstantArray<T> concat(Collection<? extends T> collection) {
        if (collection == null || collection.isEmpty()) {
            return this;
        }
        Object[] objects = new Object[values.length + collection.size()];
        System.arraycopy(values, 0, objects, 0, values.length);
        int index = values.length;
        for (T t : collection) {
            objects[index++] = t;
        }
        return new ConstantArray<>(objects, false);
    }

    @Override
    public T get(int index) {
        //noinspection unchecked
        return (T) values[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantArray<?> that = (ConstantArray<?>) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
