import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private boolean[] removed;
    private int count = 0;
    private int countRemoved = 0;
    private int currPosition = 0;
    
    private class RandomizedQueueIterator<U> implements Iterator<Item>{
	int current = 0;
	RandomizedQueue<U> rq = new RandomizedQueue();
	{
	    rq.count = count;
	    rq.countRemoved = countRemoved;
	    rq.currPosition = currPosition;
	    rq.items = (U[]) new Object[items.length];
	    rq.removed = new boolean[removed.length];
	    for(int i = 0;i<items.length;i++){
		rq.items[i] = (U)items[i];
	    }
	    for(int i = 0;i<removed.length;i++){
		rq.removed[i] = removed[i];
	    }
	    
	}

	@Override
	public boolean hasNext() {
	    return rq.size()>0;
	}

	@Override
	public Item next() {
	    return (Item)rq.dequeue();
	}
	
	
	
    }
    
    @SuppressWarnings("unchecked")
    public RandomizedQueue(){
	items = (Item[]) new Object[1];
	removed = new boolean[1];
    }
    
    public boolean isEmpty(){
	return size()==0;
    }
    
    public int size(){
	return count-countRemoved;
    }
    
    public void enqueue(Item item){
	if(item==null) throw new NullPointerException();
	if(count==items.length){
	    resize(items.length*2);
	}
	count++;
	items[currPosition++] = item;
    }
    
    private void resize(int length){
	Item[] newItems = (Item[]) new Object[length];
	for(int i = 0;i<items.length;i++){
	    newItems[i] = items[i];
	}
	items = newItems;
	
	boolean[] newValues = new boolean[length];
	for(int i = 0;i<removed.length;i++){
	    newValues[i] = removed[i];
	}
	removed = newValues;
    }
    
    public Item dequeue(){
	if(countRemoved>=count) throw new NoSuchElementException();
	int i = StdRandom.uniform(0, count);
	if(removed[i]==true) return dequeue();
	removed[i] = true;
	Item item = items[i];
	countRemoved++;
	if(((double)countRemoved)/count>0.75){
	    resize();
	}
	return item;
    }
    
    private void resize(){
	Item[] newItems = (Item[]) new Object[(items.length/2)+1];
	boolean[] newValues = new boolean[(items.length/2)+1];
	count = 0;
	for(int i = 0, j = 0; i<removed.length;i++){
	    if(removed[i]==false&&items[i]!=null){
		newItems[j] = items[i];
		j++;
		count++;
	    }
	}
	countRemoved = 0;
	currPosition = count;
	items = newItems;
	removed = newValues;
    }
    
    public Item sample(){
	if(countRemoved>=count) throw new NoSuchElementException();
	int i = StdRandom.uniform(0, count);
	if(removed[i]==true) return sample();
	return items[i];
    }
    
    @Override
    public Iterator<Item> iterator(){
	return new RandomizedQueueIterator<Item>();
    }
    
    public static void main(String[] args){
	RandomizedQueue<Integer> rQueue = new RandomizedQueue<>();
	rQueue.enqueue(109);
	rQueue.enqueue(419);
	Iterator<Integer> iterator1 = rQueue.iterator();
	Iterator<Integer> iterator2 = rQueue.iterator();
	StdOut.println(iterator1.next());
	StdOut.println(iterator1.next());
	StdOut.println(iterator2.next());
	StdOut.println(iterator2.next());
    }

}
