package github.alittlehuang.sql4j.dsl.expression;

import java.util.Objects;

public final class SortSpecification {
    private final Expression expression;
    private final boolean desc;

    public SortSpecification(Expression expression, boolean desc) {
        this.expression = expression;
        this.desc = desc;
    }

    public Expression expression() {
        return expression;
    }

    public boolean desc() {
        return desc;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        SortSpecification that = (SortSpecification) obj;
        return Objects.equals(this.expression, that.expression) &&
               this.desc == that.desc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, desc);
    }

    @Override
    public String toString() {
        return "SortSpecification[" +
               "expression=" + expression + ", " +
               "desc=" + desc + ']';
    }

}
