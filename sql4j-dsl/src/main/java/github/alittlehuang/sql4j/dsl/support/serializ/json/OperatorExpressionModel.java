package github.alittlehuang.sql4j.dsl.support.serializ.json;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.ExpressionSupplier;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.OperatorExpression;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorExpressionModel implements ExpressionSupplier {

    List<SerializableExpression> es;

    Operator o;

    public OperatorExpressionModel(OperatorExpression expression) {
        es = expression.expressions().stream()
                .map(SerializableExpression::new)
                .toList();
        o = expression.operator();
    }

    @Override
    public Expression expression() {
        return Expression.of(es.stream().map(ExpressionSupplier::expression).toList(), o);
    }


}
