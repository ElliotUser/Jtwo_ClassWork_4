package ru.rickSanchez.company.chat;

import ru.rickSanchez.company.network.ServerSocketThread;

public class ChatServer {

    ServerSocketThread server;

    public void start(int port) {
        if (server != null && server.isAlive())
            System.out.println("Already running");
        else
            server = new ServerSocketThread("Server", port);
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            System.out.println("Nothing to stop");
        } else {
            server.interrupt();
        }
    }

}
