package com.steffenboe.broadcast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class BroadcastServerTest {

	private Server server;

	@Test
	public void shouldReceiveSentMessage() {
		ByteArrayOutputStream outContent = redirectSystemOut();
		startServer(new BroadcastingNode(new HashSet<>()));
		sendMessage();
		sleep(1000);
		assertThat(outContent.toString(), is("Received: hello\n"));
	}

	private void sendMessage() {
		try (Socket socket = new Socket("localhost", 8080)) {
			socket.getOutputStream().write("hello".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ByteArrayOutputStream redirectSystemOut() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
		return outContent;
	}

	private void verifyBroadcastOnMessage() {
		sendMessage();
	}

	private void startServer(BroadcastingNode node) {
		server = new Server("8080", node);
		Thread.ofVirtual().start(() -> {
			server.start();
		});

	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
