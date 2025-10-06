package samuel.network;

import samuel.ui.Console;
import samuel.ui.UI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private final ServerSocket server;

    public SocketServer(int port, UI display) throws IOException {
        this.server = new ServerSocket(port);

        display.displayString("Server listening on port: " + port);


    }

    public Socket acceptClient() throws IOException {
        return server.accept();
    }

}
