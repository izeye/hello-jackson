package learningtest.com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests with first single-character word in field name.
 *
 * @author Johnny Lim
 */
class FirstSingleCharacterWordTests {

    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void writeValueAsString() throws JsonProcessingException {
        String value = "test";

        SomeData someData = new SomeData();
        someData.setAValue(value);

        assertThat(this.mapper.writeValueAsString(someData)).isEqualTo("{\"avalue\":\"test\"}");
    }

    @Test
    void readValue() throws JsonProcessingException {
        String value = "test";

        SomeData someData = new SomeData();
        someData.setAValue(value);

        assertThat(this.mapper.readValue("{\"aValue\":\"test\"}", SomeData.class).getAValue()).isNull();
        assertThat(this.mapper.readValue("{\"avalue\":\"test\"}", SomeData.class).getAValue()).isEqualTo(value);
    }

    @Data
    static class SomeData {

        private String aValue;

    }

}
