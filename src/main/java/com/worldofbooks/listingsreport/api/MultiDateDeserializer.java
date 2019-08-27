package com.worldofbooks.listingsreport.api;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MultiDateDeserializer extends StdDeserializer<LocalDate> {
    private static final long serialVersionUID = 1L;

    private static final String[] DATE_FORMATS = new String[]{
        "M/d/yyyy",
        "M/dd/yyyy",
        "MM/d/yyyy",
        "MM/dd/yyyy"
    };

    public MultiDateDeserializer() {
        this(null);
    }

    public MultiDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        final String date = node.textValue();

        for (String DATE_FORMAT : DATE_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        throw new JsonParseException(jp, "Unable to parse date: \"" + date + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}

