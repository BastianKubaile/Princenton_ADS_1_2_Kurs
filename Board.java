import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] numbers;
    
    private class BoardIterator implements Iterator<Board>{
	private int index = 0; 
	private Board[] boards;
	
	private BoardIterator() {
	    int iPos = -1, jPos = -1;
	    OUTER_LOOP: for(int i = 0; i<numbers.length;i ++){
		for(int j = 0;j<numbers[i].length;j++){
		    if(numbers[i][j]==0) {
			iPos = i;
			jPos = j;
			break OUTER_LOOP;
		    }
		}
	    }
	    int count = 0;
	    Board above = null;
	    Board toTheLeft = null;
	    Board toTheRight = null;
	    Board below = null;
	    //First switch above
	    if(iPos>0){
		above = new Board(numbers);
		exch(above.numbers, iPos, jPos, iPos-1,jPos);
		count++;
	    }
	    
	    //Then below
	    if(iPos<numbers.length-1){
		below = new Board(numbers);
		exch(below.numbers, iPos, jPos, iPos+1,jPos);
		count++;
	    }
	    
	    //Then to the Left
	    if(jPos>0){
		toTheLeft = new Board(numbers);
		exch(toTheLeft.numbers, iPos, jPos, iPos,jPos-1);
		count++;
	    }
	    //Then to the Right
	    if(jPos<numbers.length-1){
		toTheRight = new Board(numbers);
		exch(toTheRight.numbers, iPos, jPos, iPos,jPos+1);
		count++;
	    }
	    
	    boards = new Board[count];
	    boolean done1 = false;
	    boolean done2 = false;
	    boolean done3 = false;
	    boolean done4 = false;
	    for(int i = 0;i<boards.length;i++){
		if(below!=null&&!done1){
		    boards[i] = below;
		    done1 = true;
		    continue;
		}
		if(above!=null&&!done2){
		    boards[i] = above;
		    done2 = true;
		    continue;
		}
		if(toTheLeft!=null&&!done3){
		    boards[i] = toTheLeft;
		    done3 = true;
		    continue;
		}
		if(toTheRight!=null&&!done4){
		    boards[i] = toTheRight;
		    done4 = true;
		    continue;
		}
	    }
	}

	@Override
	public boolean hasNext() {
	    return index<boards.length; 

	}

	@Override
	public Board next() {
	    if(!hasNext()) throw new NoSuchElementException();
	    return boards[index++];
	}
	
    }
    
    public Board(int[][] blocks){
	if(blocks==null) throw new IllegalArgumentException();
	numbers = new int[blocks.length][blocks[0].length];
	for(int i = 0;i<blocks.length;i++){
	    for (int j = 0; j < blocks[i].length; j++) {
		numbers[i][j] = blocks[i][j];
	    }
	}
    }
    
    
    public int dimension(){
	return numbers.length;
    }
    
    public int hamming(){
	int falseBlocks = 0;
	for(int i = 0;i<numbers.length;i++){
	    for (int j = 0; j < numbers[i].length; j++) {
		if(numbers[i][j]!=0&&numbers[i][j]!=i*dimension()+j+1) falseBlocks++;
	    }
	}
	return falseBlocks;
    }
    
    public int manhattan(){
	int count = 0;
	for(int i = 0;i<numbers.length;i++){
	    for (int j = 0; j < numbers[i].length; j++) {
		if(numbers[i][j]!=0&&numbers[i][j]!=i*dimension()+j+1) {
		    int currentValue = numbers[i][j];
		    int iDif, jDif;
		    int shouldI, shouldJ;
		    shouldJ = (currentValue%dimension())-1;
		    if(shouldJ == -1) shouldJ = dimension()-1;
		    shouldI = (currentValue-shouldJ)/dimension();
		    jDif = Math.abs(shouldJ-j);
		    iDif = Math.abs(shouldI-i);
		    count += iDif+jDif;
		}
	    }
	}
	return count;
	
    }
    
    public boolean isGoal(){
	return hamming() == 0;
    }
    
    public Board twin(){
	Board twin = new Board(numbers);
	if(twin.numbers[0][0]==0||twin.numbers[0][1]==0){
	    exch(twin.numbers, 1, 0, 1, 1);
	    return twin;
	}
	exch(twin.numbers, 0,0, 0, 1);
	return twin;
    }
    
    private void exch(int[][] arg, int i1, int j1, int i2, int j2){
	int temp = arg[i1][j1];
	arg[i1][j1] = arg[i2][j2];
	arg[i2][j2] = temp;
    }
    
    public boolean equals(Object obj){
	if(!(obj instanceof Board)) return false;
	Board arg = (Board) obj;
	if(arg.dimension()!=dimension()) return false;
	for (int i = 0; i < numbers.length; i++) {
	    for (int j = 0; j < numbers[i].length; j++) {
		if(numbers[i][j]!=arg.numbers[i][j]) return false;
	    }
	}
	return true;
    }
    
    public Iterable<Board> neighbors(){
	return new Iterable<Board>() {
	    @Override
	    public Iterator<Board> iterator() {
		return new BoardIterator();
	    }
	};
    }
    
    public String toString(){
	String temp = dimension() + "\n";
	for(int i = 0;i<numbers.length;i++){
	    for(int j = 0;j<numbers[i].length;j++){
		String numberString = "";
		if(numbers[i][j]<10){
		    numberString += " " + numbers[i][j];
		}else{
		    numberString += numbers[i][j];
		}
		temp +=  numberString + "  ";
	    }
	    temp += "\n";
	}
	return temp;
    }
    
    public static void main(String[] args){
	int[][] b1 = new int[2][2];
	b1[0][0] = 1;
	b1[0][1] = 2;
	b1[1][0] = 3;
	b1[1][1] = 0;
	StdOut.println(new Board(b1).hamming() + " 0");
	StdOut.println(new Board(b1).manhattan() + " 0");
	
	int[][] b2 = new int[3][3];
	b2[0][0] = 0;
	b2[0][1] = 1;
	b2[0][2] = 3;
	b2[1][0] = 4;
	b2[1][1] = 2;
	b2[1][2] = 5;
	b2[2][0] = 7;
	b2[2][1] = 8;
	b2[2][2] = 6;
	StdOut.println(new Board(b2).hamming() + " 4");
	StdOut.println(new Board(b2).manhattan() + " 4");
	
	int[][] b3 = new int[3][3];
	b3[0][0] = 2;
	b3[0][1] = 1;
	b3[0][2] = 3;
	b3[1][0] = 4;
	b3[1][1] = 0;
	b3[1][2] = 5;
	b3[2][0] = 7;
	b3[2][1] = 8;
	b3[2][2] = 6;
	Board board3 = new Board(b3);
	Iterator<Board> iterator3 = board3.neighbors().iterator();
	while(iterator3.hasNext()){
	    StdOut.println(iterator3.next());
	}
    }

}
