package github.alittlehuang.sql4j.dsl.builder;

import java.util.List;
import java.util.Objects;

public final class Slice<T> {
    private final List<T> date;
    private final Sliceable sliceable;
    private final long total;

    public Slice(List<T> date, Sliceable sliceable, long total) {
        this.date = date;
        this.sliceable = sliceable;
        this.total = total;
    }

    public List<T> date() {
        return date;
    }

    public Sliceable sliceable() {
        return sliceable;
    }

    public long total() {
        return total;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Slice<?> that = (Slice<?>) obj;
        return Objects.equals(this.date, that.date) &&
               Objects.equals(this.sliceable, that.sliceable) &&
               this.total == that.total;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, sliceable, total);
    }

    @Override
    public String toString() {
        return "Slice[" +
               "date=" + date + ", " +
               "sliceable=" + sliceable + ", " +
               "total=" + total + ']';
    }


}