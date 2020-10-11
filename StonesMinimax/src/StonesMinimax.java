import java.util.Scanner;

/*
 * Rules of the Game:
 * 		The game begins with a pile of N stones. Two players take turns taking
 * 		stones from the pile. In the first move of the game, the first player
 * 		can take up to (N / 2) stones. For every subsequent move, that player
 * 		can take up to double the number of stones the other player took in the
 * 		previous turn. Whichever player takes the last stone wins.
 * 
 * My AI:
 * 		Using a minimax algorithm on all possible outcomes.
 */

/*
 * TODO: DOESN'T WORK YET IF PLAYER STARTS
 * TODO: IMPLEMENT ALPHA-BETA PRUNING FOR EFFICIENCY
 */

public class StonesMinimax {

	static Scanner scanner = new Scanner(System.in);
	final static int STARTING_STONES = 25;
	
	public static void main(String[] args) {
		boolean opponentStarts = getValidFirstTurn();
		TreeNode root = makeTree(STARTING_STONES, opponentStarts);
		root.makeDecision();
		printPath(root);
		System.out.println(root.getNumChildren());
		System.out.println("root children [11] best child: \n" + root.getChildren()[11].getBestChild() + "\n\n");
		playGame(root, opponentStarts);
	}
	
	public static void playGame(TreeNode root, boolean opponentStarts) {
		boolean myTurn = !opponentStarts;
		if (myTurn == root.getIsOpponent()) return;
		System.out.println("We start with " + root.getNumStones() + " stones");
		TreeNode current = root;
		while (current.getNumStones() > 0) {
			if (myTurn) {
				current = myMove(current);
			}
			else {
				current = opponentMove(current);
			}
			myTurn = !myTurn;
		}
		System.out.println(myTurn ? "Opponent won" : "I won");
	}
	
	private static boolean getValidFirstTurn() {
		System.out.println("Who should go first? Type 1 for the AI or 2 for the player.");
		String first = scanner.nextLine();
		while (!(first.equals("1") || first.equals("2"))) {
			System.out.println("That's not a valid response. Type 1 for the AI or 2 for the player.");
			first = scanner.nextLine();
		}
		return first.equals("2");
	}
	
	public static TreeNode myMove(TreeNode current) {
		TreeNode bestChild = current.getBestChild();
		System.out.println(current);
		int stonesTaken = current.getNumStones() - bestChild.getNumStones();
		System.out.println("I take " + stonesTaken + (stonesTaken == 1 ? " stone" : " stones"));
		System.out.println(bestChild.getNumStones() + " stones remain");
		return bestChild;
	}

	public static TreeNode opponentMove(TreeNode current) {
		int stonesTaken = getValidOpponentMove(min(current.getNumStones(), current.getNumChildren()));
		TreeNode bestChild = current.getChildren()[stonesTaken-1];
		System.out.println("You take " + stonesTaken + (stonesTaken == 1 ? " stone" : " stones"));
		System.out.println(bestChild.getNumStones() + " stones remain");
		return bestChild;
	}
	
	public static int getValidOpponentMove(int max) {
		System.out.println("How many stones would you like to take?");
		System.out.println("You can take between 1 and " + max + " stones");
		int stonesTaken = scanner.nextInt(); 
		while (stonesTaken < 1 || stonesTaken > max) {
			System.out.println("You cannot take " + stonesTaken + " stones");
			System.out.println("Please choose between 1 and " + max + " stones");
			stonesTaken = scanner.nextInt();
		}
		return stonesTaken;
	}
	
	public static TreeNode makeTree(int numStones, boolean opponentStarts) {
		TreeNode root = new TreeNode(numStones, opponentStarts);
		TreeNode[] children = new TreeNode[numStones / 2];
		for (int i = 1; i <= children.length; i++) {
			children[i-1] = makeSubTree(numStones - i, i, !opponentStarts);
		}
		root.setChildren(children);
		return root;
	}
	
	public static TreeNode makeSubTree(int numStones, int prevMove, boolean isOpponent) {
		TreeNode node = new TreeNode(numStones, isOpponent);
		TreeNode[] children = new TreeNode[min(numStones, 2 * prevMove)];
		for (int i = 1; i <= children.length; i++) {
			children[i-1] = makeSubTree(numStones - i, i, !isOpponent);
		}
		node.setChildren(children);
		return node;
	}
	
	private static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	public static void printPath(TreeNode root) {
		boolean myTurn = true;
		TreeNode current = root;
		while (current.getBestChild() != null) {
			System.out.println(myTurn ? "My turn:" : "Opponent's turn:");
			int numTaken = current.getNumStones() - current.getBestChild().getNumStones();
			System.out.println("Take " + numTaken + " stones");
			current = current.getBestChild();
			myTurn = !myTurn;
		}
		System.out.println(myTurn ? "You Lost" : "You Won");
	}
}