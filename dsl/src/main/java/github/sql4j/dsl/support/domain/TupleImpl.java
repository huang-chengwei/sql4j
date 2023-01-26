package github.sql4j.dsl.support.domain;

import github.sql4j.dsl.util.Tuple;

import java.util.Arrays;
import java.util.stream.Stream;

public class TupleImpl implements Tuple {
    private final Object[] data;

    public TupleImpl(Object[] data) {
        this.data = data;
    }


    @Override
    public <T> T get(int i) {
        //noinspection unchecked
        return (T) data[i];
    }

    @Override
    public Stream<Object> stream() {
        return Arrays.stream(data);
    }

    @Override
    public int length() {
        return data.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TupleImpl)) {
            return false;
        }
        TupleImpl tuple = (TupleImpl) o;
        return Arrays.equals(data, tuple.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
