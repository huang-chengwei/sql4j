package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.*;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Predicate;
import github.sql4j.dsl.expression.path.Entity;
import github.sql4j.dsl.expression.path.PathBuilder;
import github.sql4j.dsl.expression.path.attribute.*;
import github.sql4j.dsl.support.builder.criteria.PredicateCombinerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class WhereImpl<T, BUILDER> implements Where<T, BUILDER> {

    private final Function<Expression<Boolean>, BUILDER> mapper;
    private final boolean negate;

    public WhereImpl(Function<Expression<Boolean>, BUILDER> mapper) {
        this.mapper = mapper;
        negate = false;
    }

    public WhereImpl(Function<Expression<Boolean>, BUILDER> mapper, boolean negate) {
        this.mapper = mapper;
        this.negate = negate;
    }

    @NotNull
    private PredicateCombinerImpl<T, BUILDER> getBuilder() {
        return new PredicateCombinerImpl<>(null, mapper);
    }


    @Override
    public <U extends Entity> PathBuilder<T, U, BUILDER> where(EntityAttribute<T, U> attribute) {
        return negate ? getBuilder().andNot(attribute) : getBuilder().and(attribute);
    }

    @Override
    public <U> BasePredicate<T, U, BUILDER> where(Attribute<T, U> attribute) {
        return negate ? getBuilder().andNot(attribute) :  getBuilder().and(attribute);
    }

    @Override
    public <U extends Number & Comparable<?>> NumberPredicate<T, U, BUILDER>
    where(NumberAttribute<T, U> attribute) {
        return negate ? getBuilder().andNot(attribute) : getBuilder().and(attribute);
    }

    @Override
    public <U extends Comparable<?>> ComparablePredicate<T, U, BUILDER>
    where(ComparableAttribute<T, U> attribute) {
        return negate ? getBuilder().andNot(attribute) : getBuilder().and(attribute);
    }

    @Override
    public StringPredicate<T, BUILDER> where(StringAttribute<T> attribute) {
        return negate ? getBuilder().andNot(attribute) : getBuilder().and(attribute);
    }

    @Override
    public BUILDER where(Predicate<T> predicate) {
        return getBuilder().and(predicate);
    }

    @Override
    public Where<T, BUILDER> not() {
        return new WhereImpl<>(mapper, !negate);
    }
}
