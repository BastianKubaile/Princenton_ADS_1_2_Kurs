import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.HexDump;

public class BWTester {
    
    private static FileInputStream in;
    private static PrintStream out;

    public static void main(String[] args) throws FileNotFoundException{
	try{
	    in = new FileInputStream("C:\\Java\\Programme\\PrincetonCourse\\testing\\textfiles\\abra.txt");
	    System.setIn(in);
	    out = new PrintStream("C:\\Java\\Programme\\PrincetonCourse\\testing\\textfiles\\temp.txt");
	    System.setOut(out);
	}catch(FileNotFoundException e){
	    throw e;
	}
	String[] tmp = new String[1];
	tmp[0] = "-";
	BurrowsWheeler.main(tmp);
	BinaryStdIn.close();
	BinaryStdOut.close();
	try{
	    in = new FileInputStream("C:\\Java\\Programme\\PrincetonCourse\\testing\\textfiles\\temp.txt");
	    System.setIn(in);
	    out = new PrintStream("C:\\Java\\Programme\\PrincetonCourse\\testing\\textfiles\\binary.txt");
	    System.setOut(out);
	}catch(FileNotFoundException ex){
	    throw ex;
	}
	HexDump.main(new String[0]);
	BinaryStdIn.close();
	BinaryStdOut.close();
    }

}
