package cardGameClient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client implements Runnable{
	private Socket soc;
	private PrintWriter out;
	private BufferedReader in;
	private String serverIP;
	private int port;
	private boolean running;
	
	public Client() {
		this.serverIP = "attu8.cs.washington.edu";
		this.port = 8888;
		this.running = false;
	}
	
	public void startConnection() throws UnknownHostException, IOException {
		System.out.println("Trying to connect...");
		this.soc = new Socket(this.serverIP, this.port);
		System.out.println("Connected!");
		out = new PrintWriter(soc.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		
	}

	@Override
	public void run() {
		this.running = true;
		try {
			startConnection();
			response();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void response() throws IOException, InterruptedException {
		while(running) {
			String si = in.readLine(); // server instruction
			char siC = si.trim().toUpperCase().charAt(0);
			System.out.println("ServerInstruction: " + si); // debug
			switch(siC) {
				case 'P':
					playGame();
					break;
				case 'W':
					System.out.println("You Win.");
					this.running = false;
					stop();
					break;
				case 'L':
					this.running = false;
					System.out.println("You lost.");
					stop();
					break;
				case 'S':
					playGame();
					break;
			}
			Thread.sleep(500);
		}
	}
	
	private void playGame() {
		Scanner s = new Scanner(System.in);
		System.out.println("(R)ock (P)aper (S)cissors:");
		boolean gotInput = false;
		String userInput = "";
		int resultToServer = -1;
		while(!gotInput) {
			userInput = s.nextLine();
			userInput = userInput.toUpperCase();
			char userInputChar = userInput.charAt(0);
			switch(userInputChar){
				case 'R':
					resultToServer = 3;
					gotInput = true;
					break;
				case 'P':
					resultToServer = 1;
					gotInput = true;
					break;
				case 'S':
					resultToServer = 2;
					gotInput = true;
					break;
			}	
		}
		System.out.println("Return to server: " + resultToServer);		
		out.println(resultToServer);
	}
	
	public void stop() throws IOException {
		soc.close();
		in.close();
		out.close();
	}
	
}
