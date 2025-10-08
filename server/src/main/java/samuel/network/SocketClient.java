package samuel.network;

import samuel.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import samuel.MessageType;


public class SocketClient {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Consumer<Message>> listeners = new ArrayList<>();
    private final Map<String, BlockingQueue<Message>> pendingRequests = new ConcurrentHashMap<>();

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.startReaderThread();
    }

    public void sendData(Message message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        out.write(json + "\n");
        out.flush();
    }

    public <T> T requestData(Message message, Class<T> clazz) throws IOException, InterruptedException {
        BlockingQueue<Message> responseQueue = new LinkedBlockingQueue<>();

        pendingRequests.put(MessageType.RESPONSE.toString(), responseQueue);
        sendData(message);
        Message res = responseQueue.take();
        pendingRequests.remove(MessageType.RESPONSE.toString());
        return objectMapper.convertValue(res.getData(), clazz);
    }



    public void addListener(Consumer<Message> listener) {
        this.listeners.add(listener);
    }




    public void startReaderThread() {
        Thread thread = new Thread(() -> {
            try {

                String line;
                while((line = in.readLine()) != null) {

                    Message msg = objectMapper.readValue(line, Message.class);

                    if(msg.getType().equals(MessageType.RESPONSE)) {
                        pendingRequests.get(MessageType.RESPONSE).put(msg);
                    } else {
                        for(Consumer<Message> listener : this.listeners) {
                            listener.accept(msg);
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

}
