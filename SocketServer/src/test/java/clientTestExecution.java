import model.ClientTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;


public class clientTestExecution {
    private Scanner sc;
    private ClientTest clientTest;
    private String response;
    private String request;

    @BeforeEach
    void startClient() {
        try {
            this.clientTest = new ClientTest("127.0.0.1", 6666);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testClientServerComms() {
        try {
            this.response = this.clientTest.receiveMessage();
            System.out.println(this.response);

            System.out.println("> signUp");
            System.out.println("Enter username: ");
            System.out.println("Marsel");
            this.clientTest.sendMessage("Marsel");
            System.out.println("Enter password:");
            System.out.println("qwerty007");
            this.clientTest.sendMessage("qwerty007");

            this.response = this.clientTest.receiveMessage();
            System.out.println(this.response);

            this.clientTest.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
