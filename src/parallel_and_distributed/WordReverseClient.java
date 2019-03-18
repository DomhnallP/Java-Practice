package parallel_and_distributed;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class WordReverseClient {
	static final int MAX_T = 4;      
	public static void main(String[] args) throws UnknownHostException,
	IOException, ClassNotFoundException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("welcome client");
		Socket socket = new Socket("localhost", 4444);
		System.out.println("Client connected");
		Scanner in = new Scanner(socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		System.out.println("Ok");
		System.out.println("Enter string to reverse:");
		while (scanner.hasNextLine()) {
            out.println(scanner.nextLine());

            System.out.println("Receiving info from server ...");
            System.out.println(in.nextLine());
		}
	}
}