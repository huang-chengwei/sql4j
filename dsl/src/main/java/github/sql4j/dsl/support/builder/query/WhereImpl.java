package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.*;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.Predicate;
import github.sql4j.dsl.expression.path.Entity;
import github.sql4j.dsl.expression.path.PathBuilder;
import github.sql4j.dsl.expression.path.attribute.*;
import github.sql4j.dsl.support.builder.criteria.PredicateAssemblerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class WhereImpl<T, BUILDER> implements Where<T, BUILDER> {

    private final Function<SqlExpression<Boolean>, BUILDER> mapper;

    public WhereImpl(Function<SqlExpression<Boolean>, BUILDER> mapper) {
        this.mapper = mapper;
    }

    @NotNull
    private PredicateAssemblerImpl<T, BUILDER> getBuilder() {
        return new PredicateAssemblerImpl<>(null, mapper);
    }


    @Override
    public <U extends Entity> PathBuilder<T, U, BUILDER> where(EntityAttribute<T, U> attribute) {
        return getBuilder().and(attribute);
    }

    @Override
    public <U> BasePredicate<T, U, BUILDER> where(Attribute<T, U> attribute) {
        return getBuilder().and(attribute);
    }

    @Override
    public <U extends Number & Comparable<?>> NumberPredicate<T, U, BUILDER>
    where(NumberAttribute<T, U> attribute) {
        return getBuilder().and(attribute);
    }

    @Override
    public <U extends Comparable<?>> ComparablePredicate<T, U, BUILDER>
    where(ComparableAttribute<T, U> attribute) {
        return getBuilder().and(attribute);
    }

    @Override
    public StringPredicate<T, BUILDER> where(StringAttribute<T> attribute) {
        return getBuilder().and(attribute);
    }

    @Override
    public <U extends Entity> PathBuilder<T, U, BUILDER> whereNot(EntityAttribute<T, U> attribute) {
        return getBuilder().andNot(attribute);
    }

    @Override
    public <U> BasePredicate<T, U, BUILDER> whereNot(Attribute<T, U> attribute) {
        return getBuilder().andNot(attribute);
    }

    @Override
    public <U extends Number & Comparable<?>> NumberPredicate<T, U, BUILDER> whereNot(NumberAttribute<T, U> attribute) {
        return getBuilder().andNot(attribute);
    }

    @Override
    public <U extends Comparable<?>> ComparablePredicate<T, U, BUILDER> whereNot(ComparableAttribute<T, U> attribute) {
        return getBuilder().andNot(attribute);
    }

    @Override
    public StringPredicate<T, BUILDER> whereNot(StringAttribute<T> attribute) {
        return getBuilder().andNot(attribute);
    }

    @Override
    public BUILDER where(Predicate<T> predicate) {
        return getBuilder().and(predicate);
    }
}
