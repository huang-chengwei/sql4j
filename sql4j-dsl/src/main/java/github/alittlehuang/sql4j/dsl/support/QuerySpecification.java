package github.alittlehuang.sql4j.dsl.support;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.SortSpecification;
import github.alittlehuang.sql4j.dsl.support.builder.operator.ConstantArray;
import github.alittlehuang.sql4j.dsl.util.Array;

import java.util.Objects;

public final class QuerySpecification {

    public static final QuerySpecification EMPTY = new QuerySpecification(
            Expression.TRUE,
            ConstantArray.empty(),
            ConstantArray.empty(),
            ConstantArray.empty(),
            ConstantArray.empty()
    );
    private final Expression whereClause;
    private final Array<SortSpecification> sortSpec;
    private final Array<Expression> groupByClause;
    private final Array<Expression> selectClause;
    private final Array<PathExpression> fetchClause;

    public QuerySpecification(
            Expression whereClause,
            Array<SortSpecification> sortSpec,
            Array<Expression> groupByClause,
            Array<Expression> selectClause,
            Array<PathExpression> fetchClause
    ) {
        this.whereClause = whereClause;
        this.sortSpec = sortSpec;
        this.groupByClause = groupByClause;
        this.selectClause = selectClause;
        this.fetchClause = fetchClause;
    }

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

    public Expression whereClause() {
        return whereClause;
    }

    public Array<SortSpecification> sortSpec() {
        return sortSpec;
    }

    public Array<Expression> groupByClause() {
        return groupByClause;
    }

    public Array<Expression> selectClause() {
        return selectClause;
    }

    public Array<PathExpression> fetchClause() {
        return fetchClause;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        QuerySpecification that = (QuerySpecification) obj;
        return Objects.equals(this.whereClause, that.whereClause) &&
               Objects.equals(this.sortSpec, that.sortSpec) &&
               Objects.equals(this.groupByClause, that.groupByClause) &&
               Objects.equals(this.selectClause, that.selectClause) &&
               Objects.equals(this.fetchClause, that.fetchClause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whereClause, sortSpec, groupByClause, selectClause, fetchClause);
    }

    @Override
    public String toString() {
        return "QuerySpecification[" +
               "whereClause=" + whereClause + ", " +
               "sortSpec=" + sortSpec + ", " +
               "groupByClause=" + groupByClause + ", " +
               "selectClause=" + selectClause + ", " +
               "fetchClause=" + fetchClause + ']';
    }


}
