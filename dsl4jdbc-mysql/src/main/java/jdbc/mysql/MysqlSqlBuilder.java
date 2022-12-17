package jdbc.mysql;


import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.support.meta.Attribute;
import github.sql4j.dsl.support.meta.EntityInformation;
import github.sql4j.dsl.support.meta.ProjectionAttribute;
import github.sql4j.dsl.support.meta.ProjectionInformation;
import github.sql4j.dsl.util.Array;
import github.sql4j.dsl.util.Assert;
import jdbc.sql.PreparedSql;
import jdbc.sql.PreparedSqlBuilder;
import jdbc.sql.SelectedPreparedSql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static github.sql4j.dsl.expression.Operator.AND;


public class MysqlSqlBuilder implements PreparedSqlBuilder {

    protected final StructuredQuery criteria;
    protected final EntityInformation<?> rootEntityInfo;

    public MysqlSqlBuilder(StructuredQuery criteria, Class<?> javaType) {
        this.criteria = criteria;
        this.rootEntityInfo = getEntityInformation(javaType);
    }

    public static PathExpression<?> to(PathExpression<?> expression, String path) {
        String[] values = Stream.concat(expression.stream(), Stream.of(path))
                .toArray(String[]::new);
        return new PathExpression<>(values);
    }

    @Override
    public SelectedPreparedSql getEntityList(int offset, int maxResultant) {
        return new EntityBuilder().getEntityList(offset, maxResultant);
    }

    @Override
    public PreparedSql getObjectsList(int offset, int maxResultant) {
        return new Builder().getObjectsList(offset, maxResultant);
    }

    @Override
    public PreparedSql exist(int offset) {
        return new Builder().exist(offset);
    }

    @Override
    public PreparedSql count() {
        return new Builder().count();
    }

    @Override
    public SelectedPreparedSql getProjectionList(int offset, int maxResult, Class<?> projectionType) {
        return new ProjectionBuilder(projectionType).getProjectionList(offset, maxResult);
    }

    public EntityInformation<?> getEntityInformation(Attribute attribute) {
        return getEntityInformation(attribute.getJavaType());
    }

    public EntityInformation<?> getEntityInformation(Class<?> clazz) {
        EntityInformation<?> info = EntityInformation.getInstance(clazz);
        Assert.notNull(info, "the type " + clazz + " is not an entity type");
        return info;
    }

    private class ProjectionBuilder extends Builder implements SelectedPreparedSql {
        protected final List<PathExpression<?>> selectedPath = new ArrayList<>();
        private final Class<?> projectionType;

        private ProjectionBuilder(Class<?> projectionType) {
            this.projectionType = projectionType;
        }

        protected SelectedPreparedSql getProjectionList(int offset, int maxResult) {
            sql.append("select ");
            appendProjectionPath();
            appendQueryConditions(offset, maxResult);
            return this;
        }

        private void appendProjectionPath() {
            String join = "";
            ProjectionInformation attributes = ProjectionInformation
                    .get(rootEntityInfo.getJavaType(), projectionType);
            for (ProjectionAttribute basicAttribute : attributes) {
                sql.append(join);
                PathExpression<Object> path = new PathExpression<>(basicAttribute.getFieldName());
                appendPath(path);
                selectedPath.add(path);
                join = ",";
            }
        }

        @Override
        public List<PathExpression<?>> getSelectedPath() {
            return selectedPath;
        }
    }

    private class EntityBuilder extends Builder implements SelectedPreparedSql {
        protected final List<PathExpression<?>> selectedPath = new ArrayList<>();

        protected SelectedPreparedSql getEntityList(int offset, int maxResult) {
            sql.append("select ");
            appendEntityPath();
            appendFetchPath();
            appendQueryConditions(offset, maxResult);
            return this;
        }

        protected void appendEntityPath() {
            String join = "";
            for (Attribute basicAttribute : rootEntityInfo.getBasicAttributes()) {
                sql.append(join);
                PathExpression<Object> path = new PathExpression<>(basicAttribute.getFieldName());
                appendPath(path);
                selectedPath.add(path);
                join = ",";
            }
        }

        protected void appendFetchPath() {
            if (criteria.fetch() != null) {
                for (PathExpression<?> fetch : criteria.fetch()) {
                    Attribute attribute = getAttribute(fetch);
                    EntityInformation<?> entityInfo = getEntityInformation(attribute);
                    for (Attribute basicAttribute : entityInfo.getBasicAttributes()) {
                        sql.append(",");
                        PathExpression<?> path = to(fetch, basicAttribute.getFieldName());
                        appendPath(path);
                        selectedPath.add(path);
                    }
                }
            }
        }

        @Override
        public List<PathExpression<?>> getSelectedPath() {
            return selectedPath;
        }
    }

