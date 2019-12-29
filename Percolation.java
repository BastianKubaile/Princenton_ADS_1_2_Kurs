import java.io.BufferedReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] gridValues;
    private int numberOfOpenSites;
    private WeightedQuickUnionUFShort uf;
    private WeightedQuickUnionUFShort ufForFull;
    private int n;
    
    public Percolation(int n) { 
	if (n <= 0) { 
	    throw new IllegalArgumentException();
	}
	this.n = n;
	gridValues = new boolean[n][n];
	uf = new WeightedQuickUnionUFShort(n * n + 2);
	ufForFull = new WeightedQuickUnionUFShort(n*n+1);
	/* The last index is the virtual end node.
	 * The index before the last node is the starting node.
	 */
    }
    
    private void testValues(int x, int y) { 
	if ((x >= gridValues.length || y >= gridValues.length || 
	   (x < 0 || y < 0))) { 
	    throw new IndexOutOfBoundsException();
	}
    }
    
    private int changeValue(int x) { 
	return x - 1;
    }
    
    public void open(int row, int col) { 
	int x = this.changeValue(row);
	int y = this.changeValue(col);
	this.testValues(x, y);
	if (!gridValues[x][y]) {
	    gridValues[x][y] = true;
	    numberOfOpenSites++;
	}
	this.updateUF(x, y);
    }
    
    private boolean returnValue(int row, int col) { 
	int x = this.changeValue(row);
	int y = this.changeValue(col);
	this.testValues(x, y);
	return gridValues[x][y];
    }
    
    public boolean isOpen(int row, int col) { 
	return this.returnValue(row, col);
    }
    
    public boolean isFull(int row, int col) { 
	int x = this.changeValue(row);
	int y = this.changeValue(col);
	this.testValues(x, y);
	return ufForFull.connected((short)(x*n+y), (short)(n*n));
    }
    
    public int numberOfOpenSites() { 
	return numberOfOpenSites;
    }
    
    private void updateUF(int x, int y) { 
	// First above
	if (x - 1 == -1) { // When there is no layer above-> connects with virtual starting node
	    uf.union((short)(x *n + y),(short)(n * n));
	    ufForFull.union((short)(x *n + y),(short)(n * n));
	}else { 
	    if(this.returnValue( x + 1 - 1, y + 1)) { // connects node with the node above if it is open
		uf.union((short)(x * n + y), (short)((x - 1)* n + y));
		ufForFull.union((short)(x * n + y),(short) ((x - 1)* n + y));
	    }
	}
	
	// Second below
	if (x+1==gridValues.length) { // When there is no layer below
	    uf.union((short)(x * n + y),(short)( n * n + 1));// Connects the node to the virtual end node
	}else { 
	    if(this.returnValue(x + 1 +1, y + 1)) { // Connects the node with the node below
		uf.union((short)(x * n + y), (short)((1 + x) * n + y));
		ufForFull.union((short)(x * n + y),(short) ((1 + x) * n + y));
	    }
	}
	
	// Third to the left
	if ((y - 1 != -1) && this.isOpen(x + 1, y + 1 - 1)) { 
	    uf.union((short)(x * n + y), (short)(x * n + y - 1));
	    ufForFull.union((short)(x * n + y),(short) (x * n + y - 1));
	}
	
	// At last to the right
	if ((!(y + 1 == gridValues.length)) && this.isOpen(x + 1, y + 1 + 1)) { 
	    uf.union((short)(x * n + y), (short)(x * n + y + 1));
	    ufForFull.union((short)(x * n + y), (short)(x * n + y + 1));
	}
    }
    
    public boolean percolates() {
	/*boolean percolates = false;
	OUTER_LOOP: for(int i = 0; i<gridValues.length;i++){
	    if (gridValues[n-1][i]==true) {
		if(uf.connected(n*n,(n-1)*n+i)){
		    percolates = true;
		    break OUTER_LOOP;
		}
	    }
	}
	return percolates;*/
	return uf.connected((short)(n * n),(short)( n * n + 1));// Checks whether the virtual nodes are connected
    }
    
    public static void main(String[] args){
	/*Percolation percolation  =  new Percolation(3);
	StdOut.println(percolation.isFull(1, 1));
	percolation.open(1, 1);
	StdOut.println(percolation.isFull(1, 1));
	
	// Zur Verwendung von Datein-> der Pfad von p muss angepasst werden
	Path p = FileSystems.getDefault().getPath("C:\\Java\\Programme\\PrincetonCourse\\inputText.txt");
	try(BufferedReader reader = Files.newBufferedReader(p)){
	    String string = "";
	    while(reader.ready()){
		string += reader.readLine();
	    }
	    StringTokenizer tokenizer = new StringTokenizer(string);
	    int[] values = new int[tokenizer.countTokens()]; 
	    for (int i = 0; i < values.length; i++) {
		values[i] = Integer.parseInt(tokenizer.nextToken()); 
	    }
	    
	    Percolation percolation = new Percolation(20);
	    int i = 0;
	    System.out.println(values.length);
	    for(int openSites = 0;i+1<values.length&&openSites<=231;openSites++){
		percolation.open(values[i], values[i++]);
		i++;
	    }
	    System.out.println(percolation.isFull(18, 1));
	    System.out.print(i);
	}catch (Exception e) {
	    e.printStackTrace();
	}
	*/
	
	Percolation percolation = new Percolation(3);
	percolation.open(1, 3);
	percolation.open(2, 3);
	percolation.open(3, 3);
	percolation.open(3, 1);
	System.out.println(percolation.isFull(3, 1));
    }

}

class WeightedQuickUnionUFShort {
    private short[] parent;   // parent[i] = parent of i
    private short[] size;     // size[i] = number of sites in subtree rooted at i
    private short count;      // number of components

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own 
     * component.
     *
     * @param  n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WeightedQuickUnionUFShort(int n) {
        count = (short)n;
        parent = new short[n];
        size = new short[n];
        for (short i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public short count() {
        return count;
    }
  
    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param  p the integer representing one object
     * @return the component identifier for the component containing site {@code p}
     * @throws IndexOutOfBoundsException unless {@code 0 <= p < n}
     */
    public short find(short p) {
        validate(p);
        while (p != parent[p])
            p = parent[p];
        return p;
    }

    // validate that p is a valid index
    private void validate(short p) {
	short n = (short) parent.length;
        if (p < 0 || p >= n) {
            throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n-1));  
        }
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     *         {@code false} otherwise
     * @throws IndexOutOfBoundsException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(short p, short q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the 
     * the component containing site {@code q}.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IndexOutOfBoundsException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(short p, short q) {
	short rootP = find(p);
	short rootQ = find(q);
        if (rootP == rootQ) return;

        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }
}
