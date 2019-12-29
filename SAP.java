

import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    
    private class IdAndDistance{
	int id;
	int distance;
    }
    
    private Digraph graph;
    
    public SAP(Digraph digraph){
	if(digraph==null) throw new IllegalArgumentException();
	graph = new Digraph(digraph);
    }
    
    //Used to store data in an argument
    private class Container{
	int i = -1;
    }
    
    public int length(int v, int w){
	testArg(v);
	testArg(w);
	Container distance = new Container();
	bfs(new int[]{v}, new int[]{w}, distance);
	return distance.i;
    }
    
    public int ancestor(int v, int w){
	testArg(v);
	testArg(w);
	return bfs(new int[]{v}, new int[]{w}, new Container());
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w){
	if(v==null||w==null) throw new IllegalArgumentException();
	for(int i: v){
	    testArg(i);
	}
	for(int i: w){
	    testArg(i);
	}
	int[] arr1 = convertIterableToArray(v);
	int[] arr2 = convertIterableToArray(w);
	Container distance = new Container();
	bfs(arr1, arr2, distance);
	return distance.i;
    }
    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
	if(v==null||w==null) throw new IllegalArgumentException();
	for(int i: v){
	    testArg(i);
	}
	for(int i: w){
	    testArg(i);
	}
	int[] arr1 = convertIterableToArray(v);
	int[] arr2 = convertIterableToArray(w);
	return bfs(arr1, arr2, new Container());
	
    }
    
    private int[] convertIterableToArray(Iterable<Integer> v){
	Iterator<Integer> iterator = v.iterator();
	int size = 0;
	while(iterator.hasNext()){
	    iterator.next();
	    size++;
	}
	int[] arr = new int[size];
	iterator = v.iterator();
	for(int i = 0; i<arr.length;i++){
	    arr[i] = iterator.next();
	}
	return arr;

    }
    
    private void testArg(int v){
	if(v<0){
	    throw new IllegalArgumentException();
	}
	if(v>=graph.V()){
	    throw new IllegalArgumentException();
	}
    }

    
    private int bfs(int[] startPoints1, int[] startPoints2, Container distance){
	int currentDistance1 = 1, currentDistance2 = 1;
	int numVertexesWithCurrentDistance1 = startPoints1.length;
	int numVertexesWithCurrentDistance2 = startPoints2.length;
	Queue<Integer> currVertexes1 = new Queue<>();
	Queue<Integer> currVertexes2 = new Queue<>(); 
	byte[] visitedVertexes = new byte[graph.V()]; 
	int[] distances1 = new int[graph.V()], distances2 = new int[graph.V()];
	/* Basic idea goes like this. We want to BFS the vertexes specified in the start node.
	 * We put them in the stack and we store the current distance of the in the 
	 * currentDistance variable. We also store how of the currentDistance vertexes are there in
	 * the other variable. When we do BFS on one vertex, we decrease the number of vertexes.
	 * When it reaches 0, we increment currentDistance and the number of these vertices is equal
	 * to the size of the stack we store them in. Each vertex we visited is marked 
	 * in the visittedVertixes array. Since we need to find a match between the 2 arrays,
	 * the visittedVertix is a byte so we can store 3 states:
	 * 
	 * 0 = not visited
	 * 1 = visited by first starting Points
	 * 2 = visited by second starting Points
	 * 
	 * If any vertex from one stack leads to a vertex already visited by another stack,
	 * we have a match.
	 */
	
	for(int i = 0; i<startPoints1.length;i++){
	    for(int j = 0; j<startPoints2.length;j++){
		if(startPoints1[i]==startPoints2[j]){
		    distance.i = 0;
		    return startPoints1[i];
		}
	    }
	}
	
	initializeQueue(startPoints1, currVertexes1);
	initializeQueue(startPoints2, currVertexes2);
	for(int i = 0; i<distances1.length;i++){
	    distances1[i] = -1;
	    distances2[i] = -1;
	}
	for(int i = 0; i<startPoints1.length;i++){
	    visitedVertexes[startPoints1[i]] = 1;
	    distances1[startPoints1[i]] = 0;
	}
	for(int i = 0; i<startPoints2.length;i++){
	    visitedVertexes[startPoints2[i]] = 2;
	    distances2[startPoints2[i]] = 0;
	}
	
	
	Stack<IdAndDistance> matches = new Stack<>();
	int distanceFirstMatch =  Integer.MAX_VALUE/2-1;
	boolean matchExists = false;
	while(currentDistance1<distanceFirstMatch*2){
	    if(currVertexes1.size()==0&&currVertexes2.size()==0){
		break;
	    }
	    
	    while(numVertexesWithCurrentDistance1>0){
		numVertexesWithCurrentDistance1--;
		int currVertex = currVertexes1.dequeue();
		for(int adj: graph.adj(currVertex)){
		    if(visitedVertexes[adj]==2){
			distances1[adj] = currentDistance1;
			if(!matchExists){
			    distanceFirstMatch = distances2[adj] + distances1[adj];
			}
			IdAndDistance match = new IdAndDistance();
			match.id = adj;
			match.distance = distances2[adj] + distances1[adj];
			matches.push(match);
			matchExists = true;
			visitedVertexes[adj] = 3;
			currVertexes1.enqueue(adj);
		    }
		    if(visitedVertexes[adj]==0){
			distances1[adj] = currentDistance1;
			visitedVertexes[adj] = 1;
			currVertexes1.enqueue(adj);
		    }
		}
	    }
	    numVertexesWithCurrentDistance1 = currVertexes1.size();
	    if(numVertexesWithCurrentDistance1!=0) currentDistance1++;
	    
	    while(numVertexesWithCurrentDistance2>0){
		numVertexesWithCurrentDistance2--;
		int currVertex = currVertexes2.dequeue();
		for(int adj: graph.adj(currVertex)){
		    if(visitedVertexes[adj]==1){
			distances2[adj] = currentDistance2;
			if(!matchExists){
			    distanceFirstMatch = distances2[adj] + distances1[adj];
			}
			IdAndDistance match = new IdAndDistance();
			match.id = adj;
			match.distance = distances1[adj] + distances2[adj];
			matches.push(match);
			matchExists = true;
			visitedVertexes[adj] = 3;
			currVertexes2.enqueue(adj);
		    }
		    if(visitedVertexes[adj]==0){
			distances2[adj] = currentDistance2;
			visitedVertexes[adj] = 2;
			currVertexes2.enqueue(adj);
		    }
		}
	    }
	    numVertexesWithCurrentDistance2 = currVertexes2.size();
	    if(numVertexesWithCurrentDistance2!=0) currentDistance2++;
	}
	if(matches.size()==0){
	    return -1;
	}
	int minDistance = Integer.MAX_VALUE, id = -1;
	for(IdAndDistance current: matches){
	    if(current.distance<minDistance){
		minDistance = current.distance;
		id = current.id;
	    }
	}
	distance.i = minDistance;
	return id;
    }
    
    private static void initializeQueue(int[] array, Queue<Integer> stack){
	for(int i :array){
	    stack.enqueue(i);
	}
    }
    
    public static void main(String[] args){
	In in = new In("C:\\Java\\Programme\\PrincetonCourse\\testing\\digraph3.txt");
	Digraph digraph = new Digraph(in);
	SAP sap = new SAP(digraph);
	/*StdOut.println(sap.length(0, 3));
	StdOut.println(sap.length(1, 3));
	StdOut.println(sap.ancestor(1, 3));
	StdOut.println(sap.ancestor(0, 3));
	StdOut.println(sap.ancestor(10, 7));*/
	StdOut.println(sap.length(8, 13));
	StdOut.println(sap.length(13, 8));
    }
}
