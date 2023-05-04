package github.alittlehuang.sql4j.dsl.support.serializ.json;

import github.alittlehuang.sql4j.dsl.expression.*;
import github.alittlehuang.sql4j.dsl.support.builder.operator.DefaultPredicate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SerializableExpression implements ExpressionSupplier {

    Expression expression;

    public <T> Predicate<T> toPredicate() {
        return new DefaultPredicate<>(expression());
    }

    @Override
    public Expression expression() {
        return expression;
    }

    public ConstantExpressionModel getC() {
        return expression instanceof ConstantExpression c ? new ConstantExpressionModel(c.value()) : null;
    }

    public void setC(ConstantExpressionModel serializer) {
        setValue(new ConstantExpression(serializer.value()));
    }

    public OperatorExpressionModel getO() {
        return expression instanceof OperatorExpression c ? new OperatorExpressionModel(c) : null;
    }

    public void setO(OperatorExpressionModel serializer) {
        setValue(serializer);
    }

    public String[] getP() {
        return expression instanceof PathExpression c ? c.toArray() : null;
    }

    public void setP(String[] serializer) {
        setValue(new PathExpression(serializer));
    }

    private void setValue(ExpressionSupplier serializer) {
        if (serializer != null) {
            Expression build = serializer.expression();
            if (build != null) {
                expression = build;
            }
        }
    }

    @Override
    public String toString() {
        return "SerializableExpressionBuilder{" +
               "expression=" + expression +
               '}';
    }
}
