package jdbc.sql;


import github.alittlehuang.sql4j.dsl.expression.PathExpression;

import java.util.List;

public interface SelectedPreparedSql extends PreparedSql {

    List<PathExpression> getSelectedPath();

}
