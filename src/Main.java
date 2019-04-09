import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {

        Options options = new Options();

        Option encrypt = new Option("e", "encrypt", false, "encrypt");
        encrypt.setRequired(false);
        options.addOption(encrypt);

        Option decrypt = new Option("d", "encOrDec", false, "encOrDec");
        decrypt.setRequired(false);
        options.addOption(decrypt);

        Option keys = new Option("k", "keys", true, "path to keys");
        keys.setRequired(true);
        options.addOption(keys);

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        Option break_ = new Option("b", "break", false, "break aes");
        break_.setRequired(false);
        options.addOption(break_);

        Option message = new Option("m", "message", true, "message");
        message.setRequired(false);
        options.addOption(message);

        Option cipher = new Option("c", "cipher", true, "cipher");
        cipher.setRequired(false);
        options.addOption(cipher);


        CommandLineParser parser = new GnuParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String outputFilePath = cmd.getOptionValue("output");

        if (!cmd.hasOption("b")) {
            String inputFilePath = cmd.getOptionValue("input");
            String keysPath = cmd.getOptionValue("keys");
            encOrDec(inputFilePath, outputFilePath, keysPath, cmd.hasOption("e"));
        }
        else{
            String messagePath = cmd.getOptionValue("message");
            String cipherPath = cmd.getOptionValue("cipher");
            breakAes(messagePath, cipherPath, outputFilePath);
        }


    }

    private static void breakAes(String messagePath, String cipherPath, String outputFilePath) throws IOException {
       byte [] key1 = generateKey();
       byte [] key2 = generateKey();
       List<byte[]> message = getBytes(messagePath);
    }

    private static void encOrDec(String inputFilePath, String outputFilePath, String keysPath, boolean isEncrypt) throws IOException{

        List<byte[]> data = getBytes(inputFilePath);
        List<byte[]> key = getBytes(keysPath);

        Path outputFile = Paths.get(outputFilePath);

        List<byte[]> res = new LinkedList<>();
        for (byte[] c : data){
            byte[] result = isEncrypt ? toEncrypt(c,key) : toDecrypt(c,key);
            res.add(result);
        }
        Files.write(outputFile, toByteArray(transposeAllList(res)));
    }

    private static List<byte[]> getBytes(String path) throws IOException {
        Path pathLocation = Paths.get(path);
        return  transposeAllList(splitData(Files.readAllBytes(pathLocation)));
    }


    private static byte[] toEncrypt(byte[] message, List<byte[]> key){
        Encryption enc = new Encryption(message, key);
        return enc.encrypt();
    }

    private static byte[] toDecrypt(byte[] cypher, List<byte[]> key){
        Decryption dec = new Decryption(cypher, key);
        return dec.decrypt();
    }


    //returns a list of 2D matrix of 4x4 bytes
    private static List<byte[]> splitData(byte[] data){
        int byteCounter = 0;
        List<byte[]> messages = new LinkedList<>();
        for (int i=0; i<data.length/16; i++){
            byte[] currentMessage = new byte[16];
            for (int j= 0; j<16; j++) {
                currentMessage[j] = data[byteCounter];
                    byteCounter++;
                }
            messages.add(currentMessage);
        }
        return messages;
    }

    private static byte[] transposeMatrix(byte [] m){
        byte[] temp = new byte [m.length];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                temp[j*4+i] = m[i*4+j];
        return temp;
    }

    private static List<byte[]> transposeAllList(List<byte[]> data){
        List<byte[]> transposed_data = new LinkedList<>();
        for (byte[] data_m : data){
            transposed_data.add(transposeMatrix(data_m));
        }
        return transposed_data;
    }

    private static byte[] toByteArray(List<byte[] > data){
        int counter = 0;
        byte[] toRet = new byte[16*data.size()];
        for (byte[] b  : data)
            for (int i=0; i<16; i++) {
                toRet[counter] = b[i];
                counter++;
            }
        return toRet;
    }

    private static byte[] generateKey(){
        byte[] key = new byte[16];
        Arrays.fill( key, (byte) 1 );
        return key;
    }





}