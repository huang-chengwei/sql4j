package github.alittlehuang.sql4j.dsl.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Array<T> extends Iterable<T> {

    T get(int index);

    default boolean isEmpty() {
        return length() == 0;
    }

    int length();

    @NotNull
    @Override
    default Iterator<T> iterator() {

        return new Iterator<>() {

            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < length();
            }

            @Override
            public T next() {
                if (cursor >= length()) {
                    throw new NoSuchElementException();
                }
                return get(cursor++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }

        };
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    Array<T> join(T item);

    Array<T> join(Iterable<? extends T> items);

    Array<T> offset(int length);

    T[] toArray(Class<T[]> type);
}
