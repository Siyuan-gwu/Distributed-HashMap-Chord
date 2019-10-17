package chord;

public class MainTest {

    public static void main(String[] args) {
        Chord chord = Chord.getInstance();
        Node newNode0 = new Node(0);
        Node newNode1 = new Node(1);
        Node newNode3 = new Node(3);
        Node newNode6 = new Node(6);
        chord.join(newNode0, null);
        chord.join(newNode3, newNode0);
        chord.join(newNode6, newNode3);
        chord.join(newNode1, newNode6);
        //Chord.leave(newNode6);
        System.out.println(chord.lookup(newNode0, 7).nid);
        FingerTable ft = newNode1.fingerTable;
        for (int i = 0; i < ft.getNumOfFingers(); i++) {
            System.out.println(ft.getFinger(i).getNode().nid);
        }
    }
}
