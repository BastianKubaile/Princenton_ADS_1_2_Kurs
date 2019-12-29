import java.util.ArrayList;
import java.util.List;
/*import java.io.BufferedReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.StringTokenizer;*/

public class BruteCollinearPoints {
    private final Point[] points;
    private int numberOfSegments = 0;
    private final List<LineSegment> segmentsList = new ArrayList<LineSegment>();
    
    public BruteCollinearPoints(Point[] points){
	if(points==null) throw new IllegalArgumentException();
	this.testPoints(points);
	
	this.points = points;
	for(int i = 0;i<points.length-3;i++){
	    for(int j = i+1;j<points.length-2;j++){
		for(int k = j+1;k<points.length-1;k++){
		    for(int l = k+1;l<points.length;l++){
			if(this.isLine(i, j, k, l)){
			    numberOfSegments++;
			    this.addLineSegment(i, j, k, l);
			    
			}
		    }
		}
	    }
	}
    }
    
    private boolean isLine(int i, int j, int k, int l){
	double slope1 = this.points[i].slopeTo(points[j]);
	double slope2 = this.points[i].slopeTo(points[k]);
	double slope3 = this.points[i].slopeTo(points[l]);
	if(compareDoubles(slope1, slope2)&&compareDoubles(slope1, slope3)) return true;
	return false;
    }
    
    private boolean compareDoubles(double d1, double d2){
	if(!isInfinity(d1)&&!isInfinity(d2)){
	    return (d1-d2<0.00001&&d1-d2>-0.00001);
	}
	if(isInfinity(d1)&&isInfinity(d2)) return true;
	return false;
	
    }
    
    private boolean isInfinity(double toCheckValue){
	return toCheckValue==Double.POSITIVE_INFINITY||toCheckValue==Double.NEGATIVE_INFINITY;
    }
    
    public int numberOfSegments(){
	return numberOfSegments;
    }
    
    public LineSegment[] segments(){
	LineSegment[] segments = new LineSegment[segmentsList.size()];
	for(int i = 0;i<segments.length;i++){
	    segments[i] = segmentsList.get(i);
	}
	return segments;
	
    }
    
    private void testPoints(Point[] points){
	if(points.length==1)if(points[0]==null) throw new IllegalArgumentException();
	for(int i = 1;i<points.length;i++){
	    for(int j = i-1 ;j>=0;j--){
		if(points[i]==null||points[j]==null) throw new IllegalArgumentException(); 
		if(points[i].compareTo(points[j])==0) throw new IllegalArgumentException(); 
	    }
	}
    }
    
    private void addLineSegment(int i1, int j1, int k1, int l1){
	Point lowestPoint = new Point(0, 0);
	Point highestPoint = new Point(0, 0);
	Point i = points[i1];
	Point j = points[j1];
	Point k = points[k1];
	Point l = points[l1];
	if(i.compareTo(j)<0&&i.compareTo(k)<0&&i.compareTo(l)<0) lowestPoint=i;
	if(j.compareTo(i)<0&&j.compareTo(k)<0&&j.compareTo(l)<0) lowestPoint=j;
	if(k.compareTo(j)<0&&k.compareTo(i)<0&&k.compareTo(l)<0) lowestPoint=k;
	if(l.compareTo(j)<0&&l.compareTo(k)<0&&l.compareTo(i)<0) lowestPoint=l;
	if(i.compareTo(j)>0&&i.compareTo(k)>0&&i.compareTo(l)>0) highestPoint=i;
	if(j.compareTo(i)>0&&j.compareTo(k)>0&&j.compareTo(l)>0) highestPoint=j;
	if(k.compareTo(j)>0&&k.compareTo(i)>0&&k.compareTo(l)>0) highestPoint=k;
	if(l.compareTo(j)>0&&l.compareTo(k)>0&&l.compareTo(i)>0) highestPoint=l;
	segmentsList.add(new LineSegment(highestPoint, lowestPoint));
	
    }
    
    /*public static void main(String[] args){
	/*Path p = FileSystems.getDefault().getPath("C:\\Java\\Programme\\PrincetonCourse\\inputText.txt");
	try(BufferedReader reader = Files.newBufferedReader(p)){
	    boolean firstRound = true;
	    int index = 0;
	    Point[] points = new Point[0];
	    while(reader.ready()){
		String temp = reader.readLine();
		StdOut.println(temp);
		StringTokenizer tokenizer = new StringTokenizer(temp);
		if(firstRound){
		    points = new Point[Integer.parseInt(tokenizer.nextToken())];
		}else{
		    points[index ] = new Point(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken()));
		    index++;
		}
		firstRound = false;
		
	    }
	    BruteCollinearPoints solver = new BruteCollinearPoints(points);
	    StdOut.println("Solved!");
	    StdOut.println(solver.numberOfSegments());
	}catch (Exception e) {
	    e.printStackTrace();
	}
	Point[] arg = new Point[4];
	arg[0] = new Point(1, 1);
	arg[1] = new Point(1, 2);
	arg[2] = new Point(1, 3);
	arg[3] = new Point(1, 4);
	BruteCollinearPoints solver2 = new BruteCollinearPoints(arg);
	StdOut.println(solver2.numberOfSegments);
	
    }*/
}
