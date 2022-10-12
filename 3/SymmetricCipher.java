
// Autores:
/**
 * Jacobo Casado de Gracia  
 * Angel Casanova Bienzobas
 */

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import java.security.InvalidKeyException;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;


public class SymmetricCipher {

    SymmetricEncryption s;
    SymmetricEncryption d;

    private static final int AES_BLOCK_SIZE = 16;

    // Initialization Vector (fixed)

    byte[] iv = new byte[]{(byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54,
            (byte) 55, (byte) 56, (byte) 57, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52,
            (byte) 53, (byte) 54};

    /*************************************************************************************/
    /* Constructor method */

    /*************************************************************************************/
    public SymmetricCipher() {
    }

    /*************************************************************************************/
    /* Method to encrypt using AES/CBC/PKCS5 */

    /*************************************************************************************/
    public byte[] encryptCBC(byte[] input, byte[] byteKey) throws Exception {

        s = new SymmetricEncryption(byteKey);

        byte padding = 0;

        if (input.length % AES_BLOCK_SIZE != 0) {
            padding += (AES_BLOCK_SIZE - input.length % AES_BLOCK_SIZE);
        } else {
            padding += AES_BLOCK_SIZE;
        }

        // System.out.println("Padding: " + padding);

        byte[] just_padding = new byte[(int) padding];

        for (int i = 0; i < padding; i++) {
            // System.out.println("Inserto padding, " + i + "º byte.");
            just_padding[i] = padding;
        }

        // System.out.println(Arrays.toString(just_padding) + just_padding.length);

        byte[] plain_text = joinByteArray(input, just_padding);
        // System.out.println(Arrays.toString(plain_text));

        // Start CBC cipher
        int numberOfBlocks = plain_text.length / AES_BLOCK_SIZE; // Symbol / symbol per block
        // System.out.println("Number of blocks: " + numberOfBlocks);
        byte[] cypherText = new byte[plain_text.length];           // All the cypher text
        byte[] currentBlock = new byte[(int) AES_BLOCK_SIZE];    // Current chuck of plain test (16B per iteration)
        byte[] afterBlock = new byte[(int) AES_BLOCK_SIZE];      // Text just after apply AES
        byte[] beforeBlock;                                        // Text just before apply AES

        for (int i = 0, j = 0; j < numberOfBlocks; i += AES_BLOCK_SIZE, j++) {
            
            System.arraycopy(plain_text, i, currentBlock, 0, AES_BLOCK_SIZE); // Split in chunks
            // System.out.println(Arrays.toString(currentBlock));
            // At each iteration, we have a block of 16 bytes
            if (i == 0) {  // First time use iv
                beforeBlock = byteArrayXor(new SymmetricCipher().iv, currentBlock, AES_BLOCK_SIZE);
            } else {
                beforeBlock = byteArrayXor(afterBlock, currentBlock, AES_BLOCK_SIZE);
            }

            // Apply AES algorithm
            afterBlock = s.encryptBlock(beforeBlock);
            // Copy the cipher text in the final buffer
            System.arraycopy(afterBlock, 0, cypherText, i, AES_BLOCK_SIZE); // Split in chunks
        }

        return cypherText;
    }

    /*************************************************************************************/
    /* Method to decrypt using AES/CBC/PKCS5 */

    /*************************************************************************************/


    public byte[] decryptCBC(byte[] input, byte[] byteKey) throws Exception {
        byte[] finalplaintext = new byte[input.length];

        d = new SymmetricEncryption(byteKey);
        // Start CBC cipher
        int numberOfBlocks = input.length / AES_BLOCK_SIZE;      // Symbol / symbol per block
        // System.out.println("Number of blocks: " + numberOfBlocks);
        byte[] previousBlock = new byte[(int) AES_BLOCK_SIZE];   // Current chuck of plain test (16B per iteration)
        byte[] afterBlock;                                         // Text just after apply AES
        byte[] xoredBlock;                                         // Text just before apply AES

        for (int i = 0, j = 0; j < numberOfBlocks; i += AES_BLOCK_SIZE, j++) {
            byte[] currentBlock = new byte[(int) AES_BLOCK_SIZE];    // Current chuck of plain test (16B per iteration)
            System.arraycopy(input, i, currentBlock, 0, AES_BLOCK_SIZE); // Split in chunks
            // System.out.println(Arrays.toString(currentBlock));

            // Apply AES algorithm
            afterBlock = d.decryptBlock(currentBlock);
            // System.out.println(Arrays.toString(afterBlock));

            if (i == 0) {  // First time use iv
                xoredBlock = byteArrayXor(new SymmetricCipher().iv, afterBlock, AES_BLOCK_SIZE);
            } else {
                xoredBlock = byteArrayXor(previousBlock, afterBlock, AES_BLOCK_SIZE);
            }
            // Keep the current chunk of cypher code to use it in the next iteration
            System.arraycopy(currentBlock, 0, previousBlock, 0, AES_BLOCK_SIZE);

            // Copy the cipher text in the final buffer
            System.arraycopy(xoredBlock, 0, finalplaintext, i, AES_BLOCK_SIZE); // Split in chunks
        }

        // Remove the padding
        int padding = finalplaintext[finalplaintext.length-1];
        int finalLength = finalplaintext.length - padding;
        byte[] finalPlainTextNoPadding = new byte[finalLength];
        System.arraycopy(finalplaintext, 0, finalPlainTextNoPadding, 0, finalLength);
        return finalPlainTextNoPadding;
    }

    public static byte[] method(File file)
            throws IOException {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int) file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }

