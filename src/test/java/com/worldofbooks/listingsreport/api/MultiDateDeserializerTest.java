package com.worldofbooks.listingsreport.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiDateDeserializerTest {

    @Autowired
    private JsonParser jsonParser;
    @Autowired
    private DeserializationContext deserializationContext;
    @Autowired
    private ObjectCodec objectCodec;
    @Autowired
    private JsonNode jsonNode;

    private static final String[] DATE_FORMATS = new String[]{
            "M/d/yyyy",
            "M/dd/yyyy",
            "MM/d/yyyy",
            "MM/dd/yyyy"
    };

    @Test
    public void deserializeOk() throws Exception {
        when(jsonParser.getCodec()).thenReturn(objectCodec);
        when(objectCodec.readTree(any())).thenReturn(jsonNode);

        List<String> datesAllowed = Arrays.asList(
                "2/2/2018",
                "2/22/2018",
                "12/22/2018",
                "12/2/2018");

        MultiDateDeserializer multiDateDeserializer = new MultiDateDeserializer(LocalDate.class);

        for (String date : datesAllowed) {
            when(jsonNode.textValue()).thenReturn(date);
            LocalDate localDate = getLocaldateFromString(date);

            assertEquals(localDate, multiDateDeserializer.deserialize(jsonParser, deserializationContext));
        }
    }

    @Test(expected = JsonParseException.class)
    public void deserializeNotAllow() throws IOException {
        when(jsonParser.getCodec()).thenReturn(objectCodec);
        when(objectCodec.readTree(any())).thenReturn(jsonNode);

        String date = "2/2018";

        MultiDateDeserializer multiDateDeserializer = new MultiDateDeserializer(LocalDate.class);

        when(jsonNode.textValue()).thenReturn(date);
        multiDateDeserializer.deserialize(jsonParser, deserializationContext);
    }

    private LocalDate getLocaldateFromString(String date) {
        LocalDate localDate = null;
        for (String DATE_FORMAT : DATE_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
                localDate = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        return localDate;
    }

    @Configuration
    public static class Config {

        @Bean(name = "TestDeserializationContextConfiguration")
        @Primary
        public DeserializationContext deserializationContext() {
            return Mockito.mock(DeserializationContext.class);
        }

        @Bean(name = "TestJsonNodeConfiguration")
        @Primary
        public JsonNode jsonNode() {
            return Mockito.mock(JsonNode.class);
        }

        @Bean(name = "TestObjectCodecConfiguration")
        @Primary
        public ObjectCodec objectCodec() throws IOException {
            ObjectCodec objectCodec = Mockito.mock(ObjectCodec.class);
//            when(objectCodec.readTree(any())).thenReturn(jsonNode);

            return objectCodec;
        }

        @Bean(name = "TestJsonParserConfiguration")
        @Primary
        public JsonParser jsonParser() {
            JsonParser jsonParser = Mockito.mock(JsonParser.class);
//            when(jsonParser.getCodec()).thenReturn(objectCodec);

            return jsonParser;
        }

    }
}
