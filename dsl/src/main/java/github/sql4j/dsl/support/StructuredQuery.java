package github.sql4j.dsl.support;

import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;

public interface StructuredQuery {

    SqlExpression<Boolean> where();

    Array<Order> orderBy();

    Array<SqlExpression<?>> groupBy();

    Array<SqlExpression<?>> select();

    Array<PathExpression<?>> fetch();

}
