package github.alittlehuang.sql4j.dsl.support.builder.operator;

import java.util.function.Function;

public record DataAction<T, R, U>(T data, Function<R, U> builder) {

    public <X> DataAction<X, R, U> data(Function<T, X> mapper) {
        return new DataAction<>(mapper.apply(data), builder);
    }

}
