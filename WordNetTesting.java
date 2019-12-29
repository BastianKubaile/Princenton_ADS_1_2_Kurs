

import static org.junit.Assert.*;

import org.junit.Test;

public class WordNetTesting {

    @Test
    public void test() {
	WordNet2 testNet = new WordNet2("C:\\Java\\Programme\\PrincetonCourse\\testing\\synsets11.txt", "C:\\Java\\Programme\\PrincetonCourse\\testing\\hypernyms11.txt");
	assertEquals(true, testNet.isNoun("a"));
	assertEquals(true, testNet.isNoun("k"));
	assertEquals(false, testNet.isNoun("l"));
	assertEquals(false, testNet.isNoun("y"));
	
	assertEquals("b", testNet.sap("e" , "j"));
	assertEquals("b", testNet.sap("e" , "h"));
	assertEquals("b", testNet.sap("e" , "f"));
	assertEquals("b", testNet.sap("e" , "b"));
	assertEquals("a", testNet.sap("e" , "a"));
	assertEquals("a", testNet.sap("e" , "k"));
	assertEquals("a", testNet.sap("k" , "e"));
	assertEquals("f", testNet.sap("h" , "i"));
	assertEquals("f", testNet.sap("h" , "j"));
	
	WordNet2 testNet2 = new WordNet2("C:\\Java\\Programme\\PrincetonCourse\\testing\\synsets11.txt", "C:\\Java\\Programme\\PrincetonCourse\\testing\\hypernyms11_second.txt");
	assertEquals(true, testNet2.isNoun("a"));
	assertEquals(true, testNet2.isNoun("k"));
	assertEquals(false, testNet2.isNoun("l"));
	assertEquals(false, testNet2.isNoun("y"));

	assertEquals("b", testNet2.sap("e" , "j"));
	assertEquals("b", testNet2.sap("e" , "h"));
	assertEquals("b", testNet2.sap("e" , "f"));
	assertEquals("b", testNet2.sap("e" , "b"));
	assertEquals("a", testNet2.sap("e" , "a"));
	assertEquals("a", testNet2.sap("e" , "k"));
	assertEquals("a", testNet2.sap("k" , "e"));
	assertEquals("f", testNet2.sap("h" , "i"));
	assertEquals("f", testNet2.sap("h" , "j"));
	assertEquals("c", testNet2.sap("f" , "g"));
	assertEquals("c", testNet2.sap("k" , "j"));
	
	assertEquals(2, testNet2.distance("h", "i"));
	assertEquals(3, testNet2.distance("h", "j"));
	assertEquals(3, testNet2.distance("h", "e"));
	assertEquals(2, testNet2.distance("f", "g"));
	assertEquals(3, testNet2.distance("j", "h"));
    }

}
