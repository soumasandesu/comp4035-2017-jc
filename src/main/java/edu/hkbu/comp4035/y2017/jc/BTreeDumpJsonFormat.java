package edu.hkbu.comp4035.y2017.jc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

// Note: Classes and methods (if not static) are **final** to make sure that they won't be extended or being overriden.
public final class BTreeDumpJsonFormat {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final class Parser {
        public static BTreeNode fromString(String content) throws IOException {
            return objectMapper.readValue(content, BTreeNode.class);
        }
    }

    public static final class Stringfy {
        public static String asString(Object object) throws JsonProcessingException {
            return objectMapper.writeValueAsString(object);
        }
    }
}
