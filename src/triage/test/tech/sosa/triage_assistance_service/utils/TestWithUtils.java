package tech.sosa.triage_assistance_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONObject;

public class TestWithUtils {

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

    protected String readFromResource(String resource) throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(resource)).toURI());

        Stream<String> lines = Files.lines(path);
        String source = lines.collect(Collectors.joining("\n"));
        lines.close();
        return source;
    }

    protected String ignoreJSONWhitespaces(String aString) {
        return aString.replaceAll("\\s+", "");
    }

    protected String ignoreJSONKeyOrder(String jsonString) {
        return new JSONObject(jsonString).toString();
    }

    protected <T> T readJSON(String source, Class<T> returnType) throws IOException {
        return objectMapper.readValue(source, returnType);
    }

    protected String writeJSON(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    protected <T, U> boolean equalsIgnoreOrder(Collection<T> c1, Collection<U> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        return new HashSet<>(c1).equals(new HashSet<>(c2));
    }

}
