package github.alittlehuang.sql4j.dsl.builder;

public enum LockModeType {

    READ,
    WRITE,
    OPTIMISTIC,
    OPTIMISTIC_FORCE_INCREMENT,
    PESSIMISTIC_READ,
    PESSIMISTIC_WRITE,
    PESSIMISTIC_FORCE_INCREMENT,
    NONE

}
