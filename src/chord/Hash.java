package chord;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Hash {
    //4 byte length of hash key
    private static final int BYTE_LENGTH = 4;
    private static int hashLength = 4;

    public static int getHashLength() {
        return hashLength;
    }

    /**
     * calculated SHA-1 hash
     * @param input
     * 		the identifier
     * @return the hash value of the identifier
     */
    public static byte[] computeHash(String input) {
        byte[] value = null;
        try {
            // calculate initial SHA-1 hash
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            byte[] init_code = md.digest(input.getBytes());
            value = new byte[BYTE_LENGTH];
            int shrink = init_code.length / value.length;
            int count = 1;
            // shrink the SHA-1 hash to 4 bytes
            for (int i = 0; i < init_code.length * BYTE_LENGTH; i++) {
                int cur = ((init_code[i / BYTE_LENGTH] & (1 << (i % BYTE_LENGTH))) >> i
                        % BYTE_LENGTH);
                if (cur == 1) {
                    count++;
                }
                if (((i + 1) % shrink) == 0) {
                    int temp = (count % 2 == 0) ? 0 : 1;
                    count = 1;
                    value[i / shrink / BYTE_LENGTH] |= (temp << ((i / shrink) % BYTE_LENGTH));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * convert the byte[] hash to hex string
     * @param hash
     * @return hex string
     */
    public static String hashToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
