package edu.home42.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
    private Socket socket;
    private InputStreamReader inputStream;
    private BufferedReader inputBuffer;

    private PrintWriter outputWriter;

    public ClientTest(String ip, Integer port) throws IOException {
        this.socket = new Socket(ip, port);
        this.outputWriter = new PrintWriter(this.socket.getOutputStream());
        this.inputStream = new InputStreamReader(this.socket.getInputStream());
        this.inputBuffer = new BufferedReader(this.inputStream);
    }

    public void sendMessage(String msg) {
        this.outputWriter.println(msg);
        this.outputWriter.flush();
    }

    public String receiveMessage() throws IOException {
        String line = new String();

        line = this.inputBuffer.readLine();

        return line;
    }

    public void close() throws IOException {
        this.outputWriter.close();
        this.inputBuffer.close();
        this.inputStream.close();
        this.socket.close();
    }
}
