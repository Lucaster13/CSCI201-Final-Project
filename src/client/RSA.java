package client;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

// Code Originally By: dmydlarz (GitHub)
// https://gist.github.com/dmydlarz/32c58f537bb7e0ab9ebf
public class RSA {
	static KeyPair keyPair;
	static PublicKey pubKey;
	static PrivateKey privateKey;
	public static void generateKeys() throws Exception {
		// generate public and private keys
        keyPair = buildKeyPair();
        pubKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
	}
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
        return cipher.doFinal(encrypted);
    }
    
    public static PublicKey getPublicKey() {
    	return pubKey;
    }
    
    public static PrivateKey getPrivateKey() {
    	return privateKey;
    }
}