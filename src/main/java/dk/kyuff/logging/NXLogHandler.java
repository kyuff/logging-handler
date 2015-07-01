package dk.kyuff.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NXLogHandler extends Handler {

    // thread-safe
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void publish(LogRecord record) {
        String json = null;
        try {
            json = mapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            // discard as we cannot do a thing about it
            e.printStackTrace();
        }
        System.out.println(json);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
