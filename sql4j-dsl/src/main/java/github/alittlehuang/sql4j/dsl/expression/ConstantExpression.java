package github.alittlehuang.sql4j.dsl.expression;

import java.util.Objects;

public final class ConstantExpression implements Expression {
    private final Object value;

    public ConstantExpression(Object value) {
        this.value = value;
    }

    public Object value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ConstantExpression that = (ConstantExpression) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ConstantExpression[" +
               "value=" + value + ']';
    }

}
