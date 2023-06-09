import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double CONFIDENCE_95 = 1.96;
  private final int trials;
  private final int n;
  private final double[] avg;


  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    this.trials = trials;

    avg = new double[trials];
    for (int t = 0; t < trials; t++) {
      Percolation pr = new Percolation(n);
      int openSites = 0;
      while (!pr.percolates()) {
        int i = StdRandom.uniformInt(1, n + 1);
        int j = StdRandom.uniformInt(1, n + 1);
        if (!pr.isOpen(i, j)) {
          pr.open(i, j);
          openSites++;
        }
      }

      double fraction = (double) openSites / n * n;
      avg[t] = fraction;
    }

  }


  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(avg) / (double) (n * n);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(avg) / (double) (n * n);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
  }

  // test client (see below)
  public static void main(String[] args) {
    PercolationStats ps =
        new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

    String s = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";
    StdOut.println("mean                    = " + ps.mean());
    StdOut.println("stddev                  = " + ps.stddev());
    StdOut.println("95% confidence interval = " + s);

  }
}
