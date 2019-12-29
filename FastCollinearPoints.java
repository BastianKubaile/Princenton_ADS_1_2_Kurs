import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
    private final Point[] points;
    private List<Double>[] usedSlopes;  
    private final List<LineSegment> segments = new ArrayList<LineSegment>();
    private final LineSegment[] segmentsArray;
    
    private class SlopeWithPoint{
	double slope;
	int point;
    }
    
    public FastCollinearPoints(Point[] points){
	if(points ==null) throw new IllegalArgumentException();
	this.testPoints(points);
	Arrays.sort(points, (p1, p2) ->  p1.compareTo(p2));
	this.points = points;
	usedSlopes = new ArrayList[points.length];
	for(int i = 0;i<usedSlopes.length;i++) usedSlopes[i] = new ArrayList<Double>();
	
	for(int i =0;i<points.length;i++){
	    SlopeWithPoint[] slopes = new SlopeWithPoint[points.length-(i+1)];
	    for (int j = i+1, k = 0; k< slopes.length; j++,k++) {
		slopes[k] = new SlopeWithPoint();
		slopes[k].point = j;
		slopes[k].slope = 0;
	    }
	    for(int j = 0,k=i+1; j<slopes.length;j++,k++){
		slopes[j].slope = points[i].slopeTo(points[k]);
	    }
	    Arrays.sort(slopes,(SlopeWithPoint s1,SlopeWithPoint  s2) -> {
		double temp = s1.slope-s2.slope;
		if(temp<-0.00001) return -1;
		if(temp>0.0001) return +1;
		return 0;
	    });
	    addLineSegments(slopes, i);
	}
	
	segmentsArray = new LineSegment[segments.size()];
	for(int i =0;i<segments.size();i++ ){
	    segmentsArray[i] = segments.get(i);
	}
    }
    
    private void addLineSegments(SlopeWithPoint[] slopes, int strtPoint){
	OUTER_LOOP: for(int i = 0;i<slopes.length-2;i++){
	    if(areEqual(slopes[i].slope, slopes[i+2].slope)){
		for(double d : usedSlopes[slopes[i].point]){
		    if(areEqual(d, slopes[i].slope)){
			continue OUTER_LOOP;
		    }
		}
		i +=2;
		int endPosition = i;
		int temp = i-2;
		while((endPosition+1<slopes.length)&&(areEqual(slopes[endPosition+1].slope, slopes[temp].slope))){
		    endPosition++;
		    i++;
		}
		
		for(int j = temp;j<=endPosition;j++){
		    usedSlopes[slopes[j].point].add(new Double(slopes[temp].slope));
		    if(slopes[temp].slope==Double.NEGATIVE_INFINITY){
			usedSlopes[slopes[j].point].add(new Double(Double.POSITIVE_INFINITY));
		    } 
		    if(slopes[temp].slope==Double.POSITIVE_INFINITY){
			usedSlopes[slopes[j].point].add(new Double(Double.NEGATIVE_INFINITY));
		    }
		}
		addLineSegments(temp, endPosition,slopes, strtPoint);
	    }
	}
    }
    
    private boolean areEqual(double d1, double d2){
	if(!isInfinity(d1)&&!isInfinity(d2)){
	    return (d1-d2<0.00001&&d1-d2>-0.00001);
	}
	if(isInfinity(d1)&&isInfinity(d2)) return true;
	return false;
	
    }
    
    private void testPoints(Point[] points){
	for(int i = 1;i<points.length;i++){
	    for(int j = i-1 ;j>=0;j--){
		if(points[i]==null||points[j]==null) throw new IllegalArgumentException(); 
		if(points[i].compareTo(points[j])==0) throw new IllegalArgumentException(); 
	    }
	}
    }
    
    private boolean isInfinity(double toCheckValue){
	return toCheckValue==Double.POSITIVE_INFINITY||toCheckValue==Double.NEGATIVE_INFINITY;
    }

    private void addLineSegments(int start, int end, SlopeWithPoint[] p, int originPoint){
	Point[] temp = new Point[end-start+1];
	temp[0] = points[originPoint];
	for (int i = 1; i < temp.length; i++) {
	    temp[i] = points[p[i+start].point];
	}
	Arrays.sort(temp,(p1,p2)->p1.compareTo(p2));
	segments.add(new LineSegment(temp[0], temp[temp.length-1]));
    }
    
    
    public int numberOfSegments(){
	return segments.size();
    }
    
    public LineSegment[] segments(){
	return segmentsArray;
    }
    
    public static void main(String[] args){
	Point[] arg = new Point[4];
	arg[0] = new Point(2, 2);
	arg[1] = new Point(3, 2);
	arg[2] = new Point(4, 2);
	arg[3] = new Point(5, 2);
	/*arg[4] = new Point(1, 4);
	arg[5] = new Point(3, 2);
	arg[6] = new Point(4, 1);
	arg[7] = new Point(5, 6); */
	FastCollinearPoints solver = new FastCollinearPoints(arg);
	StdOut.println(solver.numberOfSegments());
    }

}
