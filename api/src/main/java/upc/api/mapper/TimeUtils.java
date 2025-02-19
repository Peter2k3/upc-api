package upc.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeUtils {

    public LocalDateTime toLocalDateTime(String fechaHora) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.readValue(fechaHora, LocalDateTime.class);
        return null;
    }

}