    protected class Builder implements PreparedSql {
        protected final StringBuilder sql = new StringBuilder();
        protected final List<Object> args = new ArrayList<>();
        protected final Map<PathExpression<?>, Integer> joins = new LinkedHashMap<>();

        protected PreparedSql getObjectsList(int offset, int maxResult) {
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
            return this;
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
            if (criteria.where() == null) {
                return;
            }
            sql.append(" where ");
            appendExpression(criteria.where());
        }

        protected void appendExpression(SqlExpression<?> e) {
            appendExpressions(args, e);
        }


        protected void appendExpressions(List<Object> args, SqlExpression<?> e) {
            if (e.getType() == SqlExpression.Type.CONSTANT) {
                Object value = e.getValue();
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
            } else if (e.getType() == SqlExpression.Type.PATH) {
                appendBlank();
                appendPath(e);
            } else if (e.getType() == SqlExpression.Type.OPERATOR) {
                Operator operator = e.getOperator();
                List<? extends SqlExpression<?>> list = e.getExpressions();
                SqlExpression<?> e0 = list.get(0);
                Operator operator0 = getOperator(e0);
                JdbcOperator jdbcOperator = JdbcOperator.of(operator);
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

                        appendBlank();
                        sql.append(jdbcOperator);
                        SqlExpression<?> e1 = list.get(1);
                        Operator operator1 = getOperator(e1);
                        if (operator1 != null && JdbcOperator.of(operator1).getPrecedence()
                                >= jdbcOperator.getPrecedence()) {
                            sql.append('(');
                            appendExpressions(args, e1);
                            sql.append(')');
                        } else {
                            appendExpressions(args, e1);
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
                        for (SqlExpression<?> expression : list) {
                            sql.append(join);
                            appendExpressions(args, expression);
                            join = ",";
                        }
                        sql.append(")");
                        break;
                    }
                    case IN: {
                        if (list.size() == 1) {
                            appendBlank().append(0);
                        } else {
                            appendBlank();
                            appendExpression(e0);

                            appendBlank().append(jdbcOperator);
                            char join = '(';
                            for (int i = 1; i < list.size(); i++) {
                                SqlExpression<?> expression = list.get(i);
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
                        appendExpressions(args, list.get(1).then(AND, list.get(2)));
                        break;
                    default:
                        throw new UnsupportedOperationException("unknown operator " + operator);
                }
            } else {
                throw new UnsupportedOperationException("unknown expression type " + e.getClass());
            }
        }


        protected void appendPath(SqlExpression<?> expression) {
            if (expression.getType() != SqlExpression.Type.PATH) {
                throw new UnsupportedOperationException();
            }
            PathExpression<?> paths = expression.asPathExpression();
            StringBuilder sb = sql;
            int iMax = paths.size() - 1;
            if (iMax == -1)
                return;
            int i = 0;
            if (paths.size() == 1) {
                appendRootTableAlias().append(".");
            }
            Class<?> type = MysqlSqlBuilder.this.rootEntityInfo.getJavaType();

            PathExpression<?> join = new PathExpression<>(paths.get(0));

            for (String path : paths) {
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
                PathExpression<?> parent = k.parent();
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

        Operator getOperator(SqlExpression<?> e) {
            if (e.getType() == SqlExpression.Type.OPERATOR) {
                return e.getOperator();
            }
            return null;
        }

        protected StringBuilder appendTableAlice(StringBuilder sb, Attribute attribute, Integer index) {
            EntityInformation<?> information = getEntityInformation(attribute.getJavaType());
            return sb.append(information.getTableName().charAt(0)).append("_j").append(index).append("_");
        }

        protected Attribute getAttribute(PathExpression<?> path) {
            Attribute attribute = null;
            for (String s : path.asPathExpression()) {
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
            Iterable<SqlExpression<?>> select = criteria.select();
            if (select == null || !select.iterator().hasNext()) {
                select = rootEntityInfo.getBasicAttributes()
                        .stream()
                        .map(i -> {
                            String fieldName = i.getFieldName();
                            return new PathExpression<>(fieldName);
                        })
                        .collect(Collectors.toList());
            }
            String join = "";
            for (SqlExpression<?> selection : select) {
                sql.append(join);
                appendExpression(selection);
                join = ",";
            }
        }


        private void appendGroupBy() {
            Array<SqlExpression<?>> groupBy = criteria.groupBy();
            if (groupBy != null && !groupBy.isEmpty()) {
                sql.append(" group by ");
                boolean first = true;
                for (SqlExpression<?> e : groupBy) {
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
            Array<Order> orders = criteria.orderBy();
            if (orders != null && !orders.isEmpty()) {
                sql.append(" order by ");
                boolean first = true;
                for (Order order : orders) {
                    if (first) {
                        first = false;
                    } else {
                        sql.append(",");
                    }
                    appendExpression(order.getExpression());
                    sql.append(" ").append(order.isDesc() ? "desc" : "asc");
                }

            }
        }
    }

}