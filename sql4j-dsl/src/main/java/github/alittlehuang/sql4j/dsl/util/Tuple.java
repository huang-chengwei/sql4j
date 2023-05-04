package github.alittlehuang.sql4j.dsl.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

public interface Tuple extends Iterable<Object> {

    <T> T get(int i);

    Stream<Object> stream();

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return stream().iterator();
    }

    default Object[] toArray() {
        return stream().toArray();
    }

    int length();

    default <T> T first() {
        return get(0);
    }

    default <T> T last() {
        return get(length() - 1);
    }
}
