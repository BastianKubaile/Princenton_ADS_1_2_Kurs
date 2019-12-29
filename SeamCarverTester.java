import static org.junit.Assert.*;

import org.junit.Test;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarverTester {

    @Test
    public void test() {
	fail("Not yet implemented");
    }
    
    public static void main(String[] args){
	/*Energy works fine
	StdOut.println("1x1 Image");
	PrintEnergy.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\1x1.png");
	StdOut.println("1x8 Image");
	PrintEnergy.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\1x8.png");
	StdOut.println("3x4 Image");
	PrintEnergy.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\3x4.png");
	StdOut.println("8x1 Image");
	PrintEnergy.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\8x1.png");
	StdOut.println("10x10 Image");
	PrintEnergy.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\10x10.png");
	
	StdOut.println("1x1 Image");
	PrintSeams.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\1x1.png");*/
	
	StdOut.println("3x4 Image");
	PrintEnergy.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\3x4.png");
	PrintSeams.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\3x4.png");
	Picture picture = new Picture("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\3x4.png");
	SeamCarver carver = new SeamCarver(picture);
	int[] vSeam = carver.findVerticalSeam();
	carver.removeVerticalSeam(vSeam);
	int[] hSeam = carver.findHorizontalSeam();
	carver.removeHorizontalSeam(hSeam);
	
	/*
	StdOut.println("1x8 Image");
	PrintSeams.main("C:\\Java\\Programme\\PrincetonCourse\\testing\\seams\\1x8.png");*/
    }

}
