package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.util.Assert;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface ResultBuilder<T> {

    int IGNORED = -1;

    int count();

    List<T> getList(int offset, int maxResult, LockModeType lockModeType);

    default List<T> getList(int offset, int maxResult) {
        return getList(offset, maxResult, null);
    }

    boolean exist(int offset);

    default Optional<T> first() {
        return Optional.ofNullable(getFirst());
    }

    default Optional<T> first(int offset) {
        return Optional.ofNullable(getFirst(offset));
    }

    default T getFirst() {
        return getFirst(IGNORED);
    }

    default T getFirst(int offset) {
        List<T> list = getList(offset, 1);
        return list.isEmpty() ? null : list.get(0);
    }

    default T requireSingle() {
        return Objects.requireNonNull(getSingle(IGNORED));
    }

    default Optional<T> single() {
        return Optional.ofNullable(getSingle());
    }

    default Optional<T> single(int offset) {
        return Optional.ofNullable(getSingle(offset));
    }

    default T getSingle() {
        return getSingle(IGNORED);
    }

    default T getSingle(int offset) {
        List<T> list = getList(offset, 2);
        Assert.state(list.size() <= 1, "found more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    default List<T> getList(int offset) {
        return getList(offset, Integer.MAX_VALUE);
    }

    default List<T> getList() {
        return getList(IGNORED, IGNORED);
    }

    default boolean exist() {
        return exist(IGNORED);
    }


    default Optional<T> first(LockModeType lockModeType) {
        return Optional.ofNullable(getFirst(lockModeType));
    }

    default Optional<T> first(int offset, LockModeType lockModeType) {
        return Optional.ofNullable(getFirst(offset));
    }

    default T getFirst(LockModeType lockModeType) {
        return getFirst(IGNORED, lockModeType);
    }

    default T getFirst(int offset, LockModeType lockModeType) {
        List<T> list = getList(offset, 1, lockModeType);
        return list.isEmpty() ? null : list.get(0);
    }

    default T requireSingle(LockModeType lockModeType) {
        return Objects.requireNonNull(getSingle(IGNORED, lockModeType));
    }

    default Optional<T> single(LockModeType lockModeType) {
        return Optional.ofNullable(getSingle(lockModeType));
    }

    default Optional<T> single(int offset, LockModeType lockModeType) {
        return Optional.ofNullable(getSingle(offset, lockModeType));
    }

    default T getSingle(LockModeType lockModeType) {
        return getSingle(IGNORED, lockModeType);
    }

    default T getSingle(int offset, LockModeType lockModeType) {
        List<T> list = getList(offset, 2, lockModeType);
        Assert.state(list.size() <= 1, "found more than one");
        return list.isEmpty() ? null : list.get(0);
    }

    default List<T> getList(int offset, LockModeType lockModeType) {
        return getList(offset, Integer.MAX_VALUE, lockModeType);
    }

    default List<T> getList(LockModeType lockModeType) {
        return getList(IGNORED, IGNORED, lockModeType);
    }

    default <R> R getResult(@NotNull Function<? super ResultBuilder<T>, R> function) {
        return function.apply(this);
    }

    default Slice<T> slice(Sliceable sliceable) {
        int count = count();
        if (count == 0) {
            return new Slice<>(Collections.emptyList(), sliceable, 0);
        } else {
            List<T> list = getList(sliceable.offset(), sliceable.size());
            return new Slice<>(list, sliceable, count);
        }
    }

}
