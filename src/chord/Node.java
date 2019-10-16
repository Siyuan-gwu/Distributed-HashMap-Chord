package chord;

public class Node {
    int nid;
    Node predecessor;
    Node successor;
    FingerTable fingerTable;
    //exist or not

    public Node(int nid) {
        this.nid = nid;
        this.predecessor = null;
        this.successor = this;
        this.fingerTable = new FingerTable(this);
    }
    /**
     * Look up.
     * @param node
     * @param id
     * @return id's successor node
     */
    public Node find_successor(Node node, int id) {
        Node prevNode = find_predecessor(node, id);
        return prevNode.successor;
    }
    private Node find_predecessor(Node node, int id) {
        Node temp = node;
        while (!inCurInterval(temp, temp.successor, id)) {
            temp = closest_preceding_finger(temp, id);
        }
        return temp;
    }
    private boolean inCurInterval(Node cur, Node next, int id) {
        if (next == null) {
            return true;
        }
        int curId = cur.nid;
        int nextId = next.nid;
        if (curId >= nextId) {
            //reach the end of the ring
            if (curId < id || id <= nextId) {
                return true;
            }
        } else if (curId < id && id <= nextId) {
            //find the successor
            return true;
        }
        return false;
    }
    private Node closest_preceding_finger(Node cur, int id) {
        FingerTable fingers = cur.fingerTable;
        for (int i = fingers.getNumOfFingers() - 1; i >= 0; i--) {
            Node finger = fingers.getFinger(i).node;
            if (!inCurInterval(cur, finger, id)) {
                return finger;
            }
        }
        return cur;
    }

    /**
     * @TODO updateFingers, join, leave
     */

}
