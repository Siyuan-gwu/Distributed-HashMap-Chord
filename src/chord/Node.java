package chord;

public class Node {
    int nid;
    Node predecessor;
    FingerTable fingerTable;
    //exist or not

    public Node(int nid) {
        this.nid = nid;
        this.predecessor = null;
        this.fingerTable = new FingerTable(this);
    }

    /**
     * get the successor of a node
     * @return
     */
    public Node getSuccessor() {
        return this.fingerTable.getFinger(0).getNode();
    }

}
