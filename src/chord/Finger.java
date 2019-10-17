package chord;

public class Finger {
    private int start;
    private Node node;

    Finger(int start, Node node) {
        this.start = start;
        this.node = node;
    }

    public int getStart() {
        return start;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
