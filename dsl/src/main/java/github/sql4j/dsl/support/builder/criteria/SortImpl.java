package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.SortAction;
import github.sql4j.dsl.builder.Sort;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.expression.path.attribute.ComparableAttribute;
import github.sql4j.dsl.expression.path.attribute.NumberAttribute;
import github.sql4j.dsl.expression.path.attribute.StringAttribute;
import github.sql4j.dsl.support.builder.component.ConstantArray;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SortImpl<T, BUILDER> implements Sort<T, BUILDER> {

    private final Array<Order> values;
    private final Function<Array<Order>, BUILDER> mapper;

    public SortImpl(Array<Order> values,
                    Function<Array<Order>, BUILDER> mapper) {
        this.values = values;
        this.mapper = mapper;
    }

    @Override
    public <U extends Number & Comparable<?>> SortAction<BUILDER> orderBy(NumberAttribute<T, U> attribute) {
        return orderBy((Attribute<?, ?>) attribute);
    }

    @Override
    public <U extends Comparable<?>> SortAction<BUILDER> orderBy(ComparableAttribute<T, U> attribute) {
        return orderBy((Attribute<?, ?>) attribute);
    }

    @Override
    public SortAction<BUILDER> orderBy(StringAttribute<T> attribute) {
        return orderBy((Attribute<?, ?>) attribute);
    }

    public SortAction<BUILDER> orderBy(Attribute<?, ?> attribute) {
        return new SortActionImpl<>(AttributePath.exchange(attribute), ((expression, desc) -> {
            Order order = new Order(expression, desc);
            Array<Order> orders = values == null ? new ConstantArray<>(order) : ConstantArray.from(values).concat(order);
            return mapper.apply(orders);
        }));
    }

    public static class SortActionImpl<T> implements SortAction<T> {

        private final SqlExpression<?> expression;
        private final BiFunction<SqlExpression<?>, Boolean, T> mapper;

        public SortActionImpl(SqlExpression<?> expression,
                              BiFunction<SqlExpression<?>, Boolean, T> mapper) {
            this.expression = expression;
            this.mapper = mapper;
        }

        @Override
        public T asc() {
            return mapper.apply(expression, false);
        }

        @Override
        public T desc() {
            return mapper.apply(expression, true);
        }
    }

}
