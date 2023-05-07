package github.alittlehuang.sql4j.dsl.support.builder.operator;

import java.util.Objects;
import java.util.function.Function;

public final class DataAction<T, R, U> {
    private final T data;
    private final Function<R, U> builder;

    public DataAction(T data, Function<R, U> builder) {
        this.data = data;
        this.builder = builder;
    }

    public <X> DataAction<X, R, U> data(Function<T, X> mapper) {
        return new DataAction<>(mapper.apply(data), builder);
    }

    public T data() {
        return data;
    }

    public Function<R, U> builder() {
        return builder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        DataAction<?, ?, ?> that = (DataAction<?, ?, ?>) obj;
        return Objects.equals(this.data, that.data) &&
               Objects.equals(this.builder, that.builder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, builder);
    }

    @Override
    public String toString() {
        return "DataAction[" +
               "data=" + data + ", " +
               "builder=" + builder + ']';
    }


}
