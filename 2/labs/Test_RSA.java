import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


public class Test_RSA {
	
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
	
	public static void main(String[] args) throws Exception {
		RSALibrary r = new RSALibrary();
		r.generateKeys();
		
		/* Read  public key*/
		Path path = Paths.get("./public.key");
		byte[] bytes = Files.readAllBytes(path);

		//Public key is stored in x509 format
		X509EncodedKeySpec keyspec = new X509EncodedKeySpec(bytes);
		KeyFactory keyfactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyfactory.generatePublic(keyspec);
		
		/* Read private key */
		path = Paths.get("./private.key");
		byte[] bytes2 = Files.readAllBytes(path);

		//Private key is stored in PKCS8 format
		PKCS8EncodedKeySpec keyspec2 = new PKCS8EncodedKeySpec(bytes2);
		KeyFactory keyfactory2 = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyfactory2.generatePrivate(keyspec2);

		/*    MADE BY US    */

		// Creating an object of File class and
        // providing local directory path of a file
        File plaintext = new File(
                "./test.txt");

        // Calling the Method1 in main() to
        // convert file to byte array
        byte[] plaintext_bytes = method(plaintext);
		System.out.println("TEXTO PLAIN: " + Arrays.toString(plaintext_bytes));

		byte [] ciphertext = r.encrypt(plaintext_bytes, publicKey);

		System.out.println("TEXTO CIFRADO: " + Arrays.toString(ciphertext));

		byte [] plaintext_again = r.decrypt(ciphertext, privateKey);

		System.out.println("TEXTO DESCIFRADO: " + Arrays.toString(plaintext_again));

		System.out.println("ARRAYS: " + Arrays.equals(plaintext_again, plaintext_again));
	}
	
}
