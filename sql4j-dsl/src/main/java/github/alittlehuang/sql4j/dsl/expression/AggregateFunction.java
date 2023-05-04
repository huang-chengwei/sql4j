package github.alittlehuang.sql4j.dsl.expression;

public enum AggregateFunction {

    MIN(Operator.MIN),
    MAX(Operator.MAX),
    COUNT(Operator.COUNT),
    AVG(Operator.AVG),
    SUM(Operator.SUM);
    private final Operator operator;

    AggregateFunction(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }
}
