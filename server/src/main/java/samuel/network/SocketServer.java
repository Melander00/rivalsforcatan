package samuel.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private final ServerSocket server;

    public SocketServer(int port) throws IOException {
        this.server = new ServerSocket(port);

        System.out.println("Server listening on port: " + port);


    }

    public Socket acceptClient() throws IOException {
        return server.accept();
    }

}
