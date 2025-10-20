package samuel.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements NetworkServer {

    private final ServerSocket server;

    public SocketServer(int port) throws IOException {
        this.server = new ServerSocket(port);

        System.out.println("Server listening on port: " + port);


    }

    public NetworkClient acceptClient() {
        try {

            return new SocketClient(server.accept());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
