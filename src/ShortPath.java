import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class ShortPath {

    // Creates the graph based on the maze structure while adding the nodes
    public static Map<Node, List<Edge>> createGraph(char[][] maze) {
        Map<Node, List<Edge>> graph = new HashMap<>();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] != '0') { // Not a rock
                    Node node = new Node(new Point(i, j));
                    graph.put(node, new ArrayList<>());
                    addEdgesForNode(node, graph, maze);
                }
            }
        }
        return graph;
    }

    // Adds the edges to the nodes
    private static void addEdgesForNode(Node node, Map<Node, List<Edge>> graph, char[][] maze) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int k = 0; k < 4; k++) {
            int nx = node.position.x;
            int ny = node.position.y;
            int slideCost = 0;
            while (true) {
                nx += dx[k];
                ny += dy[k];
                // Check bounds and rock conditions
                if (nx < 0 || nx >= maze.length || ny < 0 || ny >= maze[0].length || maze[nx][ny] == '0') {
                    // If it stops before hitting a boundary or a rock, add the last valid position as an edge
                    if (slideCost > 0) {
                        Node neighbor = new Node(new Point(nx - dx[k], ny - dy[k]));
                        if (!neighbor.equals(node)) {
                            // Avoid adding an edge to itself
                            graph.get(node).add(new Edge(node, neighbor, slideCost));
                        }
                    }
                    break;
                }
                if (maze[nx][ny] == 'F') {
                    // If it encounters 'F', add this as the destination and stop sliding
                    Node neighbor = new Node(new Point(nx, ny));
                    graph.get(node).add(new Edge(node, neighbor, slideCost + 1));
                    break;
                }
                slideCost++;
            }
        }
    }

    // The algorithm used to identify the shortest path
    public static List<Node> aStar(Map<Node, List<Edge>> graph, Node start, Node end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
        Map<Node, Integer> gScores = new HashMap<>();
        Set<Node> closedSet = new HashSet<>();

        gScores.put(start, 0);
        start.fScore = heuristic(start.position, end.position);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.equals(end)) {
                return reconstructPath(current);
            }
            closedSet.add(current);

            // Explore the neighbors and update the scores accordingly for the next iteration of the loop 
            for (Edge edge : graph.get(current)) {
                Node neighbor = edge.end;
                if (closedSet.contains(neighbor)) continue;

                int tentativeGScore = gScores.getOrDefault(current, Integer.MAX_VALUE) + edge.cost;
                if (tentativeGScore < gScores.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    neighbor.parent = current;
                    gScores.put(neighbor, tentativeGScore);
                    neighbor.fScore = tentativeGScore + heuristic(neighbor.position, end.position);

                    // Determine the direction of movement
                    int dx = neighbor.position.x - current.position.x;
                    int dy = neighbor.position.y - current.position.y;
                    if (dx > 0) neighbor.direction = "Move Down to";
                    else if (dx < 0) neighbor.direction = "Move Up to";
                    else if (dy > 0) neighbor.direction = "Move Right to";
                    else if (dy < 0) neighbor.direction = "Move Left to";

                    // Add the neighbor to the open set or update its position in the open set
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else {
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    // Calculates the heuristic cost from the current node to the goal node
    private static int heuristic(Point current, Point goal) {
        return Math.abs(current.x - goal.x) + Math.abs(current.y - goal.y);
    }

    // Reconstructs the path from the start node to the end node
    private static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    // ANSI color codes for path visualization
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    // Marks the path in the maze and prints it
    public static void markPathInMaze(char[][] maze, List<Node> path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        // Filling in the path with directional symbols
        for (int i = 0; i < path.size() - 1; i++) {
            Node startNode = path.get(i);
            Node endNode = path.get(i + 1);
            Point start = startNode.position;
            Point end = endNode.position;

            int dx = Integer.compare(end.x, start.x);
            int dy = Integer.compare(end.y, start.y);

            // Direction characters
            char direction = ' ';
            if (dx == 0 && dy > 0) direction = '→'; // Right
            else if (dx == 0 && dy < 0) direction = '←'; // Left
            else if (dx > 0 && dy == 0) direction = '↓'; // Down
            else if (dx < 0 && dy == 0) direction = '↑'; // Up

            // Fill in the path with directional symbols and color
            Point current = new Point(start.x, start.y);
            while (!current.equals(end)) {
                maze[current.x][current.y] = direction;
                current.x += dx;
                current.y += dy;
            }
        }

        // marking the end position
        Node lastNode = path.get(path.size() - 1);
        maze[lastNode.position.x][lastNode.position.y] = 'F';

        // marking the start position
        Node firstNode = path.get(0);
        maze[firstNode.position.x][firstNode.position.y] = 'S';

        // Print the maze with path in red
        printMazeInColor(maze);
    }

    // Print the maze with ANSI color codes for path visualization
    public static void printMazeInColor(char[][] maze) {
        for (char[] row : maze) {
            for (char cell : row) {
                if (cell == '→' || cell == '←' || cell == '↓' || cell == '↑') {
                    System.out.print(ANSI_RED + cell + ANSI_RESET + " ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }

    //convert the file path to the file name
    public static String getFileName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf("/");
        return filePath.substring(lastSlashIndex + 1);
    }
}