    public static byte[] joinByteArray(byte[] byte1, byte[] byte2) {

        byte[] result = new byte[byte1.length + byte2.length];

        System.arraycopy(byte1, 0, result, 0, byte1.length);
        System.arraycopy(byte2, 0, result, byte1.length, byte2.length);

        return result;

    }

    public static byte[] byteArrayXor(final byte[] s1, final byte[] s2, final int length) {
        byte[] ret = new byte[length];

        int i = 0;
        for (byte b : s1)
            ret[i] = (byte) (b ^ s2[i++]);
        return ret;
    }


    public static void main(String[] args)
            throws Exception {

        // Random Key Generation
         byte[] key = new byte [AES_BLOCK_SIZE];
        for (int i = 0; i < AES_BLOCK_SIZE; i++){
             key[i] = (byte) (Math.random() * 128);
         }

        // Fixed key generation
        String key2_string = "1234567890123456";
        byte[] key2 = key2_string.getBytes(); 

        // Fixed key generation (with byte vector)
        byte[] key3 = new byte[]{(byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54,
            (byte) 55, (byte) 56, (byte) 57, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52,
            (byte) 53, (byte) 54};

        SymmetricCipher sCipher = new SymmetricCipher();

        // Creating an object of File class and
        // providing local directory path of a file
        File path = new File(
                "./text.txt");

        // Calling the Method1 in main() to
        // convert file to byte array
        byte[] plaintext = method(path);

        byte[] cyphertext = sCipher.encryptCBC(plaintext, key3);
    
        // System.out.println("Texto cifrado: " + Arrays.toString(cyphertext));

        String cyphertext_string = new String(cyphertext);

        // System.out.println("Texto cifrado: " + cyphertext_string);

        try(FileOutputStream fos = new FileOutputStream("./test_enc_but_mine.txt")){
            fos.write(cyphertext);
        }

        byte[] plaintext_again = sCipher.decryptCBC(cyphertext, key3);

        // try (PrintWriter out = new PrintWriter("test_enc_but_mine.txt")) {
        //     out.print(cyphertext_string);
        // }

        String plaintext_again_string = new String(plaintext_again);

        // System.out.println("Texto descrifrado: " + plaintext_again_string);

    }   

}

