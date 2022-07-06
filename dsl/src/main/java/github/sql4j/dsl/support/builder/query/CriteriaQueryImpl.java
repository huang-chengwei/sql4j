package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;

public class CriteriaQueryImpl implements StructuredQuery {

    public static final CriteriaQueryImpl EMPTY = new CriteriaQueryImpl(null, null, null, null, null);


    private final SqlExpression<Boolean> restriction;
    private final Array<Order> orderList;
    private final Array<SqlExpression<?>> groupList;
    private final Array<SqlExpression<?>> selection;
    private final Array<PathExpression<?>> fetch;

    public CriteriaQueryImpl(SqlExpression<Boolean> restriction,
                             Array<Order> orderList,
                             Array<SqlExpression<?>> groupList,
                             Array<SqlExpression<?>> selection,
                             Array<PathExpression<?>> fetch) {
        this.restriction = restriction;
        this.orderList = orderList;
        this.groupList = groupList;
        this.selection = selection;
        this.fetch = fetch;
    }

    public static CriteriaQueryImpl from(StructuredQuery criteriaQuery) {
        if (criteriaQuery instanceof CriteriaQueryImpl) {
            return (CriteriaQueryImpl) criteriaQuery;
        } else if (criteriaQuery == null) {
            return EMPTY;
        }
        return new CriteriaQueryImpl(
                criteriaQuery.where(),
                criteriaQuery.orderBy(),
                criteriaQuery.groupBy(),
                criteriaQuery.select(),
                criteriaQuery.fetch()
        );
    }

    public CriteriaQueryImpl updateRestriction(SqlExpression<Boolean> restriction) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateOrderList(Array<Order> orderList) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateGroupList(Array<SqlExpression<?>> groupList) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateSelection(Array<SqlExpression<?>> selection) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateFetch(Array<PathExpression<?>> fetch) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    @Override
    public SqlExpression<Boolean> where() {
        return restriction;
    }

    @Override
    public Array<Order> orderBy() {
        return orderList;
    }

    @Override
    public Array<SqlExpression<?>> groupBy() {
        return groupList;
    }

    @Override
    public Array<SqlExpression<?>> select() {
        return selection;
    }

    @Override
    public Array<PathExpression<?>> fetch() {
        return fetch;
    }


}
