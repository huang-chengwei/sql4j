package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.util.Tuple;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public final class DefaultTuple implements Tuple {
    private final Object[] data;

    public DefaultTuple(Object[] data) {
        this.data = data == null ? new Object[0] : data;
    }


    @Override
    public <T> T get(int i) {
        return TypeCastUtil.cast(data[i]);
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
        if (!(o instanceof DefaultTuple)) {
            return false;
        }
        return Arrays.equals(data, ((DefaultTuple) o).data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
