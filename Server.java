package src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Server {
	private static final String PLAYER_WINS = "You open the door and push through to the outside.The light is blinding after *the time that you spent in the dark, but the warmth of the sun brings a smile to*your face. Ahead of you, you see a small village, bustling with activity. Relieved *by the thought of safety and civilization, you begin *walking toward the village. You win!*";
	private static final String DELIMETER = "*";
	private static int STATE = 1;
	private static boolean isAlive = true;
	private static boolean playerHasWon = false;

	/*death booleans*/
	private static boolean stateFourDeath = false;
	private static boolean stateSixDeath = false;
	private static boolean stateSevenDeathC = false;
	private static boolean stateSevenDeathD = false;
	private static boolean stateEightDeath = false;
	private static boolean stateNineDeath = false;
	private static boolean stateTenDeath = false;

	/*result booleans*/
	private static boolean resultStateOne = false;
	private static boolean resultStateTwo = false;
	private static boolean resultStateThreeAndFive = false;
	private static boolean resultStateFour = false;
	private static boolean resultStateSix = false;
	private static boolean resultStateSeven = false;
	private static boolean resultStateEight = false;
	private static boolean resultStateNineB = false;
	private static boolean resultStateNineC = false;

	/*stalemate booleans*/
	private static boolean tooDark = false;
	private static boolean noEffect = false;

	/*death messages*/
	private static final String STATE_FOUR_DEATH = "*You open the door, and step through it to the room beyond.*Inside the room, 3 skeletons turn to face you. They are each*holding swords. One approaches you and swings its sword at you.*You get hit, and die. Game over! Tip: If only you had a sword*of your own!*You died! Game Over!*";
	private static final String STATE_SIX_DEATH = "*The skeleton's sword cuts through the torch and follows*through toward your body. You get hit, and die. Tip:*Maybe you should use your sword instead!*"; 
	private static final String STATE_SEVEN_DEATH_C = "*The two skeletons from the back catch up to you and*attack you together. You get hit, and die. Tip:*You can beat the first skeleton! Try the sword!*";
	private static final String STATE_SEVEN_DEATH_D = "*You hit the skeleton with your torch. It stops for a moment,*lets out a boney laugh, and swings at you once more.*The skeleton's sword cuts through the torch and follows*through toward your body. You get hit, and die. Tip:*Maybe you should use your sword instead!*";
	private static final String STATE_EIGHT_DEATH = "*You reach out to take the torch that fell, but the skeleton*uses this opportunity to slice you. You get hit, and die.*Tip: The torch is just a distraction! Use your sword instead!*";
	private static final String STATE_NINE_DEATH = "*You attempt to attack the skeleton, but you are not*nearly fast enough. The skeleton's jab connects.*You get hit, and die. Tip: You cannot possibly attack*the skeleton in time. You'll have to go on the defensive!*";
	private static final String STATE_TEN_DEATH = "*The skeletons quickly overwhelm you. You get hit, and die.*Tip: There is no way that you can take on this many skeletons*at once! It's time to get out of there!*";


	/*result messages*/
	private static final String RESULT_STATE_ONE = "*You grab the unlit torch.*";
	private static final String RESULT_STATE_TWO = "*You light the torch using the candle on the*table. The room is illuminated, and you can now see.*";
	private static final String RESULT_STATE_THREE_AND_FIVE = "*You open the door, and step through it to the room beyond.*";
	private static final String RESULT_STATE_FOUR = "*You reach down and pick up the sword on the ground. *It's heavy, but it is better than nothing.*";
	private static final String RESULT_STATE_SIX = "*You raise your sword quickly and parry the skeletons attack.*";
	private static final String RESULT_STATE_SEVEN = "*You swing your sword with all of your strength and hit the skeleton. *It crumbles to the ground and stops moving.*";
	private static final String RESULT_STATE_EIGHT = "*You block the attack of the skeleton, and you swing your*sword with all of your strength and hit the skeleton.*It crumbles to the ground and stops moving.*";
	private static final String RESULT_STATE_NINE_B = "*You barely block the attack of the skeleton, and you swing*your sword with all of your strength and hit the skeleton.*It crumbles to the ground and stops moving.*";
	private static final String RESULT_STATE_NINE_C = "*You swiftly evade the sword of the skeleton, and move to*the left. The skeleton's sword narrowly misses you. You*once again swing your sword at the skeleton, causing it to collapse.*"; 

	/*stalemate messages*/
	private static final String TOO_DARK = "*It is too dark to see where you are going.*"; 
	private static final String NO_EFFECT = "*Previous action had no effect.*"; 

	/*situations*/
	private static final String SITUATION_1 = "*You awake in a room, and stand up. You are next to a table,*with an unlit torch and a lit candle on it. It is too dark to*see around the room. What will you do?*";
	private static final String SITUATION_2 = "*You are holding the unlit torch. You are next to a table,*with a lit candle on it. It is too dark to see around the*room. What will you do?";
	private static final String SITUATION_3 = "*Holding the lit torch, you observe the room. The walls are*made of stone, and there is a wooden door in front of you.*";
	private static final String SITUATION_4 = "*With the torch in hand, you see a sword on the ground.*Across the room, you see another wooden door.*";
	private static final String SITUATION_5 = "*You have a torch in one hand, and a sword in another.*Across the room, you see another wooden door.*";
	private static final String SITUATION_6 = "*Inside the room, 3 skeletons turn to face you. They are each holding*swords. One approaches you and swings its sword at you. What will you*do to defend yourself?*";
	private static final String SITUATION_7 = "*The skeleton falls off balance after you block its attack.*The 2 skeletons in the back get up and begin walking towards*you. What will you do?*";
	private static final String SITUATION_8 = "*The 2 skeletons approach you. One knocks the torch*out of your hand while the other swings its sword at*you. What will you do now?*";
	private static final String SITUATION_9 = "*The third and final skeleton takes this opportunity*to stab its sword towards you. The blade is a foot*away from your body. There is no way that you can*counterattack in time. What will you do?*";
	private static final String SITUATION_10 = "*With your enemies vanquished, you gaze across to the*other side of the room. The flame from your torch on*the ground is quickly losing its light. You see a large*door. Behind you, you hear the loud rattling of skeletons*moving closer to you. You realize that this is your best*chance to escape! What will you do?*"; 

	/*responses to situations*/
	private static final String[] S1_RESPONSES = {"a. Look around", "b. Take/grab torch", "c. Walk"};
	private static final String[] S2_RESPONSES = {"a. Look around", "b. Light Torch", "c. Walk"};
	private static final String[] S3_S5_RESPONSES = {"a. Look around", "b. Open door"};
	private static final String[] S4_RESPONSES = {"a. Look around", "b. Open door", "c. Take Sword"};
	private static final String[] S6_RESPONSES = {"a. Look around", "b. Use sword/Parry/Block", "c. Use torch"};
	private static final String[] S7_RESPONSES = {"a. Look around", "b. Use sword/Attack/Swing", "c. Run/Escape", "d. Use torch"};
	private static final String[] S8_RESPONSES = {"a. Look around", "b. Use sword/Attack/Swing/Block", "c. Use/grab/take torch"};
	private static final String[] S9_RESPONSES = {"a. Look around", "b. Use sword/Block", "c. Dodge", "d. Attack/Swing"};
	private static final String[] S10_RESPONSES = {"a. Look around", "b. Use sword/Attack/Swing/Fight", "c. Use/Open Door/Run/Escape"};

	/*map situations to responses*/
	private static final Map<String, String[]> SITUATION_TO_RESPONSES = new HashMap<String, String[]>();
	static {
		SITUATION_TO_RESPONSES.put(SITUATION_1, S1_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_2, S2_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_3, S3_S5_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_4, S4_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_5, S3_S5_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_6, S6_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_7, S7_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_8, S8_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_9, S9_RESPONSES);
		SITUATION_TO_RESPONSES.put(SITUATION_10, S10_RESPONSES);
	}

	/*map ints to situations*/
	private static final Map<Integer, String> INT_TO_SITUATION = new HashMap<Integer, String>();
	static {
		INT_TO_SITUATION.put(1, SITUATION_1);
		INT_TO_SITUATION.put(2, SITUATION_2);
		INT_TO_SITUATION.put(3, SITUATION_3);
		INT_TO_SITUATION.put(4, SITUATION_4);
		INT_TO_SITUATION.put(5, SITUATION_5);
		INT_TO_SITUATION.put(6, SITUATION_6);
		INT_TO_SITUATION.put(7, SITUATION_7);
		INT_TO_SITUATION.put(8, SITUATION_8);
		INT_TO_SITUATION.put(9, SITUATION_9);
		INT_TO_SITUATION.put(10, SITUATION_10);
	}

	public static void main(String[] args) {
		final int portNumber = 9090;
		try {
			ServerSocket listener = new ServerSocket(portNumber);
			Socket socket = listener.accept();
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String connected = input.readLine();
			while (connected.equals("1")) {
				System.out.println("connected");

				//send situation
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				String currentState = INT_TO_SITUATION.get(STATE) + DELIMETER + printArray(SITUATION_TO_RESPONSES.get(INT_TO_SITUATION.get(STATE)));

				if (isAlive) {
					if (playerHasWon) {
						out.println(DELIMETER + PLAYER_WINS + DELIMETER + currentState);
						playerHasWon = false;
					} else {
						String output = "";
						if (resultStateOne) {
							output = DELIMETER + RESULT_STATE_ONE + DELIMETER;
							resultStateOne = false;
						} else if (resultStateTwo) {
							output = DELIMETER + RESULT_STATE_TWO + DELIMETER;
							resultStateTwo = false;
						} else if (resultStateThreeAndFive) {
							output = DELIMETER + RESULT_STATE_THREE_AND_FIVE + DELIMETER;
							resultStateThreeAndFive = false;
						} else if (resultStateFour) {
							output = DELIMETER + RESULT_STATE_FOUR + DELIMETER;
							resultStateFour = false;
						} else if (resultStateSix) {
							output = DELIMETER + RESULT_STATE_SIX + DELIMETER;
							resultStateSix = false;
						} else if (resultStateSeven) {
							output = DELIMETER + RESULT_STATE_SEVEN + DELIMETER;
							resultStateSeven = false;
						} else if (resultStateEight) {
							output = DELIMETER + RESULT_STATE_EIGHT + DELIMETER;
							resultStateEight = false;
						} else if (resultStateNineB) {
							output = DELIMETER + RESULT_STATE_NINE_B + DELIMETER;
							resultStateNineB = false;
						} else if (resultStateNineC) {
							output = DELIMETER + RESULT_STATE_NINE_C + DELIMETER;
							resultStateNineC = false;
						} else if (tooDark) {
							output = DELIMETER + TOO_DARK + DELIMETER;
							tooDark = false;
						} else if (noEffect) {
							output = DELIMETER + NO_EFFECT + DELIMETER;
							noEffect = false;
						}
						output += currentState;
						out.println(output);
					}
				} else {
					if (stateFourDeath) {
						out.println(DELIMETER + STATE_FOUR_DEATH + DELIMETER + currentState);
						stateFourDeath = false;
					} else if (stateSixDeath) {
						out.println(DELIMETER + STATE_SIX_DEATH + DELIMETER + currentState);
						stateSixDeath = false;
					} else if (stateSevenDeathC) {
						out.println(DELIMETER + STATE_SEVEN_DEATH_C + DELIMETER + currentState);
						stateSevenDeathC = false;
					} else if (stateSevenDeathD) {
						out.println(DELIMETER + STATE_SEVEN_DEATH_D + DELIMETER + currentState);
						stateSevenDeathD = false;
					} else if (stateEightDeath) {
						out.println(DELIMETER + STATE_EIGHT_DEATH + DELIMETER + currentState);
						stateEightDeath = false;
					} else if (stateNineDeath) {
						out.println(DELIMETER + STATE_NINE_DEATH + DELIMETER + currentState);
						stateNineDeath = false;
					} else if (stateTenDeath) {
						out.println(DELIMETER + STATE_TEN_DEATH + DELIMETER + currentState);
						stateTenDeath = false;
					}
					isAlive = true;
				}

				//read option
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String option = input.readLine();
				switch (STATE) {
				case 1:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						resultStateOne = true;
						STATE++;
					} else if (option.equals("c")) {
						tooDark = true;
					}
				case 2:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						STATE++;
					} else if (option.equals("c")) {
						tooDark = true;
					}
					break;
				case 3:
				case 5:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						resultStateThreeAndFive = true;
						STATE++;
					}
					break;
				case 4:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						isAlive = false;
						stateFourDeath = true;
						STATE = 1;
					} else if (option.equals("c")) {
						resultStateFour = true;
						STATE++;
					}
					break;
				case 6:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						resultStateSix = true;
						STATE++;
					} else if (option.equals("c")) {
						isAlive = false;
						stateSixDeath = true;
						STATE = 1;
					}
					break;
				case 7:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						resultStateSeven = true;
						STATE++;
					} else if (option.equals("c")) {
						isAlive = false;
						stateSevenDeathC = true;
						STATE = 1;
					} else if (option.equals("d")) {
						isAlive = false;
						stateSevenDeathD = true;
						STATE = 1;
					}
					break;
				case 8:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						resultStateEight = true;
						STATE++;
					} else if (option.equals("b")) {
						isAlive = false;
						stateEightDeath = true;
						STATE = 1;
					}
					break;
				case 9:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						resultStateNineB = true;
						STATE++;
					} else if (option.equals("c")) {
						resultStateNineC = true;
						STATE++;
					} else if (option.equals("d")) {
						isAlive = false;
						stateNineDeath = true;
						STATE = 1;
					}
					break;
				case 10:
					if (option.equals("a")) {
						noEffect = true;
					} else if (option.equals("b")) {
						isAlive = false;
						stateTenDeath = true;
						STATE = 1;
					} else if (option.equals("c")) {
						playerHasWon = true;
						STATE = 1;
					}
					break;
				}
			}
			listener.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String printArray(final String[] array) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			result += array[i] + DELIMETER;
		}
		return result + DELIMETER;
	}

}
