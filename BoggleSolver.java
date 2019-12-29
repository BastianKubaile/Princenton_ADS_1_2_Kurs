import java.util.HashSet;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private class Node {
	Node[] children = new Node[26];
	boolean isEndNode = false;
	int value = -1;// Index in dictionary array of this endnode
    }

    private class RTree {
	private Node root = new Node();

	public RTree(String[] dictionary) {
	    for (int j = 0; j < dictionary.length; j++) {
		String currString = dictionary[j];
		Node current = root;
		for (int i = 0; i < currString.length(); i++) {
		    char c = currString.charAt(i);
		    byte tmp = getCount(c);
		    if (current.children[tmp] == null) {
			current.children[tmp] = new Node();
		    }
		    if (i + 1 == currString.length()) {
			current.children[tmp].isEndNode = true;
			current.children[tmp].value = j;
		    }
		    if (c == 'q' || c == 'Q') {
			if (i + 2 == currString.length()) {// Q is the last
							   // character
			    current.children[tmp].isEndNode = true;
			    current.children[tmp].value = j;
			    break;
			}
			i++;
		    }
		    current = current.children[tmp];
		}
	    }
	}

	public Node getRoot() {
	    return root;
	}

    }

    private RTree tree;
    private String[] diction;

    public BoggleSolver(String[] dictionary) {
	tree = new RTree(dictionary);
	diction = new String[dictionary.length];
	for(int i = 0; i < diction.length; i++){
	    diction[i] = dictionary[i];
	}
    }

    private static byte getCount(char c) {// Returns the int starting a 0 from a
					  // character
	return (byte) (c - 65);
    }

    public int scoreOf(String word) {
	boolean exists = false;
	Node currentNode = tree.getRoot();
	String searchWord = word.toUpperCase().replace("QU", "Q");
	for (int i = 0; i < searchWord.length(); i++) {
	    byte c = getCount(searchWord.charAt(i));
	    if (currentNode == null)
		break;
	    if (i + 1 == searchWord.length() && currentNode.children[c] != null && currentNode.children[c].isEndNode) {
		exists = true;
		break;
	    }
	    currentNode = currentNode.children[c];
	}

	int wordlength = word.length();
	if (!exists || wordlength <= 2)
	    return 0;
	if (wordlength <= 4)
	    return 1;
	if (wordlength <= 5)
	    return 2;
	if (wordlength <= 6)
	    return 3;
	if (wordlength <= 7)
	    return 5;
	return 11;
    }
    

    public Iterable<String> getAllValidWords(BoggleBoard board) {
	AdjContainer[][] adjContainers = new AdjContainer[board.rows()][board.cols()];
	for(int x = 0; x < adjContainers.length; x++){
	    for(int y = 0; y < adjContainers[x].length; y++){
		boolean adjLeftBorder = false, adjRightBorder = false, adjTopBorder = false, adjBottomBorder = false;
		if (y == 0) adjLeftBorder = true;
		if (x == 0) adjTopBorder = true;
		if (x == board.rows() - 1) adjBottomBorder = true;
		if (y == board.cols() - 1) adjRightBorder = true;
		AdjContainer currContainer = new AdjContainer();
		if(!adjTopBorder){
		    Container tmp = new Container(x-1, y, null, null);
		    tmp.character = getCount(board.getLetter(x-1, y));
		    currContainer.adj.push(tmp);
		}
		if(!adjRightBorder){
		    Container tmp = new Container(x, y+1, null, null);
		    tmp.character = getCount(board.getLetter(x, y+1));
		    currContainer.adj.push(tmp);
		}
		if(!adjBottomBorder){
		    Container tmp = new Container(x+1, y, null, null);
		    tmp.character = getCount(board.getLetter(x+1, y));
		    currContainer.adj.push(tmp);
		    
		}
		if(!adjLeftBorder){
		    Container tmp = new Container(x, y-1, null, null);
		    tmp.character = getCount(board.getLetter(x, y-1));
		    currContainer.adj.push(tmp);
		    
		}
		if(!adjTopBorder && !adjLeftBorder){
		    Container tmp = new Container(x-1, y-1, null, null);
		    tmp.character = getCount(board.getLetter(x-1, y-1));
		    currContainer.adj.push(tmp);
		    
		}
		if(!adjTopBorder && !adjRightBorder){
		    Container tmp = new Container(x-1, y+1, null, null);
		    tmp.character = getCount(board.getLetter(x-1, y+1));
		    currContainer.adj.push(tmp);
		    
		}
		if(!adjBottomBorder && !adjLeftBorder){
		    Container tmp = new Container(x+1, y-1, null, null);
		    tmp.character = getCount(board.getLetter(x+1, y-1));
		    currContainer.adj.push(tmp);
		    
		}
		if(!adjBottomBorder && !adjRightBorder){
		    Container tmp = new Container(x+1, y+1, null, null);
		    tmp.character = getCount(board.getLetter(x+1, y+1));
		    currContainer.adj.push(tmp);
		    
		}
		adjContainers[x][y] = currContainer;
	    }
	}
	
	Stack<String> solution = new Stack<>();
	HashSet<String> foundWords = new HashSet<>();
	for (int i = 0; i < board.rows(); i++) {
	    for (int j = 0; j < board.cols(); j++) {
		search(i, j, solution, foundWords, tree.root.children[getCount(board.getLetter(i, j))], board,
			new boolean[board.rows()][board.cols()], adjContainers);
	    }
	}
	return solution;
    }
    

    private class Container {
	int x;
	int y;
	Node currentNode;
	boolean[][] visitted;
	byte character = -1;
	
	public Container(int x, int y , Node currentNode, boolean[][] visitted){
	    this.x = x;
	    this.y = y;
	    this.currentNode = currentNode;
	    this.visitted = copyArray(visitted);
	}

    }
    
    private class AdjContainer{
	Stack<Container> adj= new Stack<>();
    }

    private void search(int tx, int ty, Stack<String> solution, HashSet<String> foundWords, Node tcurrentNode,  BoggleBoard board, boolean[][] tvisitted,AdjContainer[][] adjContainers) {
	Stack<Container> nodes = new Stack<>();
	Container container = new Container(tx, ty, tcurrentNode, tvisitted);
	container.character = getCount(board.getLetter(tx, ty));
	nodes.push(container);
	while (!nodes.isEmpty()) {
	    Container currContainer = nodes.pop();
	    int x = currContainer.x;
	    int y = currContainer.y;
	    Node currentNode = currContainer.currentNode;
	    boolean[][] visitted = currContainer.visitted;
	    visitted[x][y] = true;
	    if (currentNode.isEndNode) {
		String currentWord = diction[currentNode.value];
		boolean realQu = true;
		if (currentWord.contains("Q")) {
		    for (int i = 0; i < currentWord.length(); i++) {
			if (currentWord.charAt(i) == 'Q') {
			    if (i + 1 == currentWord.length())
				realQu = false;
			    else {
				if (currentWord.charAt(i + 1) != 'U')
				    realQu = false;
			    }
			}
		    }
		}
		if (!foundWords.contains(currentWord) && currentWord.length() > 2 && realQu) {
		    foundWords.add(currentWord);
		    solution.push(currentWord);
		}
	    }

	    byte adjCharacter;// Is used up to 4 times
	    Stack<Container> adjacent = adjContainers[x][y].adj;
	    Iterator<Container> iterator = adjacent.iterator();
	    while(iterator.hasNext()){
		Container temp = iterator.next();
		if(visitted[temp.x][temp.y] ) continue;
		Node newNode = currentNode.children[temp.character];
		if(newNode != null){
		    temp.visitted = copyArray(visitted);
		    temp.currentNode = newNode;
		    nodes.push(temp);
		}
	    }
	    /*boolean adjLeftBorder = false, adjRightBorder = false, adjTopBorder = false, adjBottomBorder = false;
	    if (y == 0)
		adjLeftBorder = true;
	    if (x == 0)
		adjTopBorder = true;
	    if (x == board.rows() - 1)
		adjBottomBorder = true;
	    if (y == board.cols() - 1)
		adjRightBorder = true;
	    if (!adjLeftBorder && !visitted[x][y - 1]) {// Left side
		adjCharacter = getCount(board.getLetter(x, y - 1));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x, y-1, child, visitted));
		}
	    }
	    if (!adjRightBorder && !visitted[x][y + 1]) {// Right side
		adjCharacter = getCount(board.getLetter(x, y + 1));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x, y+1, child, visitted));
		}
	    }
	    if (!adjTopBorder && !visitted[x - 1][y]) {// Top side
		adjCharacter = getCount(board.getLetter(x - 1, y));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x-1, y, child, visitted));
		}
	    }
	    if (!adjBottomBorder && !visitted[x + 1][y]) {// Bottom side
		adjCharacter = getCount(board.getLetter(x + 1, y));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x+1, y, child, visitted));
		}
	    }
	    if (!adjTopBorder && !adjLeftBorder && !visitted[x - 1][y - 1]) {// Top left side
		adjCharacter = getCount(board.getLetter(x - 1, y - 1));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x-1, y-1, child, visitted));
		}
	    }
	    if (!adjTopBorder && !adjRightBorder && !visitted[x - 1][y + 1]) {// Top
									      // right
		adjCharacter = getCount(board.getLetter(x - 1, y + 1));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x-1, y+1, child, visitted));
		}
	    }
	    if (!adjBottomBorder && !adjLeftBorder && !visitted[x + 1][y - 1]) {// Bottom
										// left
		adjCharacter = getCount(board.getLetter(x + 1, y - 1));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x+1, y-1, child, visitted));
		}
	    }
	    if (!adjBottomBorder && !adjRightBorder && !visitted[x + 1][y + 1]) {// Bottom
										 // right
		adjCharacter = getCount(board.getLetter(x + 1, y + 1));
		Node child = currentNode.children[adjCharacter];
		if(child!=null){
		    nodes.push(new Container(x+1, y+1, child, visitted));
		}
	    }*/
	}
    }

    private boolean[][] copyArray(boolean[][] tmp) {// Makes a deep copy
	if(tmp == null) return null;
	boolean[][] copy = new boolean[tmp.length][tmp[0].length];
	for (int i = 0; i < copy.length; i++) {
	    for (int j = 0; j < copy[i].length; j++) {
		copy[i][j] = tmp[i][j];
	    }
	}
	return copy;
    }

    public static void main(String[] args) {
	BoggleSolver solver;
	/*
	 * String[] testDictionary = new String[18]; testDictionary[0] =
	 * "Hello"; testDictionary[1] = "World"; testDictionary[2] = "this";
	 * testDictionary[3] = "is"; testDictionary[4] = "my"; testDictionary[5]
	 * = "first"; testDictionary[6] = "dictionary"; testDictionary[7] =
	 * "Please"; testDictionary[8] = "be"; testDictionary[9] = "kind";
	 * testDictionary[10] = "AZE"; testDictionary[11] = "MAMA";
	 * testDictionary[12] = "HENCE"; testDictionary[13] = "MAN";
	 * testDictionary[14] = "Fist"; testDictionary[15] = "Queue";
	 * testDictionary[16] = "Ranqu"; testDictionary[17] = "Eque";
	 * 
	 * 
	 * for(int i = 0; i < testDictionary.length; i++){ testDictionary[i] =
	 * testDictionary[i].toUpperCase(); } solver = new
	 * BoggleSolver(testDictionary);
	 * StdOut.println(solver.scoreOf("HELLO"));
	 * 
	 * char[][] chars = new char[2][2]; chars[0][0] = 'R'; chars[0][1] =
	 * 'A'; chars[1][0] = 'N'; chars[1][1] = 'Q'; BoggleBoard board = new
	 * BoggleBoard(chars); StdOut.println(board.toString());
	 * solver.getAllValidWords(board); for(String solution:
	 * solver.getAllValidWords(board)){ StdOut.println("Solution: " +
	 * solution); }
	 */
	Stack<String> tmp = new Stack<>();
	In in = new In("C:\\Java\\Programme\\PrincetonCourse\\testing\\boggle\\dictionary-algs4.txt");
	String str = null;
	while (!in.isEmpty()) {
	    str = in.readString();
	    tmp.push(str);
	}
	String[] array = new String[tmp.size()];
	for (int i = 0; i < array.length; i++) {
	    array[i] = tmp.pop();
	}
	solver = new BoggleSolver(array);
	BoggleBoard board = new BoggleBoard("C:\\Java\\Programme\\PrincetonCourse\\testing\\boggle\\board4x4.txt");
	StdOut.println(board.toString());
	Iterable<String> iterable = solver.getAllValidWords(board);
	for (String temp : iterable) {
	    StdOut.println(temp);
	}

    }
}
