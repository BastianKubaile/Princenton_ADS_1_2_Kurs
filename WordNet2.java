

import java.util.Iterator;
import java.util.StringTokenizer;

import edu.princeton.cs.algs4.BTree;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class WordNet2 {
    private BTree<String, Integer> tree;//String is the synonym; Integer is the id for the synonym
    private BTree<String, Integer> treeSeperate = new BTree<>();//Stores only unique words
    private Queue<String> seperateWords = new Queue<>();
    private Queue<String> words; // only used to parse words to an array
    private String[] wordsWithIds;//Id is the index of the array
    private Digraph graph;
    private SAP sap;
    
    public WordNet2(String synsets, String hypernyms){
	if(synsets==null||hypernyms==null) throw new IllegalArgumentException();
	In in = new In(synsets);
	tree = new BTree<>();
	words = new Queue<>();
	int count = 0;
	while(in.hasNextLine()){
	    String temp = in.readLine();
	    //System.out.println(temp);
	    StringTokenizer tokenizer = new StringTokenizer(temp, ",");
	    String s1 = tokenizer.nextToken();// Id
	    String s2 = tokenizer.nextToken();// Synonyms
	    words.enqueue(s2);
	    int id = Integer.parseInt(s1);
	    Stack<String> synonyms = new Stack<>();
	    tokenizer = new StringTokenizer(s2, " ");
	    while(tokenizer.hasMoreTokens()){
		String current = tokenizer.nextToken();
		if(treeSeperate.get(current)==null){
		    count++;
		    treeSeperate.put(current, 1);//We don't care about the int
		    seperateWords.enqueue(current);
		}
		synonyms.push(current);
	    }
	    Iterator<String> iterator = synonyms.iterator();
	    while(iterator.hasNext()){
		tree.put(iterator.next(), id);//Each synonyms is put seperatly, so that if you find for a word you will find it
	    }
	}
	StdOut.println(count);
	wordsWithIds = new String[words.size()];
	Iterator<String> iterator = words.iterator();
	for(int i = 0; i<wordsWithIds.length; i++){
	    wordsWithIds[i] = iterator.next();
	}
	
	graph = new Digraph(wordsWithIds.length);
	in = new In(hypernyms);
	while(in.hasNextLine()){
	    String temp = in.readLine();
	    StringTokenizer tokenizer = new StringTokenizer(temp, ",");
	    int id = Integer.parseInt(tokenizer.nextToken());
	    while(tokenizer.hasMoreTokens()){
		graph.addEdge(id, Integer.parseInt(tokenizer.nextToken().replaceAll("\\s+","")));
	    }
	}
	
	DirectedCycle dCycle = new DirectedCycle(graph);
	if(dCycle.hasCycle()) throw new IllegalArgumentException();
	
	boolean hasRoot = false;
	for(int i = 0; i<graph.V();i++){
	    if(graph.outdegree(i)==0&&hasRoot==true){//When there are 2 roots
		throw new IllegalArgumentException();
	    }else if(graph.outdegree(i)==0){
		hasRoot = true;
	    }
	}
	sap = new SAP(graph);
	//StdOut.println("End of Constructor");
    }
    
    public boolean isNoun(String word){
	if(word==null) throw new IllegalArgumentException();
	return tree.get(word)!=null;
    }
    
    public Iterable<String> nouns(){
	return words;
    }
    
    public String sap(String nounA, String nounB){
	int id1 = tree.get(nounA), id2 = tree.get(nounB);
	int commonAncestor = sap.ancestor(id1, id2);
	return wordsWithIds[commonAncestor];
    }
    
    public int distance(String nounA, String nounB){
	int id1 = tree.get(nounA), id2 = tree.get(nounB);
	return sap.length(id1, id2);
	
    }
    
    
    
    /*public int distance(String nounA, String nounB){
	if(!(isNoun(nounA)&&isNoun(nounB))) throw new IllegalArgumentException();
	int id1= tree.get(nounA);
	int id2 = tree.get(nounB);
	Stack<IdAndDistance> path1 = trimPath(walkTree(id1, 0, new Stack<IdAndDistance>()));
	Stack<IdAndDistance> path2 = trimPath(walkTree(id2, 0, new Stack<IdAndDistance>()));
	Distance distance = new Distance(); //Store the distance to common ancestor
	analysePaths(path1, path2, distance);
	return distance.distance;
	
    }*/
    
    

    public static void main(String[] args) {
	WordNet2 testNet2 = new WordNet2("C:\\Java\\Programme\\PrincetonCourse\\testing\\synsets.txt", "C:\\Java\\Programme\\PrincetonCourse\\testing\\hypernyms.txt");
	int i = 0;
	for(String str:testNet2.nouns()) i++;
	StdOut.println(i);

    }

}
