package github.alittlehuang.sql4j.dsl.builder;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Mapper<T extends Mapper<T>> {

    default <R> R map(@NotNull Function<? super Mapper<T>, ? extends R> function) {
        return function.apply(this);
    }

}
