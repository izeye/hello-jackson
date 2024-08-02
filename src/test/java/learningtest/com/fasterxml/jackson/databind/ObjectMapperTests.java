package learningtest.com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    @Test
    void writeValueAsStringWithObjectWithExceptionThrowingGetterLikeMethod() {
        ClassWithExceptionThrowingGetterLikeMethod object = new ClassWithExceptionThrowingGetterLikeMethod("Johnny");
        assertThatExceptionOfType(JsonMappingException.class).isThrownBy(() -> this.mapper.writeValueAsString(object));
    }

    @Test
    void writeValueAsStringWithObjectWithExceptionThrowingGetterLikeMethodWhenExceptionIgnoringObjectMapperIsUsed() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                return beanProperties.stream().map((writer) -> new BeanPropertyWriter(writer) {
                    @Override
                    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) {
                        try {
                            super.serializeAsField(bean, gen, prov);
                        }
                        catch (Exception ignored) {
                        }
                    }
                }).collect(Collectors.toList());
            }
        }));

        ClassWithExceptionThrowingGetterLikeMethod object = new ClassWithExceptionThrowingGetterLikeMethod("Johnny");
        assertThat(mapper.writeValueAsString(object)).isEqualTo("{\"name\":\"Johnny\"}");
    }

    @Data
    @AllArgsConstructor
    public static class ClassWithExceptionThrowingGetterLikeMethod {

        private String name;

        public String getBomb() {
            throw new RuntimeException("Boom!");
        }

    }

}
