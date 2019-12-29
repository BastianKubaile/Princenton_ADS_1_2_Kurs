import java.awt.Color;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;


public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    
    public SeamCarver(Picture picture){
	if(picture==null) throw new IllegalArgumentException();
	this.picture = picture;
	energy = new double[this.picture.width()][this.picture.height()];
	initializeEnergy();
    }
    
    public Picture picture(){
	return this.picture;
    }
    
    public int width(){
	return this.picture.width();
    }
    
    public int height(){
	return this.picture.height();
    }
    
    public double energy(int x, int y){
	return energy[x][y];
    }
    
    public int[] findVerticalSeam(){
	double[][] newEnergy = new double[energy[0].length][energy.length];//New orientation of 2D Array
	for(int i = 0; i < energy.length; i++){
	    for(int j = 0; j < energy[i].length; j++){
		newEnergy[j][i] = energy[i][j];//Does this work?
	    }
	}
	return findSeam(newEnergy);
    }
    
    public int[] findHorizontalSeam(){
	return findSeam(energy);
    }
    
    public void removeVerticalSeam(int[] seam){
	if(seam == null) throw new IllegalArgumentException();
	Picture newPic = new Picture(picture.width()-1, picture.height());
	//StdOut.println("Width: " + newPic.width() + " Height: " + newPic.height());
	for(int i = 0, k = 0; i < picture.height()&& k < newPic.height(); i++, k++){
	    for(int j = 0, o = 0; j < picture.width() && o < newPic.width(); j++, o++){
		if(seam[i]!=j){
		    //StdOut.print("Old Column and Row: [" + i + "] [" + j + "] ||| ");
		    //StdOut.print("New Column and Row: [" + k + "] [" + o + "] ");
		    Color oldCol = picture.get(j, i);
		    newPic.set(o, k, oldCol);
		}else{
		    o--;
		}
	    }
	    //StdOut.println();
	}
	//SeamCarver seamCarver = new SeamCarver(newPic);
	this.picture = newPic;
    }
    
    public void removeHorizontalSeam(int[] seam){
	if(seam == null) throw new IllegalArgumentException();
	Picture newPic = new Picture(picture.width(), picture.height()-1);
	//StdOut.println("Width: " + newPic.width() + " Height: " + newPic.height());
	for(int i = 0, k = 0; i < picture.width() && k < newPic.width(); i++, k++){
	    for(int j = 0, o = 0; j < picture.height() && o < newPic.height();j++, o++){
		if(seam[i]!=j){
		    newPic.set(k, o, picture.get(i, j));
		}else{
		    o--;
		}
	    }
	}
	//SeamCarver seamCarver = new SeamCarver(newPic);
	this.picture = newPic;
    }
    private void initializeEnergy(){
	for(int i = 0; i<energy.length; i++ ){
	    for(int j = 0; j<energy[i].length; j++ ){
		if(!isBorder(i, j)){
		    int yDiff = diff(i-1, j, i+1, j);
		    int xDiff = diff(i, j-1, i, j+1);
		    energy[i][j] = Math.sqrt(xDiff+yDiff); 
		}
	    }
	}
    }
    
    //Returns whether pixel is border and initializes it if it is a border pixel
    private boolean isBorder(int x, int y){
	if(x == 0 || y == 0 || x == energy.length-1 || y == energy[x].length-1 ){
	    energy[x][y] = 1000;
	    return true;
	}
	return false;
    }
    
    private int diff(int x1, int y1, int x2, int y2){
	Color c1 = picture.get(x1, y1);
	Color c2 = picture.get(x2, y2);
	int r = c1.getRed() - c2.getRed(), b = c1.getBlue() - c2.getBlue(), g = c1.getGreen() - c2.getGreen();
	return r*r + b*b + g*g;
    }
    
    private int[] findSeam(double[][] energyArray){
	double[][] distTo = new double[energyArray.length+2][];
	distTo[0] = new double[1];
	distTo[0][0] = 0;
	distTo[distTo.length-1] = new double[1];
	for(int i = 1; i < distTo.length-1  ; i++){
	    distTo[i] = new double[energyArray[0].length];
	}
	for(int i = 1; i < distTo.length; i++){
	    for(int j = 0; j < distTo[i].length; j++){
		distTo[i][j] = Double.POSITIVE_INFINITY;
	    }
	}
	
	int[][] ancestor = new int[energyArray.length+2][];
	ancestor[0] = new int [1];
	ancestor[0][0] = 0;
	ancestor[ancestor.length-1] = new int [1];
	for(int i = 1; i < ancestor.length-1  ; i++){
	    ancestor[i] = new int[energyArray[0].length];
	}
	for(int i = 1; i < ancestor.length; i++){
	    for(int j = 0; j < ancestor[i].length; j++){
		ancestor[i][j] =  -1;
	    }
	}
	
	MinPQ<PointWithDistance> pq = new MinPQ<>();
	pq.insert(new PointWithDistance(0, 0, 0));
	while(!pq.isEmpty()){
	    PointWithDistance currPoint = pq.delMin();
	    
	    //if(currPoint.y == 3 && currPoint.x == 1)
		//StdOut.println("We are here");//Just for debugging
	    
	    if(currPoint.y == distTo.length-1) continue;
	    
	    if(currPoint.y == 0){//Top row
		for(int i = 0; i < distTo[1].length; i++){
		    relax(pq, energyArray, distTo, ancestor, 0, 0, i, 1);
		}
		continue;
	    }
	    
	    if(currPoint.y ==  distTo.length-2){//Last row
		relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, 0, distTo.length-1);
		continue;
	    }
	    
	    if(currPoint.x == 0){//Left border
		relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x, currPoint.y+1);
		relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x+1, currPoint.y+1);
		continue;
	    }
	    
	    if(currPoint.x == distTo[1].length-1){//Right border
		relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x-1, currPoint.y+1);
		relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x, currPoint.y+1);
		continue;
	    }
	    

	    relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x, currPoint.y+1);
	    relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x+1, currPoint.y+1);
	    relax(pq, energyArray, distTo, ancestor, currPoint.x, currPoint.y, currPoint.x-1, currPoint.y+1);
	}

	int[] result = new int[energyArray.length];
	result[result.length-1] = ancestor[ancestor.length-1][0];
	for(int i = result.length-2; i>=0; i--){
	    result[i] = ancestor[i+2][result[i+1]];
	}
	return result;
    }
    
    private class PointWithDistance implements Comparable<PointWithDistance>{
	int x;
	int y; 
	double distance;
	
	public PointWithDistance(int x, int y, double distance) {
	    this.x = x;
	    this.y = y;
	    this.distance = distance;
	}
	
	@Override
	public int compareTo(PointWithDistance o) {
	    double distance = o.distance - this.distance;
	    if(distance < 0) return +1;
	    return -1;
	}
	
	
    }
    
    //Modifies the input array, returns nothing
    private void relax(MinPQ<PointWithDistance> pq, double[][] energyArray, double[][] distTo, int[][] ancestor, int x1, int y1, int x2, int y2){
	/*Values are checked if they are right, if they aren't they are ignored*/
	if(x1 < 0 || x2 < 0) return;
	if(x1 >= distTo[y1].length || x2 >= distTo[y2].length) return;
	double energy = 0;
	if(y2 > 0 && y2-1 < energyArray.length) energy = energyArray[y2-1][x2];
	double newDistance = distTo[y1][x1] + energy;
	double oldDistance = distTo[y2][x2];
	if(newDistance < oldDistance){
	    distTo[y2][x2] = newDistance;
	    ancestor[y2][x2] = x1;//Doesn't work right
	    pq.insert(new PointWithDistance(x2, y2, newDistance));//Updates PQ
	}
    }

}
