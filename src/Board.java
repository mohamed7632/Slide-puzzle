import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class Board {
    private final int[][] tiles;
    private final int n;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;

        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, n);
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder out = new StringBuilder(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(tiles[i][j]).append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0, n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != (i * tiles.length + j + 1))
                    count++;


            }

        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) { //
                    int targetX = (tiles[i][j] - 1) / n;
                    int targetY = (tiles[i][j] - 1) % n;
                    int dx = i - targetX;
                    int dy = j - targetY;
                    manhattan += Math.abs(dx) + Math.abs(dy);
                }
            }

        }
        return manhattan;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.n == that.n && this.isEqual(that));
    }

    private boolean isEqual(Board y) {
        boolean check = true;
        for (int i = 0; i < n * n; i++) {
            if (this.tiles[i / n][i % n] != y.tiles[i / n][i % n]) {
                check = false;
                break;
            }
        }
        return check;
    }

    private void swap(int i, int j, int x, int y) {
        int t = tiles[i][j];
        tiles[i][j] = tiles[x][y];
        tiles[x][y] = t;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> s = new Stack<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    //go right
                    if (j < n - 1) {

                        swap(i, j + 1, i, j);
                        Board b = new Board(tiles);
                        s.push(b);
                        swap(i, j, i, j + 1);

                    }
                    //go left
                    if (j > 0) {

                        swap(i, j, i, j - 1);
                        Board b = new Board(tiles);
                        s.push(b);
                        swap(i, j - 1, i, j);
                    }
                    //go down
                    if (i < n - 1) {

                        swap(i, j, i + 1, j);
                        Board b = new Board(tiles);
                        s.push(b);
                        swap(i + 1, j, i, j);


                    }
                    //go up
                    if (i > 0) {
                        swap(i, j, i - 1, j);
                        Board b = new Board(tiles);
                        s.push(b);
                        swap(i - 1, j, i, j);
                    }

                }
            }
        }
        return s;


    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tiles);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (twin.tiles[i][j] != 0 && twin.tiles[i][j + 1] != 0) {
                    int temp = twin.tiles[i][j];
                    twin.tiles[i][j] = twin.tiles[i][j + 1];
                    twin.tiles[i][j + 1] = temp;
                    i = j = n;

                }
            }

        }
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        //int[][] arr = {{0, 5, 4}, {2, 3, 8}, {7, 1, 6}};
        int[][] arr = {{0, 2}, {3, 1}};
        Board b = new Board(arr);
        System.out.println(b.hamming());
        System.out.println(b.toString());
        System.out.println(b.twin().toString());
        System.out.println(b.dimension());
        StdOut.println(b.manhattan());
        System.out.println(b.isGoal());

        for (Board m : b.neighbors()) {
            System.out.println(m.toString());

        }
    }

}
