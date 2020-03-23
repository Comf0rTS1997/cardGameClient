package cardGameClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class tester {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c = new Client();
		Thread cT = new Thread(c);
		cT.start();
	}
}
