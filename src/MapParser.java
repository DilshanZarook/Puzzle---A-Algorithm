import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapParser {
    private char[][] map;
    private List<int[]> rocks;

    public MapParser(String filePath) {
        // Initialize the rocks list
        rocks = new ArrayList<>();
        try {
            parseMap(filePath);
        } catch (IOException e) {
            System.out.println("Error reading map file: " + e.getMessage());
        }
    }

    // Method to parse the map file
    private void parseMap(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            int width = lines.get(0).length();
            int height = lines.size();
            map = new char[height][width];

            for (int i = 0; i < height; i++) {
                String currentLine = lines.get(i);
                for (int j = 0; j < width; j++) {
                    char currentChar = currentLine.charAt(j);
                    map[i][j] = currentChar;

                    if (currentChar == 'S') {
                        // Handle 'S' character
                    } else if (currentChar == 'F') {
                        // Handle 'F' character
                    } else if (currentChar == '0') {
                        rocks.add(new int[]{i, j});
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading map file: " + e.getMessage());
        }
    }

    public char[][] getMap() {
        return map;
    }
}
