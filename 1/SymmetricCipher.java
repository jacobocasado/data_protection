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

    byte[] byteKey;
    SymmetricEncryption s;
    SymmetricEncryption d;

    // Initialization Vector (fixed)

    byte[] iv = new byte[]{(byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54,
            (byte) 55, (byte) 56, (byte) 57, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52,
            (byte) 53, (byte) 54};

    /*************************************************************************************/
    /* Constructor method */

    /*************************************************************************************/
    public void SymmetricCipher() {
    }

    /*************************************************************************************/
    /* Method to encrypt using AES/CBC/PKCS5 */

    /*************************************************************************************/
    public byte[] encryptCBC(byte[] input, byte[] byteKey) throws Exception {

        byte padding = 0;

        if (input.length % d.AES_BLOCK_SIZE != 0) {
            padding += (d.AES_BLOCK_SIZE - input.length % d.AES_BLOCK_SIZE);
        } else {
            padding += d.AES_BLOCK_SIZE;
        }

        System.out.println("Padding: " + padding);

        byte[] just_padding = new byte[(int) padding];

        for (int i = 0; i < padding; i++) {
            System.out.println("Inserto padding, " + i + "º byte.");
            just_padding[i] = padding;
        }

        System.out.println(Arrays.toString(just_padding) + just_padding.length);

        byte[] plain_text = joinByteArray(input, just_padding);
        System.out.println(Arrays.toString(plain_text));

        // Start CBC cipher
        int numberOfBlocks = plain_text.length / d.AES_BLOCK_SIZE; // Symbol / symbol per block
        System.out.println("Number of blocks: " + numberOfBlocks);
        byte[] cypherText = new byte[plain_text.length];           // All the cypher text
        byte[] currentBlock = new byte[(int) d.AES_BLOCK_SIZE];    // Current chuck of plain test (16B per iteration)
        byte[] afterBlock = new byte[(int) d.AES_BLOCK_SIZE];      // Text just after apply AES
        byte[] beforeBlock;                                        // Text just before apply AES

        for (int i = 0, j = 0; j < numberOfBlocks; i += 16, j++) {
            System.arraycopy(plain_text, i, currentBlock, 0, d.AES_BLOCK_SIZE); // Split in chunks
            System.out.println(Arrays.toString(currentBlock));
            // At each iteration, we have a block of 16 bytes
            if (i == 0) {  // First time use iv
                beforeBlock = byteArrayXor(new SymmetricCipher().iv, currentBlock, 16);
            } else {
                beforeBlock = byteArrayXor(afterBlock, currentBlock, 16);
            }

            // Apply AES algorithm
            afterBlock = s.encryptBlock(beforeBlock);
            // Copy the cipher text in the final buffer
            System.arraycopy(afterBlock, 0, cypherText, i, d.AES_BLOCK_SIZE); // Split in chunks
        }

        return cypherText;
    }

    /*************************************************************************************/
    /* Method to decrypt using AES/CBC/PKCS5 */

    /*************************************************************************************/


    public byte[] decryptCBC(byte[] input, byte[] byteKey) throws Exception {
        byte[] finalplaintext = new byte[input.length];


        // Start CBC cipher
        int numberOfBlocks = input.length / d.AES_BLOCK_SIZE;      // Symbol / symbol per block
        System.out.println("Number of blocks: " + numberOfBlocks);
        byte[] currentBlock = new byte[(int) d.AES_BLOCK_SIZE];    // Current chuck of plain test (16B per iteration)
        byte[] previousBlock = new byte[(int) d.AES_BLOCK_SIZE];   // Current chuck of plain test (16B per iteration)
        byte[] afterBlock;                                         // Text just after apply AES
        byte[] xoredBlock;                                         // Text just before apply AES

        for (int i = 0, j = 0; j < numberOfBlocks; i += 16, j++) {
            System.arraycopy(input, i, currentBlock, 0, d.AES_BLOCK_SIZE); // Split in chunks
            System.out.println(Arrays.toString(currentBlock));

            // Apply AES algorithm
            afterBlock = s.decryptBlock(currentBlock);
            System.out.println(Arrays.toString(afterBlock));

            if (i == 0) {  // First time use iv
                xoredBlock = byteArrayXor(new SymmetricCipher().iv, afterBlock, 16);
            } else {
                xoredBlock = byteArrayXor(previousBlock, afterBlock, 16);
            }
            // Keep the current chunk of cypher code to use it in the next iteration
            System.arraycopy(currentBlock, 0, previousBlock, 0, d.AES_BLOCK_SIZE);

            // Copy the cipher text in the final buffer
            System.arraycopy(xoredBlock, 0, finalplaintext, i, d.AES_BLOCK_SIZE); // Split in chunks
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

        // Bloques de 16 caracteres.
        //

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

    //	public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    public static void splitByteArray(byte[] input) {

        byte[] cipher = new byte[8];
        byte[] nonce = new byte[4];
        byte[] extra = new byte[2];
        System.arraycopy(input, 0, cipher, 0, cipher.length);
        System.arraycopy(input, cipher.length, nonce, 0, nonce.length);
        System.arraycopy(input, cipher.length + nonce.length, extra, 0, extra.length);

    }

    public static byte[] byteArrayXor(final byte[] s1, final byte[] s2, final int length) {
        byte[] ret = new byte[length];

        int i = 0;
        for (byte b : s1)
            ret[i] = (byte) (b ^ s2[i++]);
        return ret;
    }


    public static void main(String[] args)
            throws IOException {

        // Creating an object of File class and
        // providing local directory path of a file
        File path = new File(
                "/home/skiz0/Desktop/data_protection/1/text.txt");

        // Calling the Method1 in main() to
        // convert file to byte array
        byte[] array = method(path);

        // TODO only add extra block of padding when module is 0
        // if module is not 0, add n bytes of n value
        // Printing the byte array
        System.out.print(Arrays.toString(array) + "\n");

        byte padding = 0;

        if (array.length % 16 != 0) {
            padding += (16 - array.length % 16);
        } else {
            padding += 16;
        }

        System.out.println("Padding: " + padding);

        byte[] just_padding = new byte[(int) padding];

        for (int i = 0; i < padding; i++) {
            System.out.println("Inserto padding, " + i + "º byte.");
            just_padding[i] = (byte) padding;
        }

        System.out.println(Arrays.toString(just_padding) + just_padding.length);

        byte[] plain_text = joinByteArray(array, just_padding);
        System.out.println(Arrays.toString(plain_text));

        // Start CBC cipher
        int numberOfBlocks = plain_text.length / 16; // Symbol / symbol per block
        System.out.println("Number of blocks: " + numberOfBlocks);
        byte[] cypher_text = new byte[plain_text.length]; // All the cypher text
        byte[] current_block = new byte[(int) 16];        // Current chuck of plain test (16B per iteration)
        byte[] after_block = new byte[(int) 16];          // Text just after apply AES
        byte[] before_block;                              // Text just before apply AES
//FYI:		public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        for (int i = 0, j = 0; j < numberOfBlocks; i += 16, j++) {
            System.arraycopy(plain_text, i, current_block, 0, 16); // Split in chunks
            System.out.println(Arrays.toString(current_block));
            // At each iteration, we have a block of 16 bytes
            if (i == 0) {  // First time use iv
                before_block = byteArrayXor(new SymmetricCipher().iv, current_block, 16);
            } else {
                before_block = byteArrayXor(after_block, current_block, 16);
            }

            // Apply AES algorithm
            after_block = before_block;// TODO: AES HERE, llamar a encryptBlock(before_block)
            // Copy the cipher text in the final buffer
            System.arraycopy(after_block, 0, cypher_text, i, 16); // Split in chunks
        }
        System.out.println(Arrays.toString(cypher_text));


    }

}

