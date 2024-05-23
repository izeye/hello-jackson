package learningtest.com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObjectMapper}.
 *
 * @author Johnny Lim
 */
class ObjectMapperTests {

    private static final Person PERSON = new Person("Johnny", "Lim");

    private static final String JSON = "{\"firstName\":\"Johnny\",\"lastName\":\"Lim\"}";

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void writeValueAsString() throws JsonProcessingException {
        assertThat(this.mapper.writeValueAsString(PERSON)).isEqualTo(JSON);
    }

    @Test
    void readValue() throws JsonProcessingException {
        assertThat(this.mapper.readValue(JSON, Person.class)).isEqualTo(PERSON);
    }

    @Test
    void readValueWithJsonAlias() throws JsonProcessingException {
        String json = "{\"firstNameVariant\":\"Johnny\",\"lastName\":\"Lim\"}";
        assertThat(this.mapper.readValue(json, Person.class)).isEqualTo(PERSON);
    }

}
