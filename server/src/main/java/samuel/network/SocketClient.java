package samuel.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import samuel.Message;

import java.io.*;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;


public class SocketClient {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
    }

    public void sendData(Message message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        out.write(json + "\n");
        out.flush();
    }

    public Message awaitData() throws IOException {
        String json = in.readLine();
        Message request = objectMapper.readValue(json, Message.class);
        return request;
    }

    public Message requestData(Message message) throws IOException {
        sendData(message);
        return awaitData();
    }

}
