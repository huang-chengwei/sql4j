package github.sql4j.dsl.support;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;

public interface StructuredQuery {

    Expression<Boolean> where();

    Array<Order> orderBy();

    Array<Expression<?>> groupBy();

    Array<Expression<?>> select();

    Array<PathExpression<?>> fetch();

}
