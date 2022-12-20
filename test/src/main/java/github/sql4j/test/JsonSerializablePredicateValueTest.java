package github.sql4j.test;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.sql4j.dsl.support.JsonSerializablePredicate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JsonSerializablePredicateValueTest {
    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void test() throws JsonProcessingException {

        Object[] values = {
                (byte) 1,
                (short) 1.0,
                1,
                1.0f,
                1L,
                1.0,
                false,
                new BigDecimal(10),
                new Date(System.currentTimeMillis()),
                "123123",
                new Time(System.currentTimeMillis()),
                Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis() / 1000))
        };

        for (Object value : values) {
            check(value);
        }


    }

    static void check(Object value) throws JsonProcessingException {
        JsonSerializablePredicate.Value value1 = new JsonSerializablePredicate.Value(value);
        String s = mapper.writeValueAsString(value1);
        System.out.println(value.getClass() + " --> " + s);

        JsonSerializablePredicate.Value value2 = mapper.readValue(s, JsonSerializablePredicate.Value.class);

        Object v1 = value1.value();
        Object v2 = value2.value();
        assertEquals(v1.getClass(), v2.getClass());
        assertEquals(v1.toString(), v2.toString());

    }

}
