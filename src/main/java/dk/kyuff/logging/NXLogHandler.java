package dk.kyuff.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NXLogHandler extends Handler {

    // thread-safe
    private static final ObjectMapper mapper = new ObjectMapper();

    // Used to instantiate the scheduling
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private int port;
    private String host;

    private final Queue<String> queue = new ConcurrentLinkedQueue<>();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public NXLogHandler() {
        scheduler.scheduleAtFixedRate(this::flush, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void publish(LogRecord record) {
        if (record == null) {
            // silently ignore as per the javadoc
            return;
        }
        try {
            String json = mapper.writeValueAsString(record);
            queue.add(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush() {
        // use the mapper as a monitor since it's shared across the VM
        synchronized (mapper) {
            try (Socket socket = new Socket(host, port); PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
                for (String item = queue.poll(); item != null; item = queue.poll()) {
                    writer.append(item);
                }
            } catch (SocketException e) {
                // For some reason we were not able to connect. Try again later.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws SecurityException {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
