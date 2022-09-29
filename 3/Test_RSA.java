import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test_RSA {

	public static byte[] rdmPassword() {
		return "holaholaholahola".getBytes();
	}

	public static byte[] readPassphrase() {

		// Read Passwd from the terminal
		Scanner scanner = new Scanner(System.in);
		System.out.println("Give me the passphrase: ");
		String passphrase = scanner.nextLine();
		System.out.println("Your string: " + passphrase);
		scanner.close();
		byte[] passphrase_bytes = passphrase.getBytes();

		// Checkear la longitud y acabar si no es de 16 caracteres.
		if (passphrase_bytes.length != 16) {
			System.out.println("Key length is invalid, please use 16 bytes length keys");
			System.exit(-1);
		}

		return passphrase_bytes;
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

	public static void main(String[] args) throws Exception {

		RSALibrary r = new RSALibrary();
		String sourceFile = "";
		String destinationFile = "";
		byte[] sessionKey;
		byte[] passphrase = null;

		// Parse the arguments
		if (args[0].equals("g")) {
			passphrase = readPassphrase();
			r.generateKeys(passphrase);
			System.out.println("The keys have been generated correctly, check your directories");
			System.exit(0);
		} else if (args[0].equals("e")) {
			sourceFile = args[1];
			destinationFile = args[2];
			// We have to decrypt it before reading
			SymmetricCipher sCipher = new SymmetricCipher();
			// We read the user password, needed to decrypt.
			passphrase = readPassphrase();
			// We randomly generate the session key.
			sessionKey = rdmPassword();

			/* Read public key */
			Path path = Paths.get("./public.key");
			byte[] bytes = Files.readAllBytes(path);
			// Public key is stored in x509 format
			X509EncodedKeySpec keyspec = new X509EncodedKeySpec(bytes);
			KeyFactory keyfactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyfactory.generatePublic(keyspec);

			/* Read private key THAT IS CYPHER WITH OUR PASSPHRASE */
			path = Paths.get("./private.key");
			byte[] private_key = Files.readAllBytes(path);
			byte[] decrypted_private_key = sCipher.decryptCBC(private_key, passphrase);
			// Private key is stored in PKCS8 format
			PKCS8EncodedKeySpec keyspec2 = new PKCS8EncodedKeySpec(decrypted_private_key);
			KeyFactory keyfactory2 = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyfactory2.generatePrivate(keyspec2);
			

			// We read the text that we want to cypher.
			File plaintext = new File(sourceFile);
			// We convert the string text into bytes.
			byte[] plaintext_bytes = method(plaintext);
			// We encrypt the text with our session key, randomly generated.
			byte[] ciphertext_bytes = sCipher.encryptCBC(plaintext_bytes, sessionKey);
			// Once the session key is used, we encrypt it with our public key.
			byte[] cipher_sessionKey = r.encrypt(sessionKey, publicKey);
			System.out.println("Tamaño de la session key encriptada: " + cipher_sessionKey.length);

			byte[] cyphertext_plus_encryptedskey = SymmetricCipher.joinByteArray(ciphertext_bytes, cipher_sessionKey);

			byte[] signed = r.sign(cyphertext_plus_encryptedskey, privateKey);
			System.out.println("Tamaño del hash: " + signed.length);

			byte[] message = SymmetricCipher.joinByteArray(cyphertext_plus_encryptedskey, signed);

			try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
				fos.write(message);
			}

		} else if (args[0].equals("d")) {


		} else {
			System.out.println("The arguments provided are invalid.");
			System.out.println("java < g | e |d > [sourceFile] [destinationFile]");
			System.exit(-1);
		}

		//System.out.println("TEXTO PLAIN: " + new String(plaintext_bytes));

		//byte[] ciphertext = r.encrypt(plaintext_bytes, publicKey);

		//System.out.println("Tras el cifrado: " + new String(ciphertext));

		//byte[] plaintext_again = r.decrypt(ciphertext, privateKey);

		//System.out.println("Tras el descifrado: " + new String(plaintext_again));

		//System.out.println("Son iguales los arrays: " + Arrays.equals(plaintext_again, plaintext_again));
	}

}
