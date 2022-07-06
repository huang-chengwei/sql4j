package jdbc.mysql;

import github.sql4j.dsl.expression.Operator;
import lombok.Getter;

@Getter
public class JdbcOperator {

    public static final JdbcOperator NOT = new JdbcOperator("not", 10);

    public static final JdbcOperator AND = new JdbcOperator("and", 11);

    public static final JdbcOperator OR = new JdbcOperator("or", 13);
    public static final JdbcOperator GT = new JdbcOperator(">", 8);
    public static final JdbcOperator EQ = new JdbcOperator("=", 8);
    public static final JdbcOperator NE = new JdbcOperator("<>", 8);
    public static final JdbcOperator GE = new JdbcOperator(">=", 8);
    public static final JdbcOperator LT = new JdbcOperator("<", 8);
    public static final JdbcOperator LE = new JdbcOperator("<=", 8);
    public static final JdbcOperator LIKE = new JdbcOperator("like", 8);

    public static final JdbcOperator ISNULL = new JdbcOperator("isnull", 0);
    public static final JdbcOperator IN = new JdbcOperator("in", 0);
    public static final JdbcOperator BETWEEN = new JdbcOperator("between", 8);

    public static final JdbcOperator LOWER = new JdbcOperator("lower", 0);
    public static final JdbcOperator UPPER = new JdbcOperator("upper", 0);
    public static final JdbcOperator SUBSTRING = new JdbcOperator("substring", 0);
    public static final JdbcOperator TRIM = new JdbcOperator("trim", 0);
    public static final JdbcOperator LENGTH = new JdbcOperator("length", 0);

    public static final JdbcOperator ADD = new JdbcOperator("+", 4);
    public static final JdbcOperator SUBTRACT = new JdbcOperator("-", 4);
    public static final JdbcOperator MULTIPLY = new JdbcOperator("*", 3);
    public static final JdbcOperator DIVIDE = new JdbcOperator("/", 3);
    public static final JdbcOperator MOD = new JdbcOperator("mod", 3);
    public static final JdbcOperator NULLIF = new JdbcOperator("nullif", 0);
    public static final JdbcOperator IF_NULL = new JdbcOperator("ifnull", 0);


    //aggregate function
    public static final JdbcOperator MIN = new JdbcOperator("min", 0);

    public static final JdbcOperator MAX = new JdbcOperator("max", 0);
    public static final JdbcOperator COUNT = new JdbcOperator("count", 0);
    public static final JdbcOperator AVG = new JdbcOperator("avg", 0);
    public static final JdbcOperator SUM = new JdbcOperator("sum", 0);

    public final String sign;
    public final int precedence;

    JdbcOperator(String sign, int priority) {
        this.sign = sign;
        this.precedence = priority;
    }

    @Override
    public String toString() {
        return sign;
    }


    public static JdbcOperator of(Operator operator) {
        switch (operator) {
            case NOT:
                return NOT;
            case AND:
                return AND;
            case OR:
                return OR;
            case GT:
                return GT;
            case EQ:
                return EQ;
            case NE:
                return NE;
            case GE:
                return GE;
            case LT:
                return LT;
            case LE:
                return LE;
            case LIKE:
                return LIKE;
            case ISNULL:
                return ISNULL;
            case IN:
                return IN;
            case BETWEEN:
                return BETWEEN;
            case LOWER:
                return LOWER;
            case UPPER:
                return UPPER;
            case SUBSTRING:
                return SUBSTRING;
            case TRIM:
                return TRIM;
            case LENGTH:
                return LENGTH;
            case ADD:
                return ADD;
            case SUBTRACT:
                return SUBTRACT;
            case MULTIPLY:
                return MULTIPLY;
            case DIVIDE:
                return DIVIDE;
            case MOD:
                return MOD;
            case NULLIF:
                return NULLIF;
            case IF_NULL:
                return IF_NULL;
            case MIN:
                return MIN;
            case MAX:
                return MAX;
            case COUNT:
                return COUNT;
            case AVG:
                return AVG;
            case SUM:
                return SUM;
            default:
                throw new UnsupportedOperationException(operator.name());
        }

    }
}