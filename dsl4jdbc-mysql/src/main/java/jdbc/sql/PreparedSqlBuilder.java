package jdbc.sql;

import jakarta.persistence.LockModeType;

public interface PreparedSqlBuilder {

    SelectedPreparedSql getEntityList(int offset, int maxResultant, LockModeType lockModeType);

    PreparedSql getObjectsList(int offset, int maxResultant, LockModeType lockModeType);

    PreparedSql exist(int offset);

    PreparedSql count();

    // SelectedPreparedSql getProjectionList(int offset, int maxResult, Class<?> projectionType);
}
