package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;

public class CriteriaQueryImpl implements StructuredQuery {

    public static final CriteriaQueryImpl EMPTY = new CriteriaQueryImpl(null, null, null, null, null);


    private final Expression<Boolean> restriction;
    private final Array<Order> orderList;
    private final Array<Expression<?>> groupList;
    private final Array<Expression<?>> selection;
    private final Array<PathExpression<?>> fetch;

    public CriteriaQueryImpl(Expression<Boolean> restriction,
                             Array<Order> orderList,
                             Array<Expression<?>> groupList,
                             Array<Expression<?>> selection,
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

    public CriteriaQueryImpl updateRestriction(Expression<Boolean> restriction) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateOrderList(Array<Order> orderList) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateGroupList(Array<Expression<?>> groupList) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateSelection(Array<Expression<?>> selection) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    public CriteriaQueryImpl updateFetch(Array<PathExpression<?>> fetch) {
        return new CriteriaQueryImpl(restriction, orderList, groupList, selection, fetch);
    }

    @Override
    public Expression<Boolean> where() {
        return restriction;
    }

    @Override
    public Array<Order> orderBy() {
        return orderList;
    }

    @Override
    public Array<Expression<?>> groupBy() {
        return groupList;
    }

    @Override
    public Array<Expression<?>> select() {
        return selection;
    }

    @Override
    public Array<PathExpression<?>> fetch() {
        return fetch;
    }


}
