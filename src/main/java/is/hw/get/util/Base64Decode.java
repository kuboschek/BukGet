package is.hw.get.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Decode {
	public static String decodeString(String b64) {
		return new String(Base64.decodeBase64(b64));
	}
}
