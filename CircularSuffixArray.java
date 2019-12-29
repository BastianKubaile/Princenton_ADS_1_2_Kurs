import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private StringWithOffset[] words;
    private int n;
    
    private class StringWithOffset{
	String s;
	int offset;
	
	public char getCharAt(int index){
	    return s.charAt((index+offset)% s.length());
	}
    }
    public CircularSuffixArray(String s){
	if(s == null) throw new IllegalArgumentException();
	n = s.length();
	words = new StringWithOffset[s.length()];
	for(int i = 0; i < s.length(); i++){
	    words[i] = new StringWithOffset();
	    words[i].s = s;
	    words[i].offset = i;
	}
	//StdOut.println(this.toString());
	this.sortOffset();
	//StdOut.println(this.toString());
    }
    
    
    /*@Override
    public String toString(){
	StringBuilder builder = new StringBuilder();
	for(int i = 0; i < words.length; i++){
	    for(int j = 0; j < words[i].s.length(); j++){
		builder.append(words[i].getCharAt(j));
	    }
	    builder.append("\n");
	}
	return builder.toString();
    }*/
    
    public int length(){
	return words[0].s.length();
    }
    
    private void sortOffset(){
	Comparator<StringWithOffset> comparator = new Comparator<StringWithOffset>() {

	    @Override
	    public int compare(StringWithOffset i1, StringWithOffset i2) {//indexes of string.offset
		for(int temp = 0; temp < i1.s.length(); temp++){
		    char char1, char2;
		    char1 = i1.getCharAt(temp);
		    char2 = i2.getCharAt(temp);
		    if(char1 == char2){
			continue;
		    }
		    if(char1 < char2){
			return -1;
		    }
		    if(char1 > char2){
			return +1;
		    }
		    
		}
		return 0;
	    }
	    
	};
	Arrays.sort(words, comparator);
	
    }
    
    public int index(int i ){
	if(i<0 || i >= n){
	    throw new IllegalArgumentException();
	}
	return words[i].offset;
    }
    
    public static void main(String[] args){
	CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
    }

}
