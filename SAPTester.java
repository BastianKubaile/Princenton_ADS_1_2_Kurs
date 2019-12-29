import static org.junit.Assert.*;

import org.junit.Test;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAPTester {

    @Test
    public void test() {
	In in = new In("C:\\Java\\Programme\\PrincetonCourse\\testing\\digraph2.txt");
	Digraph digraph = new Digraph(in);
	SAP sap = new SAP(digraph);
	
	assertEquals(3, sap.length(0, 3));
	assertEquals(2, sap.length(1, 3));
	assertEquals(0, sap.length(3, 3));
	assertEquals(1, sap.length(5, 4));
	in = new In("C:\\Java\\Programme\\PrincetonCourse\\testing\\digraph3.txt");
	digraph = new Digraph(in);
	sap = new SAP(digraph);
	
	assertEquals(3, sap.length(7, 10));
	assertEquals(3, sap.length(10, 7));
	assertEquals(10, sap.ancestor(7, 10));
	assertEquals(10, sap.ancestor(10, 7));
	assertEquals(5, sap.length(8, 13));
	assertEquals(5, sap.length(13, 8));
	assertEquals(8, sap.ancestor(8, 13));
	assertEquals(8, sap.ancestor(13, 8));
    }

}
