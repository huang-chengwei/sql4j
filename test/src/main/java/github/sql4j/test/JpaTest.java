package github.sql4j.test;

import github.sql4j.dsl.QueryBuilder;
import github.sql4j.dsl.builder.Query;
import github.sql4j.dsl.builder.WhereBuilder;
import github.sql4j.dsl.expression.Predicate;
import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.expression.path.attribute.ComparableAttribute;
import github.sql4j.dsl.expression.path.attribute.EntityAttribute;
import github.sql4j.dsl.support.builder.component.AggregateFunction;
import github.sql4j.jpa.JpaQueryBuilder;
import github.sql4j.test.entity.User;
import github.sql4j.test.projection.UserInterface;
import github.sql4j.test.projection.UserModel;
import jakarta.persistence.EntityManager;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class JpaTest {
    private static final String username = "Jeremy Keynes";

    protected static Query<User> userQuery;
    protected static List<User> allUsers;

    @SuppressWarnings("JpaQlInspection")
    @BeforeAll
    public static void init() {
        EntityManager manager = EntityManagers.getEntityManager();
        QueryBuilder queryBuilder = new JpaQueryBuilder(manager);
        allUsers = Users.getUsers();
        userQuery = queryBuilder.query(User.class);

        doInTransaction(() -> {
            manager.createQuery("update User set pid = null").executeUpdate();
            manager.createQuery("delete from User").executeUpdate();
            for (User user : allUsers) {
                manager.persist(user);
            }
        });

        manager.clear();
    }

    public static void doInTransaction(Runnable action) {
        Object o = doInTransaction(() -> {
            action.run();
            return null;
        });
        log.trace("{}", o);
    }

    public static <T> T doInTransaction(Callable<T> action) {
        EntityManager manager = EntityManagers.getEntityManager();

        Session session = manager.unwrap(Session.class);
        Transaction transaction = session.getTransaction();
        T result;
        try {
            transaction.begin();
            result = action.call();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw Lombok.sneakyThrow(e);
        }

        return result;
    }


    @Test
    public void testComparablePredicateTesterGt() {

        List<User> qgt80 = userQuery
                .where(User::getRandomNumber).gt(80)
                .orderBy(User::getId).asc()
                .getList();
        List<User> fgt80 = allUsers.stream()
                .filter(it -> it.getRandomNumber() > 80)
                .collect(Collectors.toList());
        assertEquals(qgt80, fgt80);

    }

    @Test
    public void testPredicateTesterEq() {
        int userId = 20;
        User user = userQuery
                .where(User::getId).eq(userId)
                .fetch(User::getParentUser)
                .fetch(EntityAttribute.of(User::getParentUser).map(User::getParentUser))
                .getSingle();
        assertNotNull(user);
        assertEquals(user.getId(), userId);
        if (user.getPid() != null) {
            User parentUser = user.getParentUser();
            assertNotNull(parentUser);
            assertEquals(user.getPid(), parentUser.getId());
            User single = userQuery.where(User::getId).eq(parentUser.getId()).getSingle();
            assertEquals(single, parentUser);
        }

    }

    @Test
    public void testAggregateFunction() {
        Object[] aggregated = userQuery
                .select(User::getRandomNumber, AggregateFunction.MIN)
                .select(User::getRandomNumber, AggregateFunction.MAX)
                .select(User::getRandomNumber, AggregateFunction.COUNT)
                .select(User::getRandomNumber, AggregateFunction.AVG)
                .select(User::getRandomNumber, AggregateFunction.SUM)
                .requireSingle();
        assertNotNull(aggregated);
        assertEquals(getUserIdStream().min().orElse(0), aggregated[0]);
        assertEquals(getUserIdStream().max().orElse(0), aggregated[1]);
        assertEquals(getUserIdStream().count(), aggregated[2]);
        OptionalDouble average = getUserIdStream().average();
        assertEquals(average.orElse(0), ((Number) aggregated[3]).doubleValue(), 0.0001);
        assertEquals((long) getUserIdStream().sum(), ((Number) aggregated[4]).intValue());

        List<Object[]> resultList = userQuery
                .select(User::getId, AggregateFunction.MIN)
                .groupBy(User::getRandomNumber)
                .select(User::getRandomNumber)
                .where(User::isValid).eq(true)
                .getList();

        Map<Integer, Optional<User>> map = allUsers.stream()
                .filter(User::isValid)
                .collect(Collectors.groupingBy(User::getRandomNumber, Collectors.minBy(Comparator.comparingInt(User::getId))));

        List<Object[]> fObjects = map.values().stream()
                .map(user -> {
                    Integer userId = user.map(User::getId).orElse(null);
                    Integer randomNumber = user.map(User::getRandomNumber).orElse(null);
                    return new Object[]{userId, randomNumber};
                })
                .sorted(Comparator.comparing(a -> ((Integer) a[0])))
                .collect(Collectors.toList());
        assertEqualsArrayList(resultList, fObjects);

        Object[] one = userQuery
                .select(User::getId, AggregateFunction.SUM)
                .where(User::isValid).eq(true)
                .requireSingle();

        int userId = allUsers.stream()
                .filter(User::isValid)
                .mapToInt(User::getId)
                .sum();
        assertEquals(((Number) one[0]).intValue(), userId);

        Object[] first = userQuery
                .select(User::getId)
                .orderBy(User::getId).desc()
                .getFirst();
        assertEquals(first[0], allUsers.get(allUsers.size() - 1).getId());
    }

    @Test
    public void testSelect() {
        List<Object[]> qList = userQuery
                .select(User::getRandomNumber)
                .select(User::getUsername)
                .getList();

        List<Object[]> fList = allUsers.stream()
                .map(it -> new Object[]{it.getRandomNumber(), it.getUsername()})
                .collect(Collectors.toList());

        assertEqualsArrayList(qList, fList);

    }

    @Test
    public void testGroupBy() {
        List<Object[]> resultList = userQuery
                .groupBy(User::getRandomNumber)
                .groupBy(Arrays.asList(User::getPid, User::isValid))
                .select(User::isValid)
                .select(User::getRandomNumber)
                .select(User::getPid)
                .getList();

        List<Object[]> resultList2 = userQuery
                .groupBy(User::getRandomNumber)
                .groupBy(Arrays.asList(User::getPid, User::isValid))
                .select(User::isValid)
                .select(Arrays.asList(User::getRandomNumber, User::getPid))
                .getList();
        assertEqualsArrayList(resultList, resultList2);
    }

    private void assertEqualsArrayList(List<Object[]> resultList, List<Object[]> resultList2) {
        assertEquals(resultList.size(), resultList2.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertArrayEquals(resultList.get(i), resultList2.get(i));
        }
    }

    @Test
    public void testOrderBy() {
        List<User> list = userQuery
                .orderBy(User::getRandomNumber).desc()
                .getList();
        ArrayList<User> sorted = new ArrayList<>(allUsers);
        sorted.sort((a, b) -> Integer.compare(b.getRandomNumber(), a.getRandomNumber()));
        assertEquals(list, sorted);

        list = userQuery
                .orderBy(User::getUsername).asc()
                .orderBy(User::getRandomNumber).desc()
                .orderBy(User::getId).asc()
                .getList();

        sorted.sort((a, b) -> Integer.compare(b.getRandomNumber(), a.getRandomNumber()));
        sorted.sort(Comparator.comparing(User::getUsername));
        assertEquals(list, sorted);

        list = userQuery
                .orderBy(User::getTime).asc()
                .getList();
        sorted = new ArrayList<>(allUsers);
        sorted.sort(Comparator.comparing(User::getTime));
        assertEquals(list, sorted);
    }

    @Test
    public void testPredicate() {
        List<User> qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .or(User::getRandomNumber).lt(5)
                        .not()
                )
                .getList();
        List<User> fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 || it.getRandomNumber() < 5))
                .collect(Collectors.toList());


        assertEquals(qList, fList);

        qList = userQuery.where(Predicate
                        .get(User::getUsername).eq("Jeremy Keynes")
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getUsername().equalsIgnoreCase("Jeremy Keynes")))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(Predicate
                        .get((ComparableAttribute<User, String>) User::getUsername).eq("Jeremy Keynes")
                        .not()
                )
                .getList();
        assertEquals(qList, fList);


        Predicate<User> jeremy_keynes = Predicate
                .get((Attribute<User, String>) User::getUsername).eq("Jeremy Keynes");
        qList = userQuery.where(jeremy_keynes
                        .or(Predicate.get(User::getId).eq(3))
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getUsername().equalsIgnoreCase("Jeremy Keynes")
                        || it.getId() == 3))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(jeremy_keynes
                        .and(Predicate.get(User::getId).eq(3))
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getUsername().equalsIgnoreCase("Jeremy Keynes")
                        && it.getId() == 3))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

    }

    @Test
    public void testIsNull() {

        List<User> qList = userQuery.whereNot(User::getPid).isNull()
                .getList();

        List<User> fList = allUsers.stream()
                .filter(it -> it.getPid() != null)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(User::getPid).isNull()
                .getList();

        fList = allUsers.stream()
                .filter(it -> it.getPid() == null)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

    }

    @Test
    public void testOperator() {

        WhereBuilder<User> isValid = userQuery.where(Predicate.get(User::isValid));
        List<User> qList = isValid.getList();
        List<User> validUsers = allUsers.stream().filter(User::isValid)
                .collect(Collectors.toList());
        List<User> fList = validUsers;
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).eq(2)
                .getList();
        fList = validUsers.stream().filter(user -> user.getRandomNumber() == 2)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getPid).ne(2)
                .getList();
        fList = validUsers.stream().filter(user -> user.getPid() != null && user.getPid() != 2)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).in(1, 2, 3)
                .getList();
        List<User> qList2 = isValid.and(User::getRandomNumber).in(Arrays.asList(1, 2, 3))
                .getList();
        fList = validUsers.stream().filter(user -> Arrays.asList(1, 2, 3).contains(user.getRandomNumber()))
                .collect(Collectors.toList());
        assertEquals(qList, fList);
        assertEquals(qList2, fList);


        qList = isValid.and(User::getPid).isNull()
                .getList();
        fList = validUsers.stream().filter(user -> user.getPid() == null)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getPid).nullIf(4).eq(2)
                .getList();
        fList = validUsers.stream().filter(user -> {
                    Integer pid = user.getPid();
                    if (pid != null && pid == 4) {
                        pid = null;
                    }
                    return pid != null && pid == 2;
                })
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getPid).ifNull(2).eq(2)
                .getList();
        fList = validUsers.stream().filter(user -> {
                    Integer pid = user.getPid();
                    if (pid == null) {
                        pid = 2;
                    }
                    return pid == 2;
                })
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = isValid.and(User::getRandomNumber).ge(10)
                .getList();
        fList = validUsers.stream().filter(user -> user.getRandomNumber() >= 10)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).gt(10)
                .getList();
        fList = validUsers.stream().filter(user -> user.getRandomNumber() > 10)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).le(10)
                .getList();
        fList = validUsers.stream().filter(user -> user.getRandomNumber() <= 10)
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = isValid.and(User::getRandomNumber).lt(10)
                .getList();
        fList = validUsers.stream().filter(user -> user.getRandomNumber() < 10)
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).between(10, 15)
                .getList();
        fList = validUsers.stream().filter(user -> user.getRandomNumber() >= 10 && user.getRandomNumber() <= 15)
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = isValid.and(User::getRandomNumber).ge(User::getPid)
                .getList();
        fList = validUsers.stream().filter(user -> user.getPid() != null && user.getRandomNumber() >= user.getPid())
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).gt(User::getPid)
                .getList();
        fList = validUsers.stream().filter(user -> user.getPid() != null && user.getRandomNumber() > user.getPid())
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber).le(User::getPid)
                .getList();
        fList = validUsers.stream().filter(user -> user.getPid() != null && user.getRandomNumber() <= user.getPid())
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = isValid.and(User::getRandomNumber).lt(User::getPid)
                .getList();
        fList = validUsers.stream().filter(user -> user.getPid() != null && user.getRandomNumber() < user.getPid())
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = isValid.and(User::getRandomNumber)
                .between(User::getRandomNumber, User::getPid)
                .getList();
        fList = validUsers.stream()
                .filter(user -> user.getPid() != null && user.getRandomNumber() >= user.getRandomNumber() && user.getRandomNumber() <= user.getPid())
                .collect(Collectors.toList());
        assertEquals(qList, fList);

    }

    @Test
    public void testPredicateAssembler() {

        List<User> qList = userQuery.where(User::isValid).eq(true)
                .and(User::getParentUser).map(User::getUsername).eq(username)
                .getList();
        List<User> fList = allUsers.stream()
                .filter(user -> user.isValid()
                        && user.getParentUser() != null
                        && Objects.equals(user.getParentUser().getUsername(), username))
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        Attribute<User, Number> getUsername = User::getRandomNumber;
        qList = userQuery.where(User::isValid).eq(true)
                .and(getUsername).eq(10)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        && Objects.equals(user.getRandomNumber(), 10))
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        qList = userQuery.where(User::isValid).eq(true)
                .or(getUsername).eq(10)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        || Objects.equals(user.getRandomNumber(), 10))
                .collect(Collectors.toList());

        assertEquals(qList, fList);


        qList = userQuery.where(User::isValid).eq(true)
                .andNot(getUsername).eq(10)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        && !Objects.equals(user.getRandomNumber(), 10))
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        qList = userQuery.where(User::isValid).eq(true)
                .orNot(getUsername).eq(10)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        || !Objects.equals(user.getRandomNumber(), 10))
                .collect(Collectors.toList());

        assertEquals(qList, fList);


        Date time = allUsers.get(20).getTime();

        qList = userQuery.where(User::isValid).eq(true)
                .or(User::getParentUser).map(User::getUsername).eq(username)
                .and(User::getTime).ge(time)
                .getList();

        List<User> jeremy_keynes = userQuery.where(User::isValid).eq(true)
                .or(User::getParentUser).map(User::getUsername).eq(username)
                .fetch(User::getParentUser)
                .and(User::getTime).ge(time)
                .getList();

        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        || (user.getParentUser() != null
                        && Objects.equals(user.getParentUser().getUsername(), username)
                        && user.getTime().getTime() >= time.getTime()))
                .collect(Collectors.toList());

        assertEquals(qList, fList);
        assertEquals(qList, jeremy_keynes);


        qList = userQuery.where(User::isValid).eq(true)
                .andNot(User::getRandomNumber).eq(5)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        && user.getRandomNumber() != 5)
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        qList = userQuery.where(User::isValid).eq(true)
                .orNot(User::getRandomNumber).ne(5)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.isValid()
                        || user.getRandomNumber() == 5)
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        qList = userQuery.whereNot(User::getRandomNumber).eq(6)
                .orNot(User::isValid).ne(false)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() != 6
                        || !user.isValid())
                .collect(Collectors.toList());

        assertEquals((qList), (fList));

        qList = userQuery.whereNot(User::getRandomNumber).eq(6)
                .and(User::getParentUser).map(User::isValid).eq(true)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() != 6
                        && (user.getParentUser() != null && user.getParentUser().isValid()))
                .collect(Collectors.toList());

        assertEquals((qList), (fList));

        qList = userQuery.whereNot(User::getRandomNumber).eq(6)
                .andNot(User::getParentUser).map(User::isValid).eq(true)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() != 6
                        && (user.getParentUser() != null && !user.getParentUser().isValid()))
                .collect(Collectors.toList());

        assertEquals((qList), (fList));

        qList = userQuery.whereNot(User::getRandomNumber).eq(6)
                .orNot(User::getParentUser).map(User::isValid).eq(true)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() != 6
                        || (user.getParentUser() != null && !user.getParentUser().isValid()))
                .collect(Collectors.toList());

        assertEquals((qList), (fList));


        qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .or((ComparableAttribute<User, Integer>) User::getRandomNumber).lt(5)
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 || it.getRandomNumber() < 5))
                .collect(Collectors.toList());


        assertEquals(qList, fList);

        qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .andNot((ComparableAttribute<User, Integer>) User::getRandomNumber).lt(5)
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 && it.getRandomNumber() >= 5))
                .collect(Collectors.toList());


        assertEquals(qList, fList);

        qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .and(User::getUsername).eq(username)
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 && it.getUsername().equals(username)))
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .or(User::getUsername).eq(username)
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 || it.getUsername().equals(username)))
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .andNot(User::getUsername).eq(username)
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 && !it.getUsername().equals(username)))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(Predicate
                        .get(User::getRandomNumber).ge(10)
                        .orNot(User::getUsername).eq(username)
                        .not()
                )
                .getList();
        fList = allUsers.stream()
                .filter(it -> !(it.getRandomNumber() >= 10 || !it.getUsername().equals(username)))
                .collect(Collectors.toList());
        assertEquals(qList, fList);


    }

    @Test
    public void testNumberPredicateTester() {
        List<User> list = userQuery
                .where(User::getRandomNumber).add(2).ge(4)
                .getList();
        List<User> fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() + 2 >= 4)
                .collect(Collectors.toList());

        assertEquals(list, fList);

        list = userQuery
                .where(User::getRandomNumber).subtract(2).ge(4)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() - 2 >= 4)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        list = userQuery
                .where(User::getRandomNumber).multiply(2).ge(4)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() * 2 >= 4)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        list = userQuery
                .where(User::getRandomNumber).divide(2).ge(4)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() / 2 >= 4)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        list = userQuery
                .where(User::getRandomNumber).mod(2).ge(1)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() % 2 >= 1)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        ///
        list = userQuery
                .where(User::getRandomNumber).add(User::getId).ge(40)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() + user.getId() >= 40)
                .collect(Collectors.toList());

        assertEquals(list, fList);

        list = userQuery
                .where(User::getRandomNumber).subtract(User::getId).ge(40)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() - user.getId() >= 40)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        list = userQuery
                .where(User::getRandomNumber).multiply(User::getId).ge(40)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getRandomNumber() * user.getId() >= 40)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        list = userQuery
                .where(User::getRandomNumber).divide(User::getId).ge(40)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getId() != 0 && user.getRandomNumber() / user.getId() >= 40)
                .collect(Collectors.toList());

        assertEquals(list, fList);


        list = userQuery
                .where(User::getRandomNumber).mod(User::getId).ge(10)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getId() != 0 && user.getRandomNumber() % user.getId() >= 10)
                .collect(Collectors.toList());

        assertEquals(list, fList);

    }

    @Test
    public void testStringPredicateTester() {
        String username = "Roy Sawyer";

        List<User> qList = userQuery.where(User::getUsername).substring(2).eq("eremy Keynes")
                .getList();
        List<User> fList = allUsers.stream()
                .filter(user -> user.getUsername().substring(1).equals("eremy Keynes"))
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        qList = userQuery.where(User::getUsername).substring(1, 1).eq("M")
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().charAt(0) == 'M')
                .collect(Collectors.toList());

        assertEquals(qList, fList);

        qList = userQuery.where(User::getUsername).trim().like(username)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().trim().startsWith(username))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(User::getUsername).length().eq(username.length())
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().length() == username.length())
                .collect(Collectors.toList());
        assertEquals(qList, fList);


        qList = userQuery.where(User::getUsername).startWith("M")
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().startsWith("M"))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(User::getUsername).endsWith("s")
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().endsWith("s"))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(User::getUsername).lower().contains("s")
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().toLowerCase().contains("s"))
                .collect(Collectors.toList());
        assertEquals(qList, fList);

        qList = userQuery.where(User::getUsername).upper().contains("S")
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getUsername().toUpperCase().contains("S"))
                .collect(Collectors.toList());
        assertEquals(qList, fList);
    }

    @Test
    public void testOffset() {
        userQuery.getList(100);
    }

    @Test
    public void testResultBuilder() {
        List<User> resultList = userQuery.getList(5, 10);
        List<User> subList = allUsers.subList(5, 5 + 10);
        assertEquals(resultList, subList);

        resultList = userQuery.getList(20);
        subList = allUsers.subList(20, allUsers.size());
        assertEquals(resultList, subList);

        List<Object[]> userIds = userQuery.select(User::getId)
                .getList(5, 10);
        List<Object[]> subUserIds = allUsers.subList(5, 5 + 10)
                .stream().map(it -> new Object[]{it.getId()})
                .collect(Collectors.toList());

        assertEqualsArrayList(userIds, subUserIds);

        resultList = userQuery.where(User::getId).in().getList();
        assertEquals(resultList.size(), 0);

        int count = userQuery.count();
        assertEquals(count, allUsers.size());

        User first = userQuery.getFirst();
        assertEquals(first, allUsers.get(0));

        first = userQuery.where(User::getId).eq(0).requireSingle();
        assertEquals(first, allUsers.get(0));

        first = userQuery.getFirst(10);
        assertEquals(first, allUsers.get(10));

        assertThrowsExactly(IllegalStateException.class, () -> userQuery.requireSingle());
        assertThrowsExactly(NullPointerException.class, () -> userQuery.where(User::getId).eq(-1).requireSingle());

        assertTrue(userQuery.exist());
        assertTrue(userQuery.exist(allUsers.size() - 1));
        assertFalse(userQuery.exist(allUsers.size()));

        List<UserInterface> userInterfaces = userQuery
                .projected(UserInterface.class)
                .getList();
        assertEquals(userInterfaces.get(0), userInterfaces.get(0));

        List<UserModel> userModels = userQuery.projected(UserModel.class)
                .getList();


        List<Map<String, Object>> l0 = allUsers.stream()
                .map(UserModel::new)
                .map(UserInterface::asMap)
                .collect(Collectors.toList());

        List<Map<String, Object>> l1 = userInterfaces.stream()
                .map(UserInterface::asMap)
                .collect(Collectors.toList());
        List<Map<String, Object>> l2 = userModels.stream()
                .map(UserInterface::asMap)
                .collect(Collectors.toList());

        assertEquals(l0, l1);
        assertEquals(l0, l2);

    }

    @Test
    public void testAttr() {
        User first = userQuery.orderBy(Attribute.of(User::getId)).desc().getFirst();
        ArrayList<User> users = new ArrayList<>(allUsers);
        users.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
        User f = users.stream().findFirst().orElse(null);
        assertEquals(first, f);

        first = userQuery.orderBy(Attribute.of(User::getUsername)).desc().getFirst();

        users = new ArrayList<>(allUsers);
        users.sort((a, b) -> b.getUsername().compareTo(a.getUsername()));
        f = users.stream().findFirst().orElse(null);
        assertEquals(first, f);

        first = userQuery.orderBy(Attribute.of(User::isValid)).desc().getFirst();
        users = new ArrayList<>(allUsers);
        users.sort((a, b) -> Boolean.compare(b.isValid(), a.isValid()));
        f = users.stream().findFirst().orElse(null);
        assertEquals(first, f);

        first = userQuery
                .where(Attribute.of((Attribute<User, Boolean>) User::isValid)).eq(true)
                .getFirst();

        f = allUsers.stream()
                .filter(User::isValid)
                .findFirst()
                .orElse(null);
        assertEquals(first, f);

        List<User> resultList = userQuery
                .where(EntityAttribute.of(User::getParentUser).map(User::isValid))
                .eq(true)
                .getList();
        List<User> fList = allUsers.stream()
                .filter(user -> user.getParentUser() != null && user.getParentUser().isValid())
                .collect(Collectors.toList());

        assertEquals(resultList, fList);
    }

    @Test
    public void testWhereable() {
        List<User> resultList = userQuery
                .where(User::getParentUser).map(User::getUsername).eq(username)
                .getList();
        List<User> fList = allUsers.stream()
                .filter(user -> user.getParentUser() != null && username.equals(user.getParentUser().getUsername()))
                .collect(Collectors.toList());
        assertEquals(resultList, fList);

        resultList = userQuery
                .whereNot(User::getParentUser).map(User::getUsername).eq(username)
                .getList();
        fList = allUsers.stream()
                .filter(user -> user.getParentUser() != null && !username.equals(user.getParentUser().getUsername()))
                .collect(Collectors.toList());
        assertEquals(resultList, fList);


        resultList = userQuery
                .whereNot((Attribute<User, String>) User::getUsername).eq(username)
                .getList();
        fList = allUsers.stream()
                .filter(user -> !username.equals(user.getUsername()))
                .collect(Collectors.toList());
        assertEquals(resultList, fList);


        resultList = userQuery
                .whereNot((ComparableAttribute<User, String>) User::getUsername).eq(username)
                .getList();
        fList = allUsers.stream()
                .filter(user -> !username.equals(user.getUsername()))
                .collect(Collectors.toList());
        assertEquals(resultList, fList);


        resultList = userQuery
                .whereNot(User::getUsername).eq(username)
                .getList();
        fList = allUsers.stream()
                .filter(user -> !username.equals(user.getUsername()))
                .collect(Collectors.toList());
        assertEquals(resultList, fList);
    }

    @Test
    public void testPathBuilder() {
        List<User> resultList = userQuery.where(User::getParentUser)
                .map(User::getParentUser).map(User::getUsername).eq(username)
                .getList();
        List<User> fList = allUsers.stream()
                .filter(user -> {
                    User p = user.getParentUser();
                    return p != null && p.getParentUser() != null && username.equals(p.getParentUser().getUsername());
                })
                .collect(Collectors.toList());
        assertEquals(resultList, fList);

        resultList = userQuery.where(User::getParentUser)
                .map(User::getRandomNumber).eq(5)
                .getList();
        fList = allUsers.stream()
                .filter(user -> {
                    User p = user.getParentUser();
                    return p != null && p.getRandomNumber() == 5;
                })
                .collect(Collectors.toList());
        assertEquals(resultList, fList);

        resultList = userQuery.where(User::getParentUser)
                .map((Attribute<User, Integer>) User::getRandomNumber).eq(5)
                .getList();
        fList = allUsers.stream()
                .filter(user -> {
                    User p = user.getParentUser();
                    return p != null && p.getRandomNumber() == 5;
                })
                .collect(Collectors.toList());
        assertEquals(resultList, fList);
    }

    @NotNull
    private IntStream getUserIdStream() {
        return allUsers.stream().mapToInt(User::getRandomNumber);
    }

}
