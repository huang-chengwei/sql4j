package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.expression.Expression;
import lombok.Getter;

@Getter
public class Order {

    private final Expression<?> expression;
    private final boolean desc;


    public Order(Expression<?> expression, boolean desc) {
        this.expression = expression;
        this.desc = desc;
    }


}
