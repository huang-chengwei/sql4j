package github.alittlehuang.sql4j.dsl.support;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.SortSpecification;
import github.alittlehuang.sql4j.dsl.support.builder.operator.ConstantArray;
import github.alittlehuang.sql4j.dsl.util.Array;

public record QuerySpecification(
        Expression whereClause,
        Array<SortSpecification> sortSpec,
        Array<Expression> groupByClause,
        Array<Expression> selectClause,
        Array<PathExpression> fetchClause
) {

    public static final QuerySpecification EMPTY = new QuerySpecification(
            Expression.TRUE,
            ConstantArray.empty(),
            ConstantArray.empty(),
            ConstantArray.empty(),
            ConstantArray.empty()
    );

    public QuerySpecification updateWhere(Expression whereClause) {
        return new QuerySpecification(whereClause, sortSpec, groupByClause, selectClause, fetchClause);
    }

    public QuerySpecification updateFetch(Array<PathExpression> fetchClause) {
        return new QuerySpecification(whereClause, sortSpec, groupByClause, selectClause, fetchClause);
    }

    public QuerySpecification updateSort(Array<SortSpecification> sortSpec) {
        return new QuerySpecification(whereClause, sortSpec, groupByClause, selectClause, fetchClause);
    }

    public QuerySpecification updateGroupBy(Array<Expression> groupByClause) {
        return new QuerySpecification(whereClause, sortSpec, groupByClause, selectClause, fetchClause);
    }

    public QuerySpecification updateSelect(Array<Expression> selectClause) {
        return new QuerySpecification(whereClause, sortSpec, groupByClause, selectClause, fetchClause);
    }

}
