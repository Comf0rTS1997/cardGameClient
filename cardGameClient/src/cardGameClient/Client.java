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
		this.serverIP = "75.172.166.37";
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
	
	private void response() throws IOException {
		while(running) {
			String si = in.readLine(); // server instruction
			char siC = si.trim().toUpperCase().charAt(0);
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
			}
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
		out.print(resultToServer);		
	}
	
	public void stop() throws IOException {
		soc.close();
		in.close();
		out.close();
	}
	
}
