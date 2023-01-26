package github.sql4j.dsl.expression;

public interface OperatorExpression<T> extends Expression<T> {

    default PathExpression<T> asPathExpression() {
        throw new UnsupportedOperationException();
    }

    default Type getType() {
        return Type.OPERATOR;
    }

    default T getValue() {
        throw new UnsupportedOperationException();
    }

}
