package com.worldofbooks.listingsreport.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MultiDateDeserializer extends StdDeserializer<Date> {
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
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        final String date = node.textValue();

        for (String DATE_FORMAT : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(DATE_FORMAT).parse(date);
            } catch (ParseException e) {
            }
        }
        throw new JsonParseException(jp, "Unable to parse date: \"" + date + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}

