import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {

        Options options = new Options();

        Option encrypt = new Option("e", "encrypt", false, "encrypt");
        encrypt.setRequired(false);
        options.addOption(encrypt);

        Option decrypt = new Option("d", "calculate", false, "calculate");
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

        String inputFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        String keysPath = cmd.getOptionValue("keys");
        calculate(inputFilePath, outputFilePath, keysPath, cmd.hasOption("e"));

    }

    private static void calculate(String inputFilePath, String outputFilePath, String keysPath, boolean isEncrypt) throws IOException{
        Path inputLocation = Paths.get(inputFilePath);
        byte[] input = Files.readAllBytes(inputLocation);
        List<byte[]> data = splitData(input); //split into arrays of length 16

        Path keysLocation = Paths.get(keysPath);
        byte[] key = Files.readAllBytes(keysLocation);

        Path outputFile = Paths.get(outputFilePath);

        for (byte[] c : data){
            byte[] result = isEncrypt ? toEncrypt(c,key) : toDecrypt(c,key);
            Files.write(outputFile, result);
        }
    }


    private static byte[] toEncrypt(byte[] message, byte[] key){
        Encryption enc = new Encryption(message, key);
        return enc.encrypt();
    }

    private static byte[] toDecrypt(byte[] cypher, byte[] key){
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



}