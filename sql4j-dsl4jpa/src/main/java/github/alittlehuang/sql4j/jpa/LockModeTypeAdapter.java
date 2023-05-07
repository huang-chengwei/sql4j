package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.builder.LockModeType;

public class LockModeTypeAdapter {

    public static javax.persistence.LockModeType of(LockModeType lockModeType) {
        return lockModeType == null ? null : javax.persistence.LockModeType.valueOf(lockModeType.name());
    }

}
