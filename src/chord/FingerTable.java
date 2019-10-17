package chord;

import java.util.ArrayList;
import java.util.List;

public class FingerTable {
    private List<Finger> fingers;
    private int tableLength;

    FingerTable(Node node) {
        /**
         * initialize the finger table
         */
        this.tableLength = (int)(Math.log(Chord.NUMBER_LIMIT) / Math.log(2));
        this.fingers = new ArrayList<>();
        for (int i = 0; i < tableLength; i++) {
            int start = (node.nid + (int)Math.pow(2, i)) % Chord.NUMBER_LIMIT;
            //get the node of finger[i].start
            fingers.add(new Finger(start, null));
        }
    }
    public int getNumOfFingers() {
        return tableLength;
    }
    /**
     * @param i
     * @return the ith finger
     */
    public Finger getFinger(int i) {
        return fingers.get(i);
    }
}
