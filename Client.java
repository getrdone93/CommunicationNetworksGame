package src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static final String DELIMETER = "*";
	private static final String FORMAT = "======================================================================";
	private static String WELCOME = FORMAT + "\nWelcome to Team Grapefruit's adventure game!";
	static {
		WELCOME += "\nIt will give you a situation, and several choices of action. If you";
		WELCOME += "\nselect the correct action, you will advance to the next situation. If";
		WELCOME += "\nyou answer incorrectly, you may die and have to start all over!";
		WELCOME += "\nUse your wit and intelligence to overcome all the challenges!\n";
		WELCOME += FORMAT + "\n";
	}
	public static void main(String[] args) {
		final int portNumber = 9090;
		String connected = "1";
		System.out.println(WELCOME);
		
		try {
			Socket socket = new Socket("localhost", portNumber);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(connected);
			while (connected.equals("1")) {
				//read in situation
				String situation = readInSituation(socket, new BufferedReader(new InputStreamReader(socket.getInputStream()))).replace(DELIMETER, "\n");
				System.out.println(situation);

				//send option
				Scanner in = new Scanner(System.in);
				System.out.print("Enter option: ");
				out.println(in.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String readInSituation(Socket socket, BufferedReader input) throws Exception {
		return input.readLine();
	}
}
