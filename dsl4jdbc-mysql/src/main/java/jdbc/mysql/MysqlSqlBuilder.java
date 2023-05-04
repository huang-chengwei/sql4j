package jdbc.mysql;


import github.alittlehuang.sql4j.dsl.expression.*;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.meta.Attribute;
import github.alittlehuang.sql4j.dsl.support.meta.EntityInformation;
import github.alittlehuang.sql4j.dsl.util.Array;
import github.alittlehuang.sql4j.dsl.util.Assert;
import jakarta.persistence.LockModeType;
import jdbc.sql.PreparedSql;
import jdbc.sql.PreparedSqlBuilder;
import jdbc.sql.SelectedPreparedSql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static github.alittlehuang.sql4j.dsl.expression.Operator.AND;


public class MysqlSqlBuilder implements PreparedSqlBuilder {

    protected final QuerySpecification criteria;
    protected final EntityInformation<?> rootEntityInfo;

    public MysqlSqlBuilder(QuerySpecification criteria, Class<?> javaType) {
        this.criteria = criteria;
        this.rootEntityInfo = getEntityInformation(javaType);
    }

    public static PathExpression to(PathExpression expression, String path) {
        return expression.to(path);
    }

    @Override
    public SelectedPreparedSql getEntityList(int offset, int maxResultant, LockModeType lockModeType) {
        return new EntityBuilder().getEntityList(offset, maxResultant, lockModeType);
    }

    @Override
    public PreparedSql getObjectsList(int offset, int maxResultant, LockModeType lockModeType) {
        return new Builder().getObjectsList(offset, maxResultant, lockModeType);
    }

    @Override
    public PreparedSql exist(int offset) {
        return new Builder().exist(offset);
    }

    @Override
    public PreparedSql count() {
        return new Builder().count();
    }

    public EntityInformation<?> getEntityInformation(Attribute attribute) {
        return getEntityInformation(attribute.getJavaType());
    }

    public EntityInformation<?> getEntityInformation(Class<?> clazz) {
        EntityInformation<?> info = EntityInformation.getInstance(clazz);
        Assert.notNull(info, "the type " + clazz + " is not an entity type");
        return info;
    }

    private class EntityBuilder extends Builder implements SelectedPreparedSql {
        protected final List<PathExpression> selectedPath = new ArrayList<>();

        protected SelectedPreparedSql getEntityList(int offset, int maxResult, LockModeType lockModeType) {
            sql.append("select ");
            appendEntityPath();
            appendFetchPath();
            appendQueryConditions(offset, maxResult);
            appendLockModeType(lockModeType);
            return this;
        }

        protected void appendEntityPath() {
            String join = "";
            for (Attribute basicAttribute : rootEntityInfo.getBasicAttributes()) {
                sql.append(join);
                PathExpression path = new PathExpression(basicAttribute.getFieldName());
                appendPath(path);
                selectedPath.add(path);
                join = ",";
            }
        }

        protected void appendFetchPath() {
            Array<PathExpression> fetchClause = criteria.fetchClause();
            if (fetchClause != null) {
                for (PathExpression fetch : fetchClause) {
                    Attribute attribute = getAttribute(fetch);
                    EntityInformation<?> entityInfo = getEntityInformation(attribute);
                    for (Attribute basicAttribute : entityInfo.getBasicAttributes()) {
                        sql.append(",");
                        PathExpression path = fetch.to(basicAttribute.getFieldName());
                        appendPath(path);
                        selectedPath.add(path);
                    }
                }
            }
        }

        @Override
        public List<PathExpression> getSelectedPath() {
            return selectedPath;
        }
    }

    protected class Builder implements PreparedSql {
        protected final StringBuilder sql = new StringBuilder();
        protected final List<Object> args = new ArrayList<>();
        protected final Map<PathExpression, Integer> joins = new LinkedHashMap<>();

        protected PreparedSql getObjectsList(int offset, int maxResult, LockModeType lockModeType) {
            sql.append("select ");
            appendSelectedPath();
            appendBlank()
                    .append("from `")
                    .append(rootEntityInfo.getTableName())
                    .append("` ");
            appendRootTableAlias();
            int sqlIndex = sql.length();
            appendWhere();
            appendGroupBy();
            appendOrderBy();
            limit(offset, maxResult);
            insertJoin(sqlIndex);
            appendLockModeType(lockModeType);
            return this;
        }

        protected void appendLockModeType(LockModeType lockModeType) {
            if (lockModeType == LockModeType.PESSIMISTIC_READ) {
                sql.append(" for share");
            } else if (lockModeType == LockModeType.PESSIMISTIC_WRITE) {
                sql.append(" for update");
            } else if (lockModeType == LockModeType.PESSIMISTIC_FORCE_INCREMENT) {
                sql.append(" for update nowait");
            }
        }

        protected void appendQueryConditions(int offset, int maxResult) {
            appendBlank()
                    .append("from `")
                    .append(rootEntityInfo.getTableName())
                    .append("` ");
            appendRootTableAlias();
            int sqlIndex = sql.length();
            appendWhere();
            appendOrderBy();
            limit(offset, maxResult);
            insertJoin(sqlIndex);
        }

        protected PreparedSql exist(int offset) {
            sql.append("select ");
            Attribute attribute = rootEntityInfo.getIdAttribute();
            appendRootTableAlias();
            sql.append(".`").append(attribute.getColumnName()).append("`");
            appendBlank()
                    .append("from `")
                    .append(rootEntityInfo.getTableName())
                    .append("` ");
            appendRootTableAlias();
            int sqlIndex = sql.length();
            appendWhere();
            limit(offset, -1);
            insertJoin(sqlIndex);
            return this;
        }

        protected PreparedSql count() {
            sql.append("select count(");
            Attribute attribute = rootEntityInfo.getIdAttribute();
            appendRootTableAlias();
            sql.append(".`").append(attribute.getColumnName()).append("`)");
            appendBlank()
                    .append("from `")
                    .append(rootEntityInfo.getTableName())
                    .append("` ");
            appendRootTableAlias();
            int sqlIndex = sql.length();
            appendWhere();
            insertJoin(sqlIndex);
            return this;
        }

        @Override
        public String getSql() {
            return sql.toString();
        }

        @Override
        public List<Object> getArgs() {
            return args;
        }

        protected StringBuilder appendRootTableAlias() {
            return appendRootTableAlias(sql);
        }

        protected StringBuilder appendRootTableAlias(StringBuilder sql) {
            return sql.append(rootEntityInfo.getTableName().charAt(0)).append("_r_");
        }

        protected StringBuilder appendBlank() {
            return sql.length() > 0 && " (".indexOf(sql.charAt(sql.length() - 1)) < 0
                    ? sql.append(' ')
                    : sql;
        }


        protected void appendWhere() {
            if (criteria.whereClause() == null) {
                return;
            }
            sql.append(" where ");
            appendExpression(criteria.whereClause());
        }

        protected void appendExpression(Expression e) {
            appendExpressions(args, e);
        }


        protected void appendExpressions(List<Object> args, Expression e) {
            if (e instanceof ConstantExpression ce) {
                Object value = ce.value();
                boolean isNumber = false;
                if (value != null) {
                    Class<?> valueType = value.getClass();
                    if (valueType.isPrimitive() || Number.class.isAssignableFrom(valueType)) {
                        isNumber = true;
                    }
                }
                if (isNumber) {
                    appendBlank().append(value);
                } else {
                    appendBlank().append('?');
                    args.add(value);
                }
            } else if (e instanceof PathExpression pe) {
                appendBlank();
                appendPath(pe);
            } else if (e instanceof OperatorExpression oe) {
                Operator operator = oe.operator();
                Array<Expression> list = oe.expressions();
                Expression e0 = list.get(0);
                Operator operator0 = getOperator(e0);
                JdbcOperator jdbcOperator = JdbcOperator.of(operator);
                // noinspection EnhancedSwitchMigration
                switch (operator) {
                    case NOT:
                        appendBlank().append(jdbcOperator);
                        sql.append(' ');
                        if (operator0 != null && JdbcOperator.of(operator0).getPrecedence()
                                                 > jdbcOperator.getPrecedence()) {
                            sql.append('(');
                            appendExpressions(args, e0);
                            sql.append(')');
                        } else {
                            appendExpressions(args, e0);
                        }
                        break;
                    case AND:
                    case OR:
                    case LIKE:
                    case MOD:
                    case GT:
                    case EQ:
                    case NE:
                    case GE:
                    case LT:
                    case LE:
                    case ADD:
                    case SUBTRACT:
                    case MULTIPLY:
                    case DIVIDE:
                        appendBlank();
                        if (operator0 != null && JdbcOperator.of(operator0).getPrecedence()
                                                 > jdbcOperator.getPrecedence()) {
                            sql.append('(');
                            appendExpressions(args, e0);
                            sql.append(')');
                        } else {
                            appendExpressions(args, e0);
                        }
                        for (int i = 1; i < list.length(); i++) {
                            appendBlank();
                            sql.append(jdbcOperator);
                            Expression e1 = list.get(i);
                            Operator operator1 = getOperator(e1);
                            if (operator1 != null && JdbcOperator.of(operator1).getPrecedence()
                                                     >= jdbcOperator.getPrecedence()) {
                                sql.append('(');
                                appendExpressions(args, e1);
                                sql.append(')');
                            } else {
                                appendExpressions(args, e1);
                            }
                        }
                        break;
                    case LOWER:
                    case UPPER:
                    case SUBSTRING:
                    case TRIM:
                    case LENGTH:
                    case NULLIF:
                    case IF_NULL:
                    case ISNULL:
                    case MIN:
                    case MAX:
                    case COUNT:
                    case AVG:
                    case SUM: {
                        appendBlank().append(jdbcOperator);
                        String join = "(";
                        for (Expression expression : list) {
                            sql.append(join);
                            appendExpressions(args, expression);
                            join = ",";
                        }
                        sql.append(")");
                        break;
                    }
                    case IN: {
                        if (list.length() == 1) {
                            appendBlank().append(0);
                        } else {
                            appendBlank();
                            appendExpression(e0);

                            appendBlank().append(jdbcOperator);
                            char join = '(';
                            for (int i = 1; i < list.length(); i++) {
                                Expression expression = list.get(i);
                                sql.append(join);
                                appendExpressions(args, expression);
                                join = ',';
                            }
                            sql.append(")");
                        }
                        break;
                    }
                    case BETWEEN:
                        appendBlank();
                        appendExpressions(args, list.get(0));
                        appendBlank().append(jdbcOperator).append(" ");
                        appendExpressions(args, list.get(1).operate(AND, list.get(2)));
                        break;
                    default:
                        throw new UnsupportedOperationException("unknown operator " + operator);
                }
            } else {
                throw new UnsupportedOperationException("unknown expression type " + e.getClass());
            }
        }


        protected void appendPath(PathExpression expression) {
            StringBuilder sb = sql;
            int iMax = expression.length() - 1;
            if (iMax == -1)
                return;
            int i = 0;
            if (expression.length() == 1) {
                appendRootTableAlias().append(".");
            }
            Class<?> type = MysqlSqlBuilder.this.rootEntityInfo.getJavaType();

            PathExpression join = new PathExpression(expression.get(0));

            for (String path : expression.path()) {
                EntityInformation<?> info = getEntityInformation(type);
                Attribute attribute = info.getAttribute(path);
                if (i++ == iMax) {
                    sb.append('`').append(attribute.getColumnName()).append('`');
                    return;
                } else {
                    joins.putIfAbsent(join, joins.size());
                    if (i == iMax) {
                        Integer index = joins.get(join);
                        appendTableAlice(sb, attribute, index).append('.');
                    }
                }
                type = attribute.getJavaType();
                join = to(join, path);
            }
        }

