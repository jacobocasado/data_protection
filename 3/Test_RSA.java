import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		SymmetricCipher sCipher = new SymmetricCipher();
		String sourceFile = "";
		String destinationFile = "";
		byte[] sessionKey = null;
		byte[] passphrase = null;
		PrivateKey privateKey = null;
		PublicKey publicKey = null;

		// Check if args0 == d or e
		if (args[0].equals("e") || args[0].equals("d")) {
			try {
				sourceFile = args[1];
				destinationFile = args[2];
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println("In 'e' and 'd' modes you need to provide both source and destination files.");
				System.out.println("Usage: java -jar SecureSec.jar d|e -input -output");

				System.exit(-1);
			}

			// We read the user password, needed to decrypt.
			passphrase = readPassphrase();

			/* Read public key */
			Path path = null;
			byte[] bytes = null;
			try {
				path = Paths.get("./public.key");
				bytes = Files.readAllBytes(path);
			} catch (NullPointerException | IOException ex) {
				System.out.println("It is not possible to acces the ./public.key file. \n Try again.");
				System.exit(-1);
			}

			// Public key is stored in x509 format
			try {
				X509EncodedKeySpec keyspec = new X509EncodedKeySpec(bytes);
				KeyFactory keyfactory = KeyFactory.getInstance("RSA");
				publicKey = keyfactory.generatePublic(keyspec);
			} catch (NullPointerException | InvalidKeySpecException ex) {
				System.out.println("Wrong public key format");
				System.exit(-1);
			}

			/* Read private key THAT IS CYPHER WITH OUR PASSPHRASE */
			byte[] private_key = null;
			try {
				path = Paths.get("./private.key");
				private_key = Files.readAllBytes(path);
			} catch (NullPointerException | IOException ex) {
				System.out.println("./private.key file not found. \n Try again generating keys.");
				System.out.println("Run: java -jar SecureSec.jar and ensure keys are created.");
				System.exit(-1);
			}

			try {
				byte[] decrypted_private_key = sCipher.decryptCBC(private_key, passphrase);
				// Private key is stored in PKCS8 format
				PKCS8EncodedKeySpec keyspec2 = new PKCS8EncodedKeySpec(decrypted_private_key);
				KeyFactory keyfactory2 = KeyFactory.getInstance("RSA");
				privateKey = keyfactory2.generatePrivate(keyspec2);
			} catch (InvalidKeySpecException ex) {
				System.out.println("Error while decrypting private key. The passphrase is not right OR key might be corrupted.");
				System.exit(-1);
			} catch (Exception ex) {
				System.out.println("Error while decripting the file");
				System.exit(-1);
			}
		}
		// Parse the arguments
		if (args[0].equals("g")) {
			passphrase = readPassphrase();
			r.generateKeys(passphrase);
			System.out.println("The keys have been generated correctly, check your directories");
			System.exit(0);
		} else if (args[0].equals("e")) {
			// We randomly generate the session key.
			sessionKey = rdmPassword();

			byte[] plaintext_bytes = new byte[0];
			// We read the text that we want to cypher.
			File plaintext = new File(sourceFile);
			// We convert the string text into bytes.
			try {
				plaintext_bytes = method(plaintext);
			} catch (IOException e) {
				System.out.println("File specified to encrypt (" + sourceFile + ") not found.");
				System.exit(-1);
			}
			
			// We encrypt the text with our session key, randomly generated.
			byte[] ciphertext_bytes = sCipher.encryptCBC(plaintext_bytes, sessionKey);
			// Once the session key is used, we encrypt it with our public key.
			byte[] cipher_sessionKey = r.encrypt(sessionKey, publicKey);
			// Size of the encrypted session key: 128 bits or 16 characters.
			System.out.println("Tamaño de la session key encriptada: " + cipher_sessionKey.length);

			// We append the encrypted session key to the cyphertext.
			byte[] cyphertext_plus_encryptedskey = SymmetricCipher.joinByteArray(ciphertext_bytes, cipher_sessionKey);

			// We sign the cyphertext plus the encrypted session key with our private key
			byte[] signed = r.sign(cyphertext_plus_encryptedskey, privateKey);
			// The hash size should be 20 bytes or 160 bits.
			System.out.println("Tamaño del hash: " + signed.length);

			byte[] message = SymmetricCipher.joinByteArray(cyphertext_plus_encryptedskey, signed);

			try {
				try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
					fos.write(message);
				}
			} catch(FileNotFoundException f){
				System.out.println("Problem at destination file. Exiting.");
				System.exit(-1);
			}

			System.out.println("Final file created as " + destinationFile + ". Exiting.");
			System.exit(0);

		} else if (args[0].equals("d")) {

			// We read the text that we want to descypher.
			File cyphertext_plus_things = new File(sourceFile);
			// We convert the string text with all the things into bytes.
			byte[] byte_cyphertext_plus_things = new byte[0];

			try {
				byte_cyphertext_plus_things = method(cyphertext_plus_things);
			} catch (IOException e) {
				System.out.println("File specified to decrypt (" + sourceFile + ") not found.");
				System.exit(0);
			}

			// We split the message into the necessay varibles
			byte[] sign = Arrays.copyOfRange(byte_cyphertext_plus_things, byte_cyphertext_plus_things.length - 128,
					byte_cyphertext_plus_things.length);
			byte[] ciphertext_plus_key = Arrays.copyOfRange(byte_cyphertext_plus_things,
					0, byte_cyphertext_plus_things.length - 128);
			byte[] encripted_sessionKey = Arrays.copyOfRange(byte_cyphertext_plus_things,
					byte_cyphertext_plus_things.length - 256, byte_cyphertext_plus_things.length - 128);
			byte[] ciphertext = Arrays.copyOfRange(byte_cyphertext_plus_things, 0,
					byte_cyphertext_plus_things.length - 256);
			// We need to verify the sign. TODO if it's false, we don't let the user
			// continue.
			Boolean good_sign = r.verify(ciphertext_plus_key, sign, publicKey);

			if (!good_sign) {
				System.out.println("Bad signature. Private and public key does not match.");
				System.exit(-1);
			}

			// We decipher the session key
			byte[] decripted_sessionKey = r.decrypt(encripted_sessionKey, privateKey);

			// We decipher the session key
			byte[] decripted_plainText = sCipher.decryptCBC(ciphertext, decripted_sessionKey);

			try {
				try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
					fos.write(decripted_plainText);
				}
			} catch(FileNotFoundException f){
				System.out.println("Problem at destination file. Exiting.");
				System.exit(-1);
			}

			System.out.println("Decrypted file created as " + destinationFile + ". Exiting.");
			System.exit(0);

		} else {
			System.out.println("The arguments provided are invalid.");
			System.out.println("Usage: java <g|e|d> [sourceFile] [destinationFile]");
			System.exit(-1);
		}
	}
}
