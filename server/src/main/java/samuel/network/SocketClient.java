package samuel.network;

import com.fasterxml.jackson.core.type.TypeReference;
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
import samuel.jackson.CustomMapper;


public class SocketClient implements NetworkClient {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    private final ObjectMapper objectMapper = CustomMapper.getInstance();

    private final List<Consumer<Message>> listeners = new ArrayList<>();
    private final Map<String, BlockingQueue<Message>> pendingRequests = new ConcurrentHashMap<>();

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.startReaderThread();
    }

    public void sendData(Message message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            out.write(json + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T requestData(Message message, Class<T> clazz) {
        try {
            BlockingQueue<Message> responseQueue = new LinkedBlockingQueue<>();

            UUID requestId = message.getRequestId();

            pendingRequests.put(requestId.toString(), responseQueue);

            message.setRequestId(requestId);
            sendData(message);
            Message res = responseQueue.take();

            pendingRequests.remove(requestId.toString());

            return objectMapper.convertValue(res.getData(), clazz);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T requestData(Message message, TypeReference<T> typeReference) {
        try {

            BlockingQueue<Message> responseQueue = new LinkedBlockingQueue<>();

            UUID requestId = message.getRequestId();

            pendingRequests.put(requestId.toString(), responseQueue);

            message.setRequestId(requestId);
            sendData(message);
            Message res = responseQueue.take();

            pendingRequests.remove(requestId.toString());

            return objectMapper.convertValue(res.getData(), typeReference);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    public void addListener(Consumer<Message> listener) {
        this.listeners.add(listener);
    }




    private void startReaderThread() {
        Thread thread = new Thread(() -> {
            try {

                String line;
                while((line = in.readLine()) != null) {

                    Message msg = objectMapper.readValue(line, Message.class);

                    String requestId = msg.getRequestId().toString();

                    if(requestId != null && pendingRequests.containsKey(requestId)) {
//                    if(msg.getType().equals(MessageType.RESPONSE)) {

                        pendingRequests.get(requestId).put(msg);

                    } else {
                        for(Consumer<Message> listener : this.listeners) {
                            listener.accept(msg);
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

}
