import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private static final int VIRTUAL_TOP = 0;
  private final boolean[][] grid;
  private final int n;
  private final int virtualBot;
  private final WeightedQuickUnionUF uf;
  private final WeightedQuickUnionUF ufBack;
  private int openSites;


  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    virtualBot = n * n + 1;
    uf = new WeightedQuickUnionUF(n * n + 2);
    ufBack = new WeightedQuickUnionUF(n * n + 1);
    grid = new boolean[n][n];
    openSites = 0;
  }

  public void open(int row, int col) {
    checker(row, col);

    if (!isOpen(row, col)) {
      grid[row - 1][col - 1] = true;
      openSites++;

      // top row
      if (row == 1) {
        uf.union(findIndex(row, col), VIRTUAL_TOP);
        ufBack.union(findIndex(row, col), VIRTUAL_TOP);
      }

      // last row
      if (row == n) {
        uf.union(findIndex(row, col), virtualBot);
      }

      // middle rows
      if (row > 1 && isOpen(row - 1, col)) {
        uf.union(findIndex(row, col), findIndex(row - 1, col));
        ufBack.union(findIndex(row, col), findIndex(row - 1, col));
      }
      if (row < n && isOpen(row + 1, col)) {
        uf.union(findIndex(row, col), findIndex(row + 1, col));
        ufBack.union(findIndex(row, col), findIndex(row + 1, col));
      }
      if (col > 1 && isOpen(row, col - 1)) {
        uf.union(findIndex(row, col), findIndex(row, col - 1));
        ufBack.union(findIndex(row, col), findIndex(row, col - 1));
      }
      if (col < n && isOpen(row, col + 1)) {
        uf.union(findIndex(row, col), findIndex(row, col + 1));
        ufBack.union(findIndex(row, col), findIndex(row, col + 1));
      }
    }
  }

  public boolean isOpen(int row, int col) {
    checker(row, col);
    return grid[row - 1][col - 1];
  }

  public boolean isFull(int row, int col) {
    if ((row > 0 && row <= n) && (col > 0 && col <= n)) {
      return isOpen(row, col) && ufBack.find(VIRTUAL_TOP) == ufBack.find(findIndex(row, col));
    } else {
      throw new IllegalArgumentException();
    }
  }

  public int numberOfOpenSites() {
    return openSites;
  }

  public boolean percolates() {
    return uf.find(VIRTUAL_TOP) == uf.find(virtualBot);
  }

  private int findIndex(int row, int col) {
    return n * (row - 1) + col;
  }

  private void checker(int row, int col) {
    if (row <= 0 || row > n || col <= 0 || col > n) {
      throw new IllegalArgumentException();
    }
  }

}