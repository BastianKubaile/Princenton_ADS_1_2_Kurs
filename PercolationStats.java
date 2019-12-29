import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats { 
    private int trials;
    private double[] fractions;
    private double mean;
    private double stddev;
    
    public PercolationStats(int n, int trials) { 
	if (trials <= 0) throw new IllegalArgumentException();
	this.trials = trials;
	fractions = new double[this.trials];
	
	for (int i = 0; i<trials; i++) { 
	   Percolation p = new Percolation(n);
	     while (!p.percolates()) { 
		int col = StdRandom.uniform(1,n+1);
		int row = StdRandom.uniform(1,n+1);
		if (!p.isOpen(row, col)){
		    p.open(row, col);
		}
	    }
	    fractions[i] = ((double)p.numberOfOpenSites()/((double)n*n));
	}
	
       	mean = StdStats.mean(fractions);
	stddev = StdStats.stddev(fractions);
    }
    
    public double mean() { 
	return mean;
    }
    
    public double stddev() { 
	return stddev;
    }
    
    public double confidenceLo() { 
	return (this.mean()-((1.96*this.stddev())/Math.sqrt(trials)));
    }
    
    public double confidenceHi() { 
	return (this.mean()+((1.96*this.stddev())/Math.sqrt(trials)));
    }
    
    public static void main(String[] args) { 
	int n = Integer.parseInt("20");
	int t = Integer.parseInt("10");
	PercolationStats stats = new PercolationStats(n, t);
	System.out.println("mean = "+ stats.mean());
	System.out.println("stddev = "+ stats.stddev());
	System.out.println("95% confidence interval  = ["+ stats.confidenceLo()+ ", "+ stats.confidenceHi() + "]");
    }
    
}
