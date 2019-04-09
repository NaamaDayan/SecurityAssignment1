import java.util.List;

/**
 * Created by Naama on 04/04/2019.
 */
public class Decryption {

    private byte[] cypher;
    private List<byte[]> keys;

    public Decryption(byte[] cypher, List<byte[]> keys) {
        this.cypher = cypher;
        this.keys = keys;
    }

    public byte[] decrypt(){
        for (int iterationNumber = 0; iterationNumber<3; iterationNumber++)
            iterate(2 - iterationNumber);
        return cypher;
    }
    private void iterate(int keyNumber){
        reverseAddRoundKey(keyNumber);
        reverseShiftRows();
    }

    private void reverseShiftRows(){
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < i; j++)
            {
                byte tmp = cypher[3+ i*4];
                for(int k = 3; k > 0; k--)
                    cypher[k+i*4] = cypher[(k-1)+i*4];
                cypher[i*4] = tmp;
            }
        }
    }

    private void reverseAddRoundKey(int keyNumber){
        for(int i = 0; i < 16; i++)
            cypher[i] ^= keys.get(keyNumber)[i];
    }
}