        protected void insertJoin(int sqlIndex) {
            StringBuilder sql = new StringBuilder();

            joins.forEach((k, v) -> {
                Attribute attribute = getAttribute(k);
                EntityInformation<?> entityInfo = getEntityInformation(attribute);
                sql.append(" left join `").append(entityInfo.getTableName()).append("` ");

                appendTableAlice(sql, attribute, v);
                sql.append(" on ");
                PathExpression parent = k.parent();
                if (parent == null) {
                    appendRootTableAlias(sql);
                } else {
                    Integer parentIndex = joins.get(parent);
                    Attribute parentAttribute = getAttribute(parent);
                    appendTableAlice(sql, parentAttribute, parentIndex);
                }
                sql.append(".`").append(attribute.getJoinColumn().name()).append("`=");
                appendTableAlice(sql, attribute, v);
                String referenced = attribute.getJoinColumn().referencedColumnName();
                if (referenced.length() == 0) {
                    referenced = entityInfo.getIdAttribute().getColumnName();
                }
                sql.append(".`").append(referenced).append('`');
            });
            this.sql.insert(sqlIndex, sql);

        }

        Operator getOperator(Expression e) {
            return e instanceof OperatorExpression expression ? expression.operator() : null;
        }

        protected StringBuilder appendTableAlice(StringBuilder sb, Attribute attribute, Integer index) {
            EntityInformation<?> information = getEntityInformation(attribute.getJavaType());
            return sb.append(information.getTableName().charAt(0)).append("_j").append(index).append("_");
        }

        protected Attribute getAttribute(PathExpression path) {
            Attribute attribute = null;
            for (String s : path.path()) {
                EntityInformation<?> entityInfo = attribute == null
                        ? rootEntityInfo
                        : getEntityInformation(attribute);
                attribute = entityInfo.getAttribute(s);
            }
            return attribute;
        }

        protected void limit(int offset, int maxResults) {
            if (offset >= 0 || maxResults >= 0) {
                sql.append(" LIMIT ")
                        .append(Math.max(offset, 0))
                        .append(',')
                        .append(maxResults < 0 ? Long.MAX_VALUE : maxResults);
            }
        }

        protected void appendSelectedPath() {
            Iterable<Expression> select = criteria.selectClause();
            if (select == null || !select.iterator().hasNext()) {
                select = rootEntityInfo.getBasicAttributes()
                        .stream()
                        .map(i -> {
                            String fieldName = i.getFieldName();
                            return new PathExpression(fieldName);
                        })
                        .collect(Collectors.toList());
            }
            String join = "";
            for (Expression selection : select) {
                sql.append(join);
                appendExpression(selection);
                join = ",";
            }
        }


        private void appendGroupBy() {
            Array<Expression> groupBy = criteria.groupByClause();
            if (groupBy != null && !groupBy.isEmpty()) {
                sql.append(" group by ");
                boolean first = true;
                for (Expression e : groupBy) {
                    if (first) {
                        first = false;
                    } else {
                        sql.append(",");
                    }
                    appendExpression(e);
                }

            }
        }

        protected void appendOrderBy() {
            Array<SortSpecification> orders = criteria.sortSpec();
            if (orders != null && !orders.isEmpty()) {
                sql.append(" order by ");
                boolean first = true;
                for (SortSpecification order : orders) {
                    if (first) {
                        first = false;
                    } else {
                        sql.append(",");
                    }
                    appendExpression(order.expression());
                    sql.append(" ").append(order.desc() ? "desc" : "asc");
                }

            }
        }
    }

}