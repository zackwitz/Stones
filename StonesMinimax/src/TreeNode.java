public class TreeNode {
	private TreeNode[] children;
	private int numStones;
	private int win; 	//1 for win, -1 for loss, 0 for unsure yet
	private boolean isOpponent;
	private TreeNode bestChild;
	
	public TreeNode(int numStones, boolean isOpponent) {
		this.numStones = numStones;
		this.win = 0;
		this.isOpponent = isOpponent;
		this.children = new TreeNode[0];
		this.bestChild = null;
	}
	
	public boolean makeDecision() {
		// test for leaf node
		if (numStones == 0) {
			win = isOpponent ? 1 : -1;
			return true;
		}
		// check children
		int bestWinSoFar = isOpponent ? 2 : -2;
		TreeNode bestChildSoFar = null;
		for (TreeNode child: children) {
			child.makeDecision();
			
			if (isOpponent && child.getWin() <= bestWinSoFar) {
				bestWinSoFar = child.getWin();
				bestChildSoFar = child;
				if (bestWinSoFar < 0) break;
			}
			else if (!isOpponent && child.getWin() >= bestWinSoFar) {
				bestWinSoFar = child.getWin();
				bestChildSoFar = child;
				if (bestWinSoFar > 0) break;
			}
		}
		if (bestChildSoFar == null || bestWinSoFar == 0) return false;
		this.win = bestWinSoFar;
		bestChild = bestChildSoFar;
		return true;
	}
	
	public int getNumStones() { return numStones; }
	public int getWin() { return win; }
	public boolean getIsOpponent() { return isOpponent; }
	public TreeNode getBestChild() { return bestChild; }
	public TreeNode[] getChildren() { return children; }
	public int getNumChildren() { return children.length; }
	public void setChildren(TreeNode[] children) { this.children = children; }
	
	public String toString() {
		String nodeString = "numStones: " + numStones + "\n";
		nodeString += "isOpponent: " + isOpponent + "\n";
		return nodeString;
	}
}