package github.sql4j.dsl.support;

import github.sql4j.dsl.expression.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Data
public class JsonSerializablePredicate {

    private String[] pathExpression;

    private Expression.Type type;

    private Operator operator;

    private JsonSerializablePredicate[] expressions;

    private Value value;

    public JsonSerializablePredicate() {
    }

    public JsonSerializablePredicate(Expression<?> expression) {
        this.type = expression.getType();
        switch (type) {
            case PATH:
                this.pathExpression = expression.asPathExpression().toArray();
                break;
            case OPERATOR:
                this.operator = expression.getOperator();
                List<? extends Expression<?>> list = expression.getExpressions();
                if (list != null) {
                    expressions = list.stream().map(JsonSerializablePredicate::new).toArray(JsonSerializablePredicate[]::new);
                } else {
                    expressions = null;
                }
                break;
            case CONSTANT:
                this.value = new Value(expression.getValue());
                break;
        }
    }

    public <T> Predicate<T> toPredicate() {
        //noinspection unchecked
        return new PredicateBuilder<>((Expression<Boolean>) toSqlExpression());
    }

    private Expression<?> toSqlExpression() {
        List<Expression<?>> expressions = this.expressions == null ? null : Arrays.stream(this.expressions)
                .map(JsonSerializablePredicate::toSqlExpression)
                .collect(Collectors.toList());
        PathExpression<Object> path = pathExpression == null ? null : new PathExpression<>(pathExpression);
        Object value = this.value == null ? null : this.value.value;
        return new ExpressionImpl<>(path,
                type,
                value,
                operator,
                expressions);
    }

    private static class ExpressionImpl<T> implements Expression<T> {
        final PathExpression<T> pathExpression;
        final Type type;
        final T value;
        final Operator operator;
        final List<Expression<?>> expressions;

        private ExpressionImpl(PathExpression<T> pathExpression,
                               Type type,
                               T value,
                               Operator operator,
                               List<Expression<?>> expressions) {
            this.pathExpression = pathExpression;
            this.type = type;
            this.value = value;
            this.operator = operator;
            this.expressions = expressions;
        }

        @Override
        public PathExpression<T> asPathExpression() {
            return pathExpression;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Operator getOperator() {
            return operator;
        }

        @Override
        public List<? extends Expression<?>> getExpressions() {
            return expressions;
        }
    }

    public static class Value {

        private static List<Class<?>> typeIndexes = Arrays.asList(
                Byte.class,
                Short.class,
                Integer.class,
                Float.class,
                Long.class,
                Double.class,
                Boolean.class,
                BigDecimal.class,
                Date.class,
                String.class,
                Time.class,
                Timestamp.class
        );


        private Object value;

        public Value() {
        }

        public Value(Object value) {
            int type = typeIndexes.indexOf(value.getClass());
            if (type < 0) {
                throw new IllegalArgumentException();
            }
            this.value = value;
        }

        public Object value() {
            return value;
        }

        private <T> T getValue(Class<T> type) {
            //noinspection unchecked
            return type == value.getClass() ? (T) value : null;
        }

        private void setValue(Object value) {
            if (value != null) {
                this.value = value;
            }
        }


        /*
         * Json序列化保留类型
         */


        public void setByteValue(Byte value) {
            setValue(value);
        }

        public Byte getByteValue() {
            return getValue(Byte.class);
        }

        public void setShortValue(Short value) {
            setValue(value);
        }

        public Short getShortValue() {
            return getValue(Short.class);
        }

        public Integer getIntegerValue() {
            return getValue(Integer.class);
        }

        public void setIntegerValue(Integer value) {
            setValue(value);
        }

        public Float getFloutValue() {
            return getValue(Float.class);
        }

        public void setFloutValue(Float value) {
            setValue(value);
        }

        public Long getLongValue() {
            return getValue(Long.class);
        }

        public void setLongValue(Long value) {
            setValue(value);
        }

        public Double getDoubleValue() {
            return getValue(Double.class);
        }

        public void setDoubleValue(Double value) {
            setValue(value);
        }

        public Boolean getBooleanValue() {
            return getValue(Boolean.class);
        }

        public void setBooleanValue(Boolean value) {
            setValue(value);
        }

        public BigDecimal getBigDecimalValue() {
            return getValue(BigDecimal.class);
        }

        public void setBigDecimalValue(BigDecimal value) {
            setValue(value);
        }

        public Date getDateValue() {
            return getValue(Date.class);
        }

        public void setDateValue(Date value) {
            setValue(value);
        }

        public String getStringValue() {
            return getValue(String.class);
        }

        public void setStringValue(String value) {
            setValue(value);
        }

        public Time getTimeValue() {
            return getValue(Time.class);
        }

        public void setTimeValue(Time value) {
            setValue(value);
        }

        public Timestamp getTimestampValue() {
            return getValue(Timestamp.class);
        }

        public void setTimestampValue(Timestamp value) {
            setValue(value);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value1 = (Value) o;

            return Objects.equals(value, value1.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "value=" + value +
                    '}';
        }
    }
}

