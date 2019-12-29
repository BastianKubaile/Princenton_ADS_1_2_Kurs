import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    private Tree internalTree = new Tree();
    
    private class Node{
	    private Point2D point;
	    private Node leftNode;
	    private Node rightNode;
	    private int size = 1;
	    private boolean isVertical;
	    
	    public Node(int size,Point2D point, boolean isVertical){
		this.size       = size;
		this.point      = point;
		this.isVertical = isVertical;
		leftNode        = null;
		rightNode       = null;
		
	    }
	}
    
    private class Tree{
	/*If two points are on one line(meaning either x1 and x2 or y1 and y2 are equal)
	 * the point will be put on the left node.
	 * To the left means below or equal, to the right above.
	 */
	Node root;
	

	
	private void insert(Point2D toInsert){
	    this.put(toInsert,root,true);
	}
	
	private double compare(Node node, Point2D p1){
	    double cmp;
	    if(node.isVertical==true){
		cmp = p1.x()-node.point.x();
	    }else{
		cmp = p1.y()-node.point.y();
	    }
	    return cmp;
	}
	
	private Node put(Point2D p,Node node, boolean vertical){
	    if(node==root&&root==null) root = new Node(1, p,true);
	    if(node==null) return node = new Node(1,p,vertical);
	    
	    double cmp = compare(node, p);
	    
	    if     (cmp<=0.0&& !node.point.equals(p))	node.leftNode  = put(p, node.leftNode, !node.isVertical);
	    else if(cmp>0.0)    			node.rightNode = put(p, node.rightNode, !node.isVertical);
	    else /*if(cmp==0)*/{
		node.point     = p;
	    }
	    int size = 1;
	    if(node.leftNode!=null){
		size += node.leftNode.size;
	    }
	    if(node.rightNode!=null){
		size += node.rightNode.size;
	    }
	    node.size = size;
	    return node;
	}
	
	private boolean contains(Point2D point2d){
	    return contains(point2d,root);
	}
	
	private boolean contains(Point2D point2d, Node node){
	    if(node.point.equals(point2d)) return true;
	    double cmp = compare(node, point2d);
	    if(cmp<=0.0){
		if(node.leftNode==null)  return false;
		                         return contains(point2d, node.leftNode);
	    }
		if(node.rightNode==null) return false;
		                         return contains(point2d, node.rightNode);
	    
	}
	
	private int size(){
	    if(root==null) return 0;
	    return root.size;
	}
	
	private void draw(Node node, Node superNode){
	    if(node==null) return;
	    setColor(node);
	    if(superNode==null){
		StdDraw.line(node.point.x() , 0, node.point.x(), 1);
	    }else{
		if(node.isVertical==true){
		    if(node.point.y()<superNode.point.y()){
			StdDraw.line(node.point.x(), superNode.point.y(), node.point.x(), 0);
		    }else{
			StdDraw.line(node.point.x(), superNode.point.y(), node.point.x(), 1);
		    }
		}else{
		    if(node.point.x()<superNode.point.x()){
			StdDraw.line(superNode.point.x(), node.point.y(), 0, node.point.y());
		    }else{
			StdDraw.line(superNode.point.x(), node.point.y(), 1, node.point.y());
		    }
		}
	    }
	    draw(node.leftNode , node);
	    draw(node.rightNode, node);
	}
	
	private void setColor(Node node){
	    if(node.isVertical){
		StdDraw.setPenColor(StdDraw.RED);
	    }else{
		StdDraw.setPenColor(StdDraw.BLUE);
	    }
	}
	
	private Node nearestToPoint(Node node, Point2D point){
	    if(node.isVertical==true){
		double cmp = point.x()-node.point.x();
		if(cmp<=0.0){
		    return node.leftNode;
		}
		return node.rightNode;
	    }
	    double cmp = point.y()-node.point.y();
	    if(cmp<=0.0){
		return node.leftNode;
	    }
	    return node.rightNode;

	}
	
	public Point2D nearestTo(Point2D point, Node node){
	    if(node.leftNode==null&&node.rightNode==null) return node.point;
	    
	    if(node.leftNode == null){
		Point2D otherPoint = nearestTo(point, node.rightNode);
		if(node.point.distanceSquaredTo(point)<=otherPoint.distanceSquaredTo(point)){
		    return node.point;
		}
		return otherPoint;
	    }
	    
	    if(node.rightNode == null){
		Point2D otherPoint = nearestTo(point, node.leftNode);
		if(node.point.distanceSquaredTo(point)<=otherPoint.distanceSquaredTo(point)){
		    return node.point;
		}
		return otherPoint;
	    }
	    
	    double optimalDistance;
	    if(node.isVertical==true){
		Point2D optimalPoint = new Point2D(node.point.x(), point.y());
		optimalDistance = optimalPoint.distanceSquaredTo(point);
	    }else{
		Point2D optimalPoint = new Point2D(point.x(), node.point.y());
		optimalDistance = optimalPoint.distanceSquaredTo(point);
		
	    }
	    
	    Point2D nearPoint = nearestTo(point, nearestToPoint(node, point));
	    if(nearPoint.distanceSquaredTo(point)<optimalDistance){
		return nearPoint;
	    }
	    
	    Node otherSide;
	    if(nearestToPoint(node, point)==node.leftNode){
		otherSide = node.rightNode;
	    }else{
		otherSide = node.leftNode;
	    }
	    
	    Point2D awayPoint = nearestTo(point, otherSide);
	    if(awayPoint.distanceSquaredTo(point)<nearPoint.distanceSquaredTo(point)&&
	       awayPoint.distanceSquaredTo(point)<node.point.distanceSquaredTo(point)){
		return awayPoint;
	    }
	    if (nearPoint.distanceSquaredTo(point)<awayPoint.distanceSquaredTo(point)&&
	        nearPoint.distanceSquaredTo(point)<node.point.distanceSquaredTo(point)) {
		return nearPoint;
	    }
	    return node.point;
	}
    }
    
    private class RectangleIterator implements Iterator<Point2D>{
	Stack<Point2D> points = new Stack<Point2D>();
	
	public RectangleIterator(RectHV rect, Node root) {
	    addPoints(rect, root);
	}
	
	private void addPoints(RectHV rect, Node node){
	    if(node==null) return;
	    if(rect.contains(node.point)){
		points.push(node.point);
		addPoints(rect, node.leftNode);
		addPoints(rect, node.rightNode);
	    }else{
		if(node.isVertical==true){
		    if(rect.xmin()<node.point.x()&&rect.xmax()<node.point.x()){
			addPoints(rect, node.leftNode);
		    }else if(rect.xmin()>node.point.x()&&rect.xmax()>node.point.x()){
			addPoints(rect, node.rightNode);
		    }else if(rect.xmin()<=node.point.x()&&rect.xmax()>=node.point.x()){
			addPoints(rect, node.leftNode);
			addPoints(rect, node.rightNode);
		    }
		}else{
		    if(rect.ymin()<node.point.y()&&rect.ymax()<node.point.y()){
			addPoints(rect, node.leftNode);
		    }else if(rect.ymin()>node.point.y()&&rect.ymax()>node.point.y()){
			addPoints(rect, node.rightNode);
		    }else if(rect.ymin()<=node.point.y()&&rect.ymax()>=node.point.y()){
			addPoints(rect, node.leftNode);
			addPoints(rect, node.rightNode);
		    }
		}
	    }
	}

	@Override
	public boolean hasNext() {
	    return !points.isEmpty();
	}

	@Override
	public Point2D next() {
	    if(!hasNext()) throw new NoSuchElementException();
	    return points.pop();
	}
	
    }
    
    public int size(){
	return internalTree.size();

    }
    
    public boolean isEmpty(){
	return size()==0;
    }
    
    public void insert(Point2D p){
	internalTree.insert(p);
    }
    
    public boolean contains(Point2D point2d){
	return internalTree.contains(point2d);
    }
    
    public void draw(){
	internalTree.draw(internalTree.root, null);
    }
    
    public Iterable<Point2D> range(RectHV rectHV){
	return new Iterable<Point2D>() {
	    
	    @Override
	    public Iterator<Point2D> iterator() {
		return new RectangleIterator(rectHV, internalTree.root);
	    }
	};
    }
    
    public Point2D nearest(Point2D that){
	return internalTree.nearestTo(that, internalTree.root);
    }
    
    public static void main(String[] args){
	/*
	KdTree solver = new KdTree();
	solver.insert(new Point2D(0.206107, 0.095492));
	solver.insert(new Point2D(0.975528, 0.654508));
	solver.insert(new Point2D(0.024472, 0.345492));
	solver.insert(new Point2D(0.793893, 0.095492));
	solver.insert(new Point2D(0.793893, 0.904508));
	solver.insert(new Point2D(0.975528, 0.345492));//
	solver.insert(new Point2D(0.206107, 0.904508));
	solver.insert(new Point2D(0.500000, 0.000000));
	solver.insert(new Point2D(0.024472, 0.654508));
	solver.insert(new Point2D(0.500000, 1.000000));
	Point2D point2d = new Point2D(0.601, 0.6);
	StdOut.println(point2d.distanceTo(new Point2D(0.206107, 0.095492)));//Solution 
	StdOut.println(point2d.distanceTo(new Point2D(0.975528, 0.654508)));
	StdOut.println(point2d.distanceTo(new Point2D(0.024472, 0.345492)));
	StdOut.println(point2d.distanceTo(new Point2D(0.793893, 0.095492)));
	StdOut.println(point2d.distanceTo(new Point2D(0.793893, 0.904508)));//other solution 
	StdOut.println(point2d.distanceTo(new Point2D(0.975528, 0.345492)));
	StdOut.println(point2d.distanceTo(new Point2D(0.206107, 0.904508)));//One solution
	StdOut.println(point2d.distanceTo(new Point2D(0.500000, 0.000000)));
	StdOut.println(point2d.distanceTo(new Point2D(0.024472, 0.654508)));
	StdOut.println(point2d.distanceTo(new Point2D(0.500000, 1.000000)));
	StdOut.println(solver.nearest(point2d));
	StdOut.println("Solved!");
	KdTree secondSolver = new KdTree();
	secondSolver.insert(new Point2D( 0.9,0.3));
	secondSolver.insert(new Point2D( 0.1,0.3));
	secondSolver.insert(new Point2D( 0.2,0.3));
	secondSolver.insert(new Point2D( 0.8,0.3));
	secondSolver.insert(new Point2D( 0.4,0.3));
	secondSolver.insert(new Point2D( 0.7,0.3));
	secondSolver.insert(new Point2D( 0.5,0.3));
	StdOut.println(secondSolver.nearest(new Point2D(0.0, 0.0)));
	*/
	
	StdOut.println(new Point2D(0.5, 0.5).equals(new Point2D(0.499999, 0.5)));
	KdTree grid = new KdTree();
	KdTree2 checker = new KdTree2();
	for(int i = 0; i<5000;i++){
	    Random random = new Random();
	    double xRand = StdRandom.uniform();
	    double yRand = StdRandom.uniform();
	    grid.insert(new Point2D(xRand, yRand));
	    checker.insert(new Point2D(xRand, yRand));
	}
	for(int i =0;i<50000;i++){
	    Random random = new Random();
	    double xRand = StdRandom.uniform();
	    double yRand = StdRandom.uniform();
	    Point2D gridPoint    = grid.nearest(new Point2D(xRand, yRand));
	    Point2D checkerPoint = grid.nearest(new Point2D(xRand, yRand));
	    if(!gridPoint.equals(checkerPoint)){
		System.out.println(xRand + "   " + yRand);
	    }
	}
	/*
	KdTree solver3 = new KdTree();
	solver3.insert(new Point2D(0.99, 0.0));
	solver3.insert(new Point2D(0.25, 0.25));
	solver3.insert(new Point2D(0.5, 0.5));
	solver3.insert(new Point2D(1.0, 0.0));	
	solver3.insert(new Point2D(0.6, 0.75));

	StdOut.println(solver3.nearest(new Point2D(0.4, 0.75)));*/
    }

}
