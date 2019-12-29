import static org.junit.Assert.*;

import org.junit.Test;

public class BoggleSolverTester {

    @Test
    public void test() {
	String[] testDictionary = new String[16];
	testDictionary[0] = "Hello";
	testDictionary[1] = "World";
	testDictionary[2] = "this";
	testDictionary[3] = "is";
	testDictionary[4] = "my";
	testDictionary[5] = "first";
	testDictionary[6] = "dictionary";
	testDictionary[7] = "Please";
	testDictionary[8] = "be";
	testDictionary[9] = "kind";
	testDictionary[10] = "AZE";
	testDictionary[11] = "MAMA";
	testDictionary[12] = "HENCE";
	testDictionary[13] = "MAN";
	testDictionary[14] = "Fist";
	testDictionary[15] = "Queue";
	for(int i = 0; i < testDictionary.length; i++){
	    testDictionary[i] = testDictionary[i].toUpperCase();
	}
	BoggleSolver solver = new BoggleSolver(testDictionary);
	assertEquals(2,solver.scoreOf("HELLO"));
	assertEquals(2,solver.scoreOf("WORLD"));
	assertEquals(1,solver.scoreOf("QUEUE"));
    }

}
