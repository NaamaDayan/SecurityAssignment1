import java.util.List;

/**
 * Created by Naama on 04/04/2019.
 */
public class Encryption {


    private byte[] message;
    private List<byte[]> keys;

    public Encryption(byte[] message, List<byte[]> keys) {
        this.message = message;
        this.keys = keys;
    }

    public byte[] encrypt(){
        for (int iterationNumber = 0; iterationNumber<3; iterationNumber++)
            iterate(iterationNumber);
        return message;
    }

    private void iterate(int iterationNumber){
        shiftRows();
        addRoundKey(iterationNumber);

    }

    private void addRoundKey(int keyNumber)
    {
        for(int i = 0; i < 16; i++)
            message[i] ^= keys.get(keyNumber)[i];
    }


    private void shiftRows() {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < i; j++)
            {
                byte tmp = message[i*4];
                for(int k = 0; k < 3; k++)
                    message[k+i*4] = message[(k+1)+i*4];
                message[3+i*4] = tmp;
            }
    }


}
