package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.util.Array;

public record OperatorExpression(Array<Expression> expressions, Operator operator) implements Expression {
}
