import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Container<Item> first;
    private Container<Item> last;
    private int count;
    
    
    private class Container<U>{

	private Container<U> next;
	private Container<U> before;
	private Item item;
	
	public Item getItem() {
	    return item;
	}
	
	public void setItem(Item item) {
	    this.item = item;
	}
	
	public Container<U> getNext() {
	    return next;
	}
	
	public void setNext(Container<U> next) {
	    this.next = next;
	}

	public Container<U> getBefore() {
	    return before;
	}

	public void setBefore(Container<U> before) {
	    this.before = before;
	}
    }
    
    private class DequeIterator implements Iterator<Item>{
	Container<Item> curr =  first;
	int i = 0;

	@Override
	public boolean hasNext() {
	    return i<size();
	}

	@Override
	public Item next() {
	    if(!hasNext()) throw new NoSuchElementException();
	    Item item = curr.item;
	    curr = curr.getNext();
	    i++;
	    return item;
	}
	
    } 

    
    public int size(){
	return count;
    }
    
    public boolean isEmpty(){
	return count==0;
    }
    
    private void test(Object obj){
	if(obj==null) throw new NullPointerException();
    }
    
    public void addFirst(Item item){
	test(item);
	if(first ==null){
	    first = new Container<Item>();
	    first.setItem(item);
	    last = first;
	}else{
	    Container<Item> oldFirst = first;
	    first = new Container<Item>();
	    first.setItem(item);
	    first.setNext(oldFirst);
	    oldFirst.setBefore(first);
	}
	count++;
    }
    
    public void addLast(Item item){
	test(item);
	Container<Item> newContainer = new Container<Item>();
	newContainer.setItem(item);
	Container<Item> oldLast = last;
	if(count>=1){
	    last.setNext(newContainer);
	    last = newContainer;
	    last.setBefore(oldLast);
	}else{
	    addFirst(item);
	    return;
	}
	count++;
    }
    
    public Item removeFirst(){
	if(count == 0) throw new NoSuchElementException();
	else if(count==1){
	    count--;
	    Item item = first.getItem();
	    first = null;
	    last = null;
	    return item;

	}else{
	    count--;
	    Item i = first.getItem();
	    first = first.getNext();
	    first.setBefore(null);
	    return i;
	}

    }
    
    public Item removeLast(){
	if(count == 0) throw new NoSuchElementException();
	
	if (count==1) {
	    return removeFirst();
	}
	Item i = last.getItem();
	last = last.getBefore();
	last.setNext(null);
	count--;
	return i;
    }

    
    public Iterator<Item> iterator(){
	return new DequeIterator();
    }
    
    @SuppressWarnings("boxing")
    public static void main(String[] args){
	Deque<Integer> deque = new Deque<>();
	deque.addLast(1);
	StdOut.println(deque.size());
	Iterator iterator = deque.iterator();
	StdOut.println(iterator.next());
	
    }

}
