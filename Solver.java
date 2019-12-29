import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Board currBoard;
    private Board switchedBoard;
    private MinPQ<SearchNode> currPQ;
    private MinPQ<SearchNode> switchedPQ;
    private boolean solvable = true;
    private int count = 0;
    private SearchNode lastSearchNode;
    
    private class SolutionIterator implements Iterator<Board>{
	Stack<Board> solution = new Stack<Board>();
	
	public SolutionIterator(){
	    SearchNode copySearchNode = lastSearchNode;
	    while(copySearchNode!=null){
		solution.push(copySearchNode.board);
		copySearchNode = copySearchNode.before;
	    }
	}

	@Override
	public boolean hasNext() {
	    return !solution.isEmpty();
	}

	@Override
	public Board next() {
	    if(!hasNext()) throw new NoSuchElementException();
	    return solution.pop();
	}
	
    }
    
    private static class SearchNode{
	Board board;
	int numberOfMoves;
	SearchNode before;
	int manhattanCount;
	
	
    }
    
    public Solver(Board intial){
	if(intial== null) throw new IllegalArgumentException();
	currBoard = intial;
	switchedBoard = currBoard.twin();
	currPQ = new MinPQ<>(1,(SearchNode s1, SearchNode s2)->{
	    return (s1.manhattanCount+s1.numberOfMoves)-(s2.manhattanCount+s2.numberOfMoves);
	});	
	switchedPQ = new MinPQ<>(1,(SearchNode s1, SearchNode s2)->{
	    return (s1.manhattanCount+s1.numberOfMoves)-(s2.manhattanCount+s2.numberOfMoves);
	});
	SearchNode currSearchNode = new SearchNode();
	currSearchNode.board = currBoard;
	currSearchNode.numberOfMoves = 0;
	currSearchNode.before = null;
	currPQ.insert(currSearchNode);
	SearchNode switchedSearchNode = new SearchNode();
	switchedSearchNode.board = switchedBoard;
	switchedSearchNode.numberOfMoves = 0;
	switchedSearchNode.before = null;
	switchedPQ.insert(switchedSearchNode);
	
	boolean curr = true;
	LOOP: while(true){
	    if(curr==true){
		curr = false;
		currSearchNode = currPQ.delMin();
		if(currSearchNode.board.isGoal()){
		    lastSearchNode = currSearchNode;
		    break LOOP;
		}
		List<SearchNode> temp = new ArrayList<>();
		Iterator<Board> iterator = currSearchNode.board.neighbors().iterator();
		while(iterator.hasNext()){
		    Board board = iterator.next();
		    if(currSearchNode.before==null||!currSearchNode.before.board.equals(board)){
			SearchNode addNode = new SearchNode();
			addNode.before = currSearchNode;
			addNode.board = board;
			addNode.numberOfMoves = currSearchNode.numberOfMoves+1;
			addNode.manhattanCount = addNode.board.manhattan();
			temp.add(addNode);
		    }
		}
		for(SearchNode sNode: temp){
			currPQ.insert(sNode);
		    }
	    }else{
		curr = true;
		switchedSearchNode = switchedPQ.delMin();
		if(switchedSearchNode.board.isGoal()){
		    break LOOP;
		}
		List<SearchNode> temp = new ArrayList<>();
		Iterator<Board> iterator = switchedSearchNode.board.neighbors().iterator();
		while(iterator.hasNext()){
		    Board board = iterator.next();
		    if(switchedSearchNode.before==null||!switchedSearchNode.before.board.equals(board)){
			SearchNode addNode = new SearchNode();
			addNode.before = switchedSearchNode;
			addNode.board = board;
			addNode.numberOfMoves = switchedSearchNode.numberOfMoves+1;
			addNode.manhattanCount = addNode.board.manhattan();
			temp.add(addNode);
		    }

		}
		for(SearchNode sNode: temp){
		    switchedPQ.insert(sNode);
		}
	    }
	}
	currPQ = null;
	switchedPQ = null;
	curr = !curr;
	if(curr == false) solvable = false;
	
	if(solution()!=null){
	    Iterator<Board> iterator = solution().iterator();
	    while(iterator!=null&&iterator.hasNext()){
		count++;
		iterator.next();
	    }
	    count--;
	}
	
    }
    
    public int moves(){
	if(!isSolvable()){
	    return -1;
	}
	return count;
    }
    
    public boolean isSolvable(){
	return solvable;
    }
    
    public Iterable<Board> solution(){
	if(!isSolvable()) return null;
	return new Iterable<Board>() {
	    
	    @Override
	    public Iterator<Board> iterator() {	
		return new SolutionIterator();
	    }
	};
    }
    
    private static SearchNode test(){
	return new SearchNode();
    }

    public static void main(String[] args) {
	/*int[][] b1 = new int[3][3];
	b1[0][0] = 1;
	b1[0][1] = 2;
	b1[0][2] = 3;
	b1[1][0] = 4;
	b1[1][1] = 6;
	b1[1][2] = 5;
	b1[2][0] = 7;
	b1[2][1] = 8;
	b1[2][2] = 0;
	Board board = new Board(b1);
	Solver solver = new Solver(board);
	StdOut.println(solver.isSolvable() + " false");
	StdOut.println(solver.moves() + " -1");
	Iterator<Board> iterator = solver.solution().iterator();
	while(iterator.hasNext()){
	    System.out.println(iterator.next());
	}
	
	MinPQ<SearchNode> testPQ = new MinPQ<>(2,(SearchNode s1, SearchNode s2)->{
	    return (s1.board.hamming()+s1.numberOfMoves)-(s2.board.hamming()+s2.numberOfMoves);
	});
	
	int[][] b2 = new int[2][2];
	b2[0][0] = 1;
	b2[0][1] = 2;
	b2[1][0] = 3;
	b2[1][1] = 0;
	SearchNode s1 = test();
	s1.numberOfMoves =0;
	s1.before = null;
	s1.board = new Board(b2);
	
	int[][] b3 = new int[2][2];
	b3[0][0] = 2;
	b3[0][1] = 1;
	b3[1][0] = 3;
	b3[1][1] = 0;
	SearchNode s2 = test();
	s2.numberOfMoves = 0;
	s2.before = null;
	s2.board = new Board(b3);
	
	testPQ.insert(s1);
	testPQ.insert(s2);
	StdOut.println(testPQ.delMin().board.toString());
	StdOut.println(testPQ.delMin().board.toString());
	*/
	
	
	// create initial board from file
	In in = new In(args[0]);
	int n = in.readInt();
	int[][] blocks = new int[n][n];
	for (int i = 0; i < n; i++)
	    for (int j = 0; j < n; j++)
	        blocks[i][j] = in.readInt();
	Board initial = new Board(blocks);

	// solve the puzzle
	Solver solver = new Solver(initial);

	// print solution to standard output
	if (!solver.isSolvable())
	    StdOut.println("No solution possible");
	else {
	    StdOut.println("Minimum number of moves = " + solver.moves());
	    for (Board board : solver.solution())
	        StdOut.println(board);
	}
    }


}
