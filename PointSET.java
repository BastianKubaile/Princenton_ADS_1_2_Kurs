import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private SET<Point2D> points = new SET<>();
    
    private class RectangleIterator implements Iterator<Point2D>{
	Stack<Point2D> temp = new Stack<Point2D>();
	
	public RectangleIterator(RectHV rectHV) {
	    for(Point2D point2d:points){
		if(rectHV.contains(point2d)){
		    temp.push(point2d);
		}
	    }
	}

	@Override
	public boolean hasNext() {
	    return !temp.isEmpty();
	}

	@Override
	public Point2D next() {
	    if(!hasNext()) throw new NoSuchElementException();
	    return temp.pop();
	}
	
    }
    
    public PointSET(){
	
    }
    
    private void test(Object obj){
	if(obj==null)throw new IllegalArgumentException();
    }
    
    public boolean isEmpty(){
	return points.isEmpty();
    }
    
    public int size(){
	return points.size();
    }
    
    public void insert(Point2D p){
	test(p);
	points.add(p);
    }
    
    public boolean contains(Point2D p){
	test(p);
	return points.contains(p);
    }
    
    public void draw(){
	for(Point2D point2d : points){
	    StdDraw.point(point2d.x(), point2d.y());
	}
    }
    
    public Iterable<Point2D> range(RectHV rect){
	test(rect);
	return new Iterable<Point2D>() {
	    
	    @Override
	    public Iterator<Point2D> iterator() {
		return new RectangleIterator(rect);
	    }
	};
    }
    
    public Point2D nearest(Point2D p){
	test(p);
	Point2D solution = points.min();
	for(Point2D temp: points){
	    if(p.distanceTo(temp)<p.distanceTo(solution)) solution = temp;
	}
	return solution;
    }
    
    public static void main(String[] args){
	PointSET solver = new PointSET();
	solver.insert(new Point2D(0.7, 0.2));
	solver.insert(new Point2D(0.5, 0.4));
	solver.insert(new Point2D(0.2, 0.3));
	solver.insert(new Point2D(0.4, 0.7));
	StdOut.println("Solved!");
	StdOut.println(solver.size() + "4");
	StdOut.println(solver.contains(new Point2D(0.7, 0.2)) + "true");
	StdOut.println(solver.nearest(new Point2D(0.6, 0.2)));
	Iterator<Point2D> iterator = solver.range(new RectHV(0.0, 0.0, 0.5, 0.5)).iterator();
	while(iterator.hasNext())
	    StdOut.println(iterator.next());
    }

}
