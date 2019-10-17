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
     *
     * @return singleton
     */
    public static MainTest getInstance() {
        return mainTest;
    }
    /**
     *
     * @param newNode
     * @return -1 : input number exceed limit
     *         1 : the node ring is full
     *         0 : insert node successfully.
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
     *
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
     *
     * @return array of node
     */
    public Node[] getNodeRing() {
        return nodeRing;
    }

    /**
     *
     * @return current number of nodes in the ring
     */
    public int getCurNumOfNode() {
        return curNumOfNode;
    }

    public static void main(String[] args) {
        MainTest mainTest = getInstance();
        Node newNode = new Node(1);
        Node newNode1 = new Node(6);
        Node newNode2 = new Node(3);
        Node newNode3 = new Node(2);
        Chord.join(newNode, null);
        Chord.join(newNode1, newNode);
        Chord.join(newNode2, newNode1);
        Chord.join(newNode3, newNode1);

        System.out.println(mainTest.getCurNumOfNode());
        System.out.println(Chord.find_successor(newNode, 7).nid);
        FingerTable ft = newNode.fingerTable;
    }
}
