package client;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3S6XrTs46QYCku1hyZRo4yANDEPi7Ql3Rn+Hq95gjopxKYFxqeT9oip6/9YxmVJzoIrafC+Sbowp+QIv/vw54TFlseAK+xMlmR2rAeH04lqpiiUYi/WAOyhbqABimYAFKCOiw6W0UTqFxpacIo2n1/W3GZyhz7TIro4klNvMW0wIDAQAB";
    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALdLpetOzjpBgKS7WHJlGjjIA0MQ+LtCXdGf4er3mCOinEpgXGp5P2iKnr/1jGZUnOgitp8L5JujCn5Ai/+/DnhMWWx4Ar7EyWZHasB4fTiWqmKJRiL9YA7KFuoAGKZgAUoI6LDpbRROoXGlpwijafX9bcZnKHPtMiujiSU28xbTAgMBAAECgYBOROZPybH0XF4jecB18Mg2sOn7rHaZ9+f6CGy/GgUESn60n9s2uBmkeXT6gU73r2/lDFWxKrHu57smWEev6JSzfS74YfqAOesB1gjO9R5sMeGXatqN6D3QdUL66ECRHSo2wJygiMjwjasTNcf3ajCOVzpE3Uaixop08Ni/wGMPwQJBAPfpn9m9PbzNlNBFypRH78oEAMozm1NsCtYYcUs46UsJ71bWjJV0PZNBdG//G9XwEzxOgR57rzT0unuJNhJCvOECQQC9RmQwGyqUv0otyFlDolKhZrqfNgQqrtfEy/cW/2VIv6naTJqYeanf+oFG7ziJFnaMK1qe4PcO5QPwOowp0DYzAkA3RiuRzJO7SMrpNOZZ8UARdpsGekMjcm9oFMMN1n6NcIWggu2NVQF1l55yahq2tHRVedtoBIOn9vitGoanw6GBAkBSfp+kJ/MDKeyWz7+4LsN8/V1RjyV5xQLeD1f+egoXDxJkxwaKdlyH9pLp69HkThoVNcAlhz+nCkIRLjNw9gLpAkBWPr7fHs4G9s4VbabCkuFzrJY9onDQHgrmFEq/MBrRhbkf9wyi0VujwDC7WpiZ9I2mM+Yx1r4/slDonOTS7qOB";
	
	public static String encrypt(String msg) {
		try {
			return Base64.getEncoder().encodeToString(RSAUtil.encrypt(msg, publicKey));
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(String msg) {
		try {
			return RSAUtil.decrypt(msg, privateKey);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}