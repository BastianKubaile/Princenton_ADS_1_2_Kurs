import static org.junit.Assert.*;

import org.junit.Test;

import edu.princeton.cs.algs4.StdOut;

public class BaseballEliminationTester {

    @Test
    public void test() {
	BaseballElimination division = new BaseballElimination("C:\\Java\\Programme\\PrincetonCourse\\testing\\baseball\\teams4.txt");
	assertEquals(false, division.isEliminated("Atlanta"));
	assertEquals(true, division.isEliminated("Philadelphia"));
	assertEquals(false, division.isEliminated("New_York"));
	assertEquals(true, division.isEliminated("Montreal"));
	division = new BaseballElimination("C:\\Java\\Programme\\PrincetonCourse\\testing\\baseball\\teams4a.txt");
	assertEquals(false, division.isEliminated("CIA"));
	assertEquals(true, division.isEliminated("Ghaddafi"));
	assertEquals(true, division.isEliminated("Bin_Ladin"));
	assertEquals(false, division.isEliminated("Obama"));
	division = new BaseballElimination("C:\\Java\\Programme\\PrincetonCourse\\testing\\baseball\\teams5.txt");
	assertEquals(true, division.isEliminated("Detroit"));
	division = new BaseballElimination("C:\\Java\\Programme\\PrincetonCourse\\testing\\baseball\\teams7.txt");
	assertEquals(true, division.isEliminated("Ireland"));
	division = new BaseballElimination("C:\\Java\\Programme\\PrincetonCourse\\testing\\baseball\\teams42.txt");
	assertEquals(true, division.isEliminated("Team6"));
	assertEquals(true, division.isEliminated("Team15"));
	assertEquals(true, division.isEliminated("Team25"));
    }

}
