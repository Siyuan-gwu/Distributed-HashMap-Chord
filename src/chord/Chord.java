package chord;

public class Chord {
    /**
     * Look up.
     *
     * @param node
     * @param id
     * @return id's successor node
     */
    public static Node find_successor(Node node, int id) {
        if (node.nid == id) {
            return node;
        }
        Node prevNode = find_predecessor(node, id);
        return prevNode.getSuccessor();
    }

    private static Node find_predecessor(Node node, int id) {
        Node temp = node;
        while (!inCurInterval(temp, temp.getSuccessor(), id)) {
            temp = closest_preceding_finger(temp, id);
        }
        return temp;
    }

    private static boolean inCurInterval(Node cur, Node next, int id) {
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

    private static Node closest_preceding_finger(Node cur, int id) {
        FingerTable fingers = cur.fingerTable;
        for (int i = fingers.getNumOfFingers() - 1; i >= 0; i--) {
            Node finger = fingers.getFinger(i).getNode();
            if (!inCurInterval(cur, finger, id)) {
                return finger;
            }
        }
        return cur;
    }

    private static int getId(Node node) {
        return node.nid;
    }

    /**
     * join a node to the ring
     *
     * @param node
     * @param existNode
     */
    public static void join(Node node, Node existNode) {
        // need to edit
        //add node to the ring
        MainTest mainTest = MainTest.getInstance();
        int result = mainTest.addNodeToRing(node);
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
            for (int i = 0; i < 3; i++) {
                node.fingerTable.getFinger(i).setNode(node);
            }
            node.predecessor = node;
        }
        //System.out.println("join node No." + node.nid + " successfully.");
    }

    private static void initFingerTable(Node newNode, Node existNode) {
        FingerTable ft = newNode.fingerTable;
//        Node temp = find_successor(existNode, existNode.fingerTable.getFinger(0).getStart());
        Node temp = find_successor(existNode, ft.getFinger(0).getStart());
        System.out.println("successor node id is: " + temp.nid);

        ft.getFinger(0).setNode(temp);
//        System.out.println("initial table: finger id is " + ft.getFinger(0).getNode().nid);
        newNode.predecessor = temp.predecessor;
        temp.predecessor = newNode;
        for (int i = 0; i < 2; i++) {
            int start = ft.getFinger(i + 1).getStart();
            /**
             * need to be edited
             */
            if (judgement(start, newNode, i)) {
                ft.getFinger(i + 1).setNode(ft.getFinger(i).getNode());
                //System.out.println("The " + (i+1) + "th finger is " + ft.getFinger(i + 1).getNode().nid);
            } else {
                Node cur = find_successor(existNode, ft.getFinger(i + 1).getStart());
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
    private static void updateOthers(Node newNode) {
        for (int i = 0; i < 3; i++) {
            int id = getId(newNode) - (int) Math.pow(2, i);
            if (id < 0) {
                id += MainTest.NUMBER_LIMIT;
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
    private static void updateFingerTable(Node needUpdate, Node newNode, int i) {
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

    private static boolean judgement(int start, Node node, int i) {
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
    public static void leave(Node leaveNode) {
        MainTest mainTest = MainTest.getInstance();
        if (!mainTest.deleteNodeFromRing(leaveNode.nid)) {
            System.out.println("The node doesn't exist.");
            return;
        }
        Node prev = leaveNode.predecessor;
        Node succ = leaveNode.getSuccessor();
        for(int i = 0; i < 3; i++){
            int id = getId(leaveNode) - (int)Math.pow(2,i);
            if (id < 0) {
                id += MainTest.NUMBER_LIMIT;
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
    private static void updateFingerTable1(Node needUpdate, Node leaveNode,int i,Node succ){
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
