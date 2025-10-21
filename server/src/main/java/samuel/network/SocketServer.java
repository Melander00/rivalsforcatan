package samuel.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements NetworkServer {

    private final ServerSocket server;

    public SocketServer(int port) throws IOException {
        this.server = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));

        System.out.println("Bound server socket to: " + server.getInetAddress() + ":" + server.getLocalPort());


    }

    public NetworkClient acceptClient() {
        try {

            return new SocketClient(server.accept());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
