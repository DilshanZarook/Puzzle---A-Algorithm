import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("\n\t****************************************************");
        System.out.println("\t* A* Algorithm to Find the Shortest Path in a Maze *");
        System.out.println("\t****************************************************");

        //inseret the path of the maze file path here =>
        String filePath = "/Users/amanjanee/Desktop/ALGO CW/AlgoCW2024/DilshanZarook-20220088-w1953568-AlgoCW/Sample/benchmark_series/benchmark_series/puzzle_10.txt";

        // Initialize the MapParser with the maze file path
        MapParser parser = new MapParser(filePath);
        char[][] maze = parser.getMap();

        // Print the filename and printing the maze
        System.out.println("\nLoaded file: " + ShortPath.getFileName(filePath)+"\n");
        ShortPath.printMazeInColor(maze);

        // Create the graph from the maze
        Map<Node, List<Edge>> graph = ShortPath.createGraph(maze);
        Node start = null;
        Node end = null;

        // Find the start (S) and end (F) nodes in the graph
        for (Node node : graph.keySet()) {
            if (maze[node.position.x][node.position.y] == 'S') {
                start = node;
            } else if (maze[node.position.x][node.position.y] == 'F') {
                end = node;
            }
            if (start != null && end != null) {
                break;
            }
        }

        // Ensure both start and end are found
        if (start == null || end == null) {
            System.out.println("\nStart or end node not found in the maze.");
            return;
        }

        // perfromance test 
        long startTime = System.nanoTime();
        List<Node> path = ShortPath.aStar(graph, start, end);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime; // Time in nanoseconds

        // Check if a path was found
        if (path != null && !path.isEmpty()) {
            System.out.println("\nFootprint of Traversal \n");
            // Mark the path in the maze and print it
            ShortPath.markPathInMaze(maze, path);
            System.out.println("\nShortest Path Found:\n");

            // Print the steps with directions
            System.out.println("1) Start at " + " (" + (start.position.y+1) + "," + (start.position.x+1) + ") ");
            int step = 2;
            for (Node node : path) {
                if (node.direction != null && !node.direction.isEmpty()) {
                    System.out.println(step + ") "+ node.direction + " (" + (node.position.y+1) + "," + (node.position.x+1) + ") ");
                    step ++;
                }
            }
            System.out.println(step + ") Done!!\n");
            System.out.println("\n\t****************************************************");
            System.out.println("\t\t Elapsed time in milliseconds: " + elapsedTime / 1000000);
            System.out.println("\t****************************************************\n");
            
        } else {
            System.out.println("\nNo path found.\n");
        }
    }
}
