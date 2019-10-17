package chord;

public class Chord {
    private Node[] nodeRing;
    public static final int NUMBER_LIMIT = 8;
    private int curNumOfNode;
    private static final Chord chord = new Chord();

    private Chord() {
        nodeRing = new Node[NUMBER_LIMIT];
        curNumOfNode = 0;
    }

    /**
     * @return singleton
     */
    public static Chord getInstance() {
        return chord;
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
        if (id >= NUMBER_LIMIT || id < 0) {
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
    /**
     * Look up.
     * @param node
     * @param id
     * @return id's successor node
     */
    public Node lookup(Node node, int id) {
        if (node.nid == id) {
            return node;
        }
        Node prevNode = find_predecessor(node, id);
        return prevNode.getSuccessor();
    }

    private Node find_predecessor(Node node, int id) {
        Node temp = node;
        while (!inCurInterval(temp, temp.getSuccessor(), id)) {
            temp = closest_preceding_finger(temp, id);
        }
        return temp;
    }

    private boolean inCurInterval(Node cur, Node next, int id) {
        int curId = cur.nid;
        int nextId = next.nid;
        if (curId > nextId) {
            return id > curId || id <= nextId;
        } else if (curId < nextId) {
            //find the successor
            return id > curId && id <= nextId;
        } else {
            return true;
        }
    }

    private Node closest_preceding_finger(Node cur, int id) {
        FingerTable fingers = cur.fingerTable;
        for (int i = fingers.getNumOfFingers() - 1; i >= 0; i--) {
            Node finger = fingers.getFinger(i).getNode();
            if (!inCurInterval(cur, finger, id)) {
                return finger;
            }
        }
        return cur;
    }

    private int getId(Node node) {
        return node.nid;
    }

    /**
     * join a node to the ring
     *
     * @param node
     * @param existNode
     */
    public void join(Node node, Node existNode) {
        // need to edit
        //add node to the ring
        int result = chord.addNodeToRing(node);
        if (result == -1) {
            System.out.println("input number exceed limit, please try again.");
            return;
        } else if (result == 1) {
            System.out.println("The ring is full.");
            return;
        }

        if (existNode != null) {
            initFingerTable(node, existNode);
            updateOthers(node);
        } else {
            // the join node is the only node in the chord
            FingerTable ft = node.fingerTable;
            for (int i = 0; i < ft.getNumOfFingers(); i++) {
                ft.getFinger(i).setNode(node);
            }
            node.predecessor = node;
        }
        //System.out.println("join node No." + node.nid + " successfully.");
    }

    private void initFingerTable(Node newNode, Node existNode) {
        FingerTable ft = newNode.fingerTable;
//        Node temp = find_successor(existNode, existNode.fingerTable.getFinger(0).getStart());
        Node temp = lookup(existNode, ft.getFinger(0).getStart());
        System.out.println("successor node id is: " + temp.nid);

        ft.getFinger(0).setNode(temp);
//        System.out.println("initial table: finger id is " + ft.getFinger(0).getNode().nid);
        newNode.predecessor = temp.predecessor;
        temp.predecessor = newNode;
        for (int i = 0; i < ft.getNumOfFingers() - 1; i++) {
            int start = ft.getFinger(i + 1).getStart();
            /**
             * need to be edited
             */
            if (judgement(start, newNode, i)) {
                ft.getFinger(i + 1).setNode(ft.getFinger(i).getNode());
                //System.out.println("The " + (i+1) + "th finger is " + ft.getFinger(i + 1).getNode().nid);
            } else {
                Node cur = lookup(existNode, ft.getFinger(i + 1).getStart());
                ft.getFinger(i + 1).setNode(cur);
            }
        }
    }

    //    private static void updateOthers(Node newNode) {
//        for (int i = 0; i < 3; i++) {
//            int id = getId(newNode) - (int) Math.pow(2, i);
//            if (id < 0) {
//                id += MainTest.NUMBER_LIMIT;
//            }
//            Node needUpdate = find_predecessor(newNode, id);
//            updateFingerTable(needUpdate, newNode, i);
//        }
//    }
    private void updateOthers(Node newNode) {
        FingerTable ft = newNode.fingerTable;
        for (int i = 0; i < ft.getNumOfFingers(); i++) {
            int id = getId(newNode) - (int) Math.pow(2, i);
            if (id < 0) {
                id += NUMBER_LIMIT;
            }
            if (newNode.predecessor.nid == id) {
                updateFingerTable(newNode.predecessor, newNode, i);
            } else {
                Node needUpdate = find_predecessor(newNode, id);
                updateFingerTable(needUpdate, newNode, i);
            }
        }
    }

    /**
     * @param needUpdate
     * @param newNode
     * @param i
     * @INFO: This method has been modified.Å
     */
    private void updateFingerTable(Node needUpdate, Node newNode, int i) {
        //the nid of newNode
        if (needUpdate == newNode) {
            return;
        }
        int newNodeId = getId(newNode);
        FingerTable ft = needUpdate.fingerTable;
        //if newNode is ith finger of node(needModify), then update needModify.fingers[i] = newNode
        if (judgement(newNodeId, needUpdate, i)) {
            ft.getFinger(i).setNode(newNode);
            Node prev = needUpdate.predecessor;
            /**
             * need to update the finger[i] of previous nodes
             */
            updateFingerTable(prev, newNode, i);
        }
    }

    private boolean judgement(int start, Node node, int i) {
        int left = getId(node);
        int right = getId(node.fingerTable.getFinger(i).getNode());
        if (left < right) {
            return (start >= left && start < right);
        } else {
            return start >= left || start < right;
        }
    }

    /**
     * delete a node from the ring
     *
     * @param leaveNode
     */
    public void leave(Node leaveNode) {
        if (!chord.deleteNodeFromRing(leaveNode.nid)) {
            System.out.println("The node doesn't exist.");
            return;
        }
        Node prev = leaveNode.predecessor;
        Node succ = leaveNode.getSuccessor();
        FingerTable ft = leaveNode.fingerTable;
        for(int i = 0; i < ft.getNumOfFingers(); i++){
            int id = getId(leaveNode) - (int)Math.pow(2,i);
            if (id < 0) {
                id += NUMBER_LIMIT;
            }
            if(prev.nid == id){
                updateFingerTable1(prev,leaveNode,i,succ);
            }else {
                Node needUpdate = find_predecessor(leaveNode, id);
                updateFingerTable1(needUpdate, leaveNode, i,succ);
            }

        }
        // initial the leave node
        leaveNode.fingerTable = new FingerTable(leaveNode);
        leaveNode.predecessor = null;
        succ.predecessor = prev;
    }
    private void updateFingerTable1(Node needUpdate, Node leaveNode,int i,Node succ){
        if (needUpdate == leaveNode) {
            return;
        }
        FingerTable ft = needUpdate.fingerTable;
        if(ft.getFinger(i).getNode() == leaveNode) {
            ft.getFinger(i).setNode(succ);
            Node prev = needUpdate.predecessor;
            updateFingerTable1(prev, leaveNode, i, succ);
        }
        /**
         * time complexity is O(n) need improve here
         */
//        int leaveId = leaveNode.nid;
//        if(judgement(leaveId,needUpdate,i)){
//            if(ft.getFinger(i).getNode() == leaveNode){
//                ft.getFinger(i).setNode(succ);
//            }
//            Node prev = needUpdate.predecessor;
//            updateFingerTable1(prev,leaveNode,i,succ);
//        }
    }
}
