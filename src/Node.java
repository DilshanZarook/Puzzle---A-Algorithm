import java.awt.Point;
import java.util.Objects;

class Node {
    Point position;
    int gScore;
    int fScore;
    Node parent;
    String direction; 

    // Constructor to initialize the node with a position and default values for gScore and fScore 
    public Node(Point position) {
        this.position = position;
        this.gScore = Integer.MAX_VALUE;
        this.fScore = Integer.MAX_VALUE;
        this.direction = ""; 
    }

    // Constructor to initialize the node with a position and gScore 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return position.equals(node.position);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}