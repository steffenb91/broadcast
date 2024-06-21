package com.steffenboe.broadcast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class BroadcastTest {

    /**
     * We start three server instances, each connected to another one and and a
     * fourth in client mode, that sends "Hello World" message to the first one. We
     * expect the message to be propagated to all nodes.
     * 
     */
    @Test
    public void testBroadcast() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        startServerWithArgs("8080", "8081");
        startServerWithArgs("8081", "8082");
        startServerWithArgs("8082");
        startServerWithArgs("c");

        String expectedOutput = "Starting on port 8080...\n" + //
                "Starting on port 8081...\n" + //
                "Starting on port 8082...\n" + //
                "Received: Hello World\n" + //
                "Sending message to localhost:8081...\n" + //
                "Sent: Hello World to localhost:8081\n" + //
                "Received: Hello World\n" + //
                "Sending message to localhost:8082...\n" + //
                "Sent: Hello World to localhost:8082\n" + //
                "Received: Hello World\n";
        String actualOutput = outContent.toString();
        assertThat(actualOutput, is(expectedOutput));
    }

    private void startServerWithArgs(String... args) throws InterruptedException {
        Thread.ofVirtual().start(() -> {
            try {
                Main.main(args);
            } catch (Exception e) {
                fail(e);
            }
        });
        Thread.sleep(1000);
    }
}
