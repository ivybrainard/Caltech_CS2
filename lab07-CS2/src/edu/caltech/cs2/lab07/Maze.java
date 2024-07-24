package edu.caltech.cs2.lab07;

import edu.caltech.cs2.libraries.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
    public int n;                 // dimension of maze
    public boolean[][] north;     // is there a wall to north of cell i, j
    public boolean[][] east;
    public boolean[][] south;
    public boolean[][] west;
    public boolean done = false;
    public Point end;
    private static final int DRAW_WAIT = 4;

    public Maze(int n, String mazeFile) throws FileNotFoundException {
        this.n = n;
        end = new Point(n / 2, n / 2);
        StdDraw.setXscale(0, n + 2);
        StdDraw.setYscale(0, n + 2);
        init();

        Scanner scanner = new Scanner(new File(mazeFile));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            assert tokens.length == 3;
            String direction = tokens[0];
            int x = Integer.valueOf(tokens[1]);
            int y = Integer.valueOf(tokens[2]);
            switch (direction) {
                case "N":
                    north[x][y] = false;
                    break;
                case "S":
                    south[x][y] = false;
                    break;
                case "E":
                    east[x][y] = false;
                    break;
                case "W":
                    west[x][y] = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void init() {
        // initialze all walls as present
        north = new boolean[n+2][n+2];
        east  = new boolean[n+2][n+2];
        south = new boolean[n+2][n+2];
        west  = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < n+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }
    }

    // draw the maze
    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(n / 2.0 + 0.5, n / 2.0 + 0.5, 0.375);
        StdDraw.filledCircle(1.5, 1.5, 0.375);

        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) StdDraw.line(x, y, x + 1, y);
                if (north[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
                if (west[x][y]) StdDraw.line(x, y, x, y + 1);
                if (east[x][y]) StdDraw.line(x + 1, y, x + 1, y + 1);
            }
        }
        StdDraw.show();
        StdDraw.pause(1000);
    }

    // Draws a blue circle at coordinates (x, y)
    private void selectPoint(Point point) {
        int x = point.x;
        int y = point.y;
        System.out.println("Selected point: (" + x + ", " + y + ")");
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(DRAW_WAIT);
    }

    /*
     * Returns an array of all children to a given point
     */
    public Point[] getChildren(Point point) {
        int size = 0;
        if (north[point.x][point.y] == false) {
            size++;
        }
        if (south[point.x][point.y] == false) {
            size++;
        }
        if (east[point.x][point.y] == false) {
            size++;
        }
        if (west[point.x][point.y] == false) {
            size++;
        }
        Point[] array = new Point[size];

        int idx = 0;

        if (north[point.x][point.y] == false) {
            Point newNorth = new Point(point.x, point.y + 1);
            array[idx] = newNorth;
            newNorth.parent = point;
            idx++;
        }
        if (south[point.x][point.y] == false) {
            Point newSouth = new Point(point.x, point.y -1);
            array[idx] = newSouth;
            newSouth.parent = point;
            idx++;
        }
        if (east[point.x][point.y] == false) {
            Point newEast = new Point(point.x + 1, point.y);
            array[idx] = newEast;
            newEast.parent = point;
            idx++;
        }
        if (west[point.x][point.y] == false) {
            Point newWest = new Point(point.x-1, point.y);
            array[idx] = newWest;
            newWest.parent = point;
        }
        return array;
    }

    public void solveDFSRecursiveStart() {
        Point start = new Point(1, 1);
        solveDFSRecursive(start);
    }

    /*
     * Solves the maze using a recursive DFS. Calls selectPoint()
     * when a point to move to is selected.
     */
    private void solveDFSRecursive(Point point) {
        if (point.equals(this.end)) {
            selectPoint(point);
            done = true;

        } else if (!done) {
            selectPoint(point);
            Point[] listChild = getChildren(point);

            for (Point child : listChild) {
                if (!child.equals(point.parent)) {
                    solveDFSRecursive(child);
                }
            }
        }
    }

    /*
     * Solves the maze using an iterative DFS using a stack. Calls selectPoint()
     * when a point to move to is selected.
     */
    public void solveDFSIterative() {
        Point first = new Point(1, 1);
        selectPoint(first);

        Stack<Point> stackChild = new Stack<>();
        Point[] listChild = getChildren(first);

        for (Point child: listChild) {
            stackChild.push(child);
        }

        while (!stackChild.isEmpty()){
            Point currPoint = stackChild.pop();
            selectPoint(currPoint);

            if (currPoint.equals(this.end)){
                done = true;
                return;
            }

            Point[] listChild2 = getChildren(currPoint);

            for (Point child: listChild2) {
                if (!child.equals(currPoint.parent)) {
                    stackChild.push(child);
                }
            }
        }
    }
}

