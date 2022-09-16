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
	
	byte[] iv = new byte[] { (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, 
		(byte)55, (byte)56, (byte)57, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52,
		(byte)53, (byte)54};

    /*************************************************************************************/
	/* Constructor method */
    /*************************************************************************************/
	public void SymmetricCipher() {
	}

    /*************************************************************************************/
	/* Method to encrypt using AES/CBC/PKCS5 */
    /*************************************************************************************/
	public byte[] encryptCBC (byte[] input, byte[] byteKey) throws Exception {
		
		byte[] ciphertext = null;	
		
		
			// Generate the plaintext with padding
			
			
	
			// Generate the ciphertext
			
		
		
		return ciphertext;
	}
	
	/*************************************************************************************/
	/* Method to decrypt using AES/CBC/PKCS5 */
    /*************************************************************************************/
	
	
	public byte[] decryptCBC (byte[] input, byte[] byteKey) throws Exception {
	
		
		byte [] finalplaintext = null;
		
			
		// Generate the plaintext
			
		
		// Eliminate the padding
		
			
		
		return finalplaintext;
	}

	public static byte[] method(File file)
        throws IOException
    {

		// Bloques de 16 caracteres.
		// 
  
        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);
  
        // Now creating byte array of same length as file
        byte[] arr = new byte[(int)file.length()];
  
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

	public static void splitByteArray(byte[] input) {

		byte[] cipher = new byte[8];
		byte[] nonce = new byte[4];
		byte[] extra = new byte[2];
		System.arraycopy(input, 0, cipher, 0, cipher.length);
		System.arraycopy(input, cipher.length, nonce, 0, nonce.length);
		System.arraycopy(input, cipher.length + nonce.length, extra, 0, extra.length);
	
	}
	

	public static void main(String[] args)
	throws IOException {

		// Creating an object of File class and
        // providing local directory path of a file
        File path = new File(
            "./text.txt");
  
        // Calling the Method1 in main() to
        // convert file to byte array
        byte[] array = method(path);
		
		// TODO only add extra block of padding when module is 0
		// if module is not 0, add n bytes of n value 
        // Printing the byte array
        System.out.print(Arrays.toString(array) + "\n");

		byte padding = 0;

		if (array.length % 16 != 0){
			padding += (16 - array.length % 16);
		}
		else{
			padding += 16;
		}

		System.out.println("Padding: " + padding);

		byte[] just_padding = new byte[(int)padding];

		for (int i = 0; i < padding; i++){
			System.out.println("Inserto padding, " + i + "º byte.");
			just_padding[i] = (byte)padding;
		}
		
		System.out.println(Arrays.toString(just_padding) + just_padding.length);

		byte[] all = joinByteArray(array, just_padding);

		System.out.println(Arrays.toString(all));
		
    }
	
}

