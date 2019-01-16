package de.htw.ai.kbe.commons;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.emptyList;

public class Commons<T> {
    public List<T> readSongsFromFile(String fileName, Class clazz) {
        try {
            InputStream json = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(json, type);
        } catch (IOException e) {
            System.out.println("File " + fileName + ".json can not be loaded!");
            return emptyList();
        }
    }
}
