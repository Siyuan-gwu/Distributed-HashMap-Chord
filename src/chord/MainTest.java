package chord;

public class MainTest {
    private Node[] nodeRing;
    public static final int NUMBER_LIMIT = 8;
    private int curNumOfNode;
    private static final MainTest mainTest = new MainTest();

    private MainTest() {
        nodeRing = new Node[NUMBER_LIMIT];
        curNumOfNode = 0;
    }

    /**
     * @return singleton
     */
    public static MainTest getInstance() {
        return mainTest;
    }

    /**
     * @param newNode
     * @return -1 : input number exceed limit
     * 1 : the node ring is full
     * 0 : insert node successfully.
     */
    public int addNodeToRing(Node newNode) {
        if (newNode.nid >= NUMBER_LIMIT) {
            return -1;
        } else if (curNumOfNode >= NUMBER_LIMIT) {
            return 0;
        }
        nodeRing[newNode.nid] = newNode;
        curNumOfNode++;
        return 0;
    }

    /**
     * delete a node from the ring
     *
     * @param id
     * @return
     */
    public boolean deleteNodeFromRing(int id) {
        Node delete = getNodeById(id);
        if (delete == null) {
            return false;
        }
        nodeRing[id] = null;
        curNumOfNode--;
        return true;
    }

    /**
     * @param id
     * @return ith node
     */
    public Node getNodeById(int id) {
        if (id >= NUMBER_LIMIT) {
            return null;
        }
        return nodeRing[id];
    }

    /**
     * @return array of node
     */
    public Node[] getNodeRing() {
        return nodeRing;
    }

    /**
     * @return current number of nodes in the ring
     */
    public int getCurNumOfNode() {
        return curNumOfNode;
    }

    public static void main(String[] args) {
        MainTest mainTest = getInstance();
        Node newNode0 = new Node(0);
        Node newNode1 = new Node(1);
        Node newNode3 = new Node(3);
        Node newNode6 = new Node(6);
        Chord.join(newNode0, null);
        Chord.join(newNode3, newNode0);
        Chord.join(newNode6, newNode3);
        Chord.join(newNode1, newNode6);
        System.out.println(Chord.find_successor(newNode0, 7).nid);
        FingerTable ft = newNode0.fingerTable;
        for (int i = 0; i < ft.getNumOfFingers(); i++) {
            System.out.println(ft.getFinger(i).getNode().nid);
        }
    }
}
