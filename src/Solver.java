import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final SearchNode head;
    private Boolean isSolvable;
    private SearchNode res;


    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private int moves;
        SearchNode previous;

        private final int priority;


        public SearchNode(Board board) {

            this.board = board;
            moves = 0;
            previous = null;

            priority = this.board.manhattan() + moves;

        }


        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);

        }


    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        isSolvable = true;
        res = null;

        MinPQ<SearchNode> pqM = new MinPQ<>();
        MinPQ<SearchNode> pqT = new MinPQ<>();
        head = new SearchNode(initial);
        SearchNode twin = new SearchNode(initial.twin());
        //min solution
        pqM.insert(head);
        SearchNode min;
        //twin solution
        //  System.out.println(twin.board.toString());
        pqT.insert(twin);
        //System.out.println("size " + pqT.size());
        while (true) {
            min = pqM.delMin();
            twin = pqT.delMin();
            if (twin.board.isGoal()) {
                this.isSolvable = false;
                break;
            }
            if (min.board.isGoal()) {
                res = min;
                break;

            } else {


                for (Board board : min.board.neighbors()) {
                    SearchNode prev = min.previous;
                    boolean seen = false;
                    if (prev != null) {
                        if (board.equals(prev.board)) {
                            seen = true;
                        }
                    }
                    if (!seen) {

                        SearchNode CurrentMin = new SearchNode(board);
                        CurrentMin.moves = min.moves + 1;
                        CurrentMin.previous = min;
                        pqM.insert(CurrentMin);

                    }


                }
                for (Board board : twin.board.neighbors()) {
                    SearchNode prev = twin.previous;
                    boolean seen = false;
                    if (prev != null) {
                        if (board.equals(prev.board)) {
                            seen = true;
                        }
                    }
                    if (!seen) {
                        SearchNode CurrentMin = new SearchNode(board);
                        CurrentMin.moves = twin.moves + 1;
                        CurrentMin.previous = twin;
                        pqT.insert(CurrentMin);

                    }


                }
            }

        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable)
            return -1;
        return res.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> s = new Stack<>();
        SearchNode curr = res;
        while (!curr.board.equals(head.board)) {
            s.push(curr.board);
            curr = curr.previous;

        }
        return s;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

