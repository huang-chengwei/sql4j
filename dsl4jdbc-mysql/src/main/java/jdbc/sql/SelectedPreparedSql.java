package jdbc.sql;


import github.sql4j.dsl.expression.PathExpression;

import java.util.List;

public interface SelectedPreparedSql extends PreparedSql {

    List<PathExpression<?>> getSelectedPath();

}
