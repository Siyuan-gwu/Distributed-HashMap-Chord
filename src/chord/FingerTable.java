package chord;

import java.util.ArrayList;
import java.util.List;

public class FingerTable {
    private List<Finger> fingers;

    FingerTable(Node node) {
        /**
         * initialize the finger table
         */
        this.fingers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int start = node.nid + Helper.getPowerOfTwo(i);
            //get the node of finger[i].start
            Node successor = node.find_successor(node, start);
            fingers.add(new Finger(start, successor));
        }
    }
    public int getNumOfFingers() {
        return fingers.size();
    }

    /**
     *
     * @param i
     * @return the ith finger
     */
    public Finger getFinger(int i) {
        return fingers.get(i);
    }
}
