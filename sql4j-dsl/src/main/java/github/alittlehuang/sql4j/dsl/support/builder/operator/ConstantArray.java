package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.util.Array;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConstantArray<T> implements Array<T> {

    public static final ConstantArray<?> EMPTY = new ConstantArray<>(new Object[0], false);

    protected final Object[] values;

    public ConstantArray(T value) {
        this(new Object[]{value}, false);
    }

    public ConstantArray(T[] values) {
        this(values, true);
    }

    protected ConstantArray(Object[] values, boolean copy) {
        if (copy) {
            this.values = Arrays.copyOf(values, values.length);
        } else {
            this.values = values;
        }
    }

    public ConstantArray(Iterable<? extends T> values) {
        this.values = values instanceof Collection<?> c
                ? c.toArray()
                : StreamSupport.stream(values.spliterator(), false).toArray();
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

    public ConstantArray<T> replace(int index, UnaryOperator<T> operator) {
        T t = get(index);
        T apply = operator.apply(t);
        if (apply == t) {
            return this;
        } else {
            Object[] objects = Arrays.copyOf(this.values, this.values.length);
            objects[index] = apply;
            return new ConstantArray<>(objects, false);
        }
    }

    public ConstantArray<T> join(T t) {
        Object[] objects = doJoin(t);
        return new ConstantArray<>(objects, false);
    }

    @NotNull
    protected Object[] doJoin(T t) {
        Object[] objects = new Object[values.length + 1];
        System.arraycopy(values, 0, objects, 0, values.length);
        objects[values.length] = t;
        return objects;
    }

    public ConstantArray<T> join(Iterable<? extends T> items) {
        Object[] objects = doJoin(items);
        return new ConstantArray<>(objects, false);
    }

    @NotNull
    protected Object[] doJoin(Iterable<? extends T> items) {
        Objects.requireNonNull(items);
        Stream<? extends T> stream = StreamSupport
                .stream(items.spliterator(), false);
        return Stream.concat(this.stream(), stream).toArray();
    }

    @Override
    public T get(int index) {
        return TypeCastUtil.cast(values[index]);
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

    public static <T> ConstantArray<T> empty() {
        return TypeCastUtil.cast(EMPTY);
    }

    @Override
    public ConstantArray<T> offset(int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        if (length > values.length) {
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        if (length == values.length) {
            return this;
        }
        return new ConstantArray<>(Arrays.copyOf(values, length), false);
    }

    @Override
    public T[] toArray(Class<T[]> type) {
        return Arrays.copyOf(values, values.length, type);
    }


}
