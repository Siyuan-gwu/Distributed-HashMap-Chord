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
        for (int i = 0; i < 3; i++) {
            int start = (node.nid + (int)Math.pow(2, i)) % MainTest.NUMBER_LIMIT;
            //get the node of finger[i].start
            Node fingerNode = node;
            fingers.add(new Finger(start, fingerNode));
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
