package id.ac.its.kij.simplechat;

import java.security.SecureRandom;
import java.util.Random;

public class RandomAlphaNumeric {
	
	// inisialisasi set karakter (alpha-numeric)
	private static final char[] CHARSET_azAZ09 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	
	public static String randomString(char[] characterSet, int length) {
		// secure random dan simpan hasil sesuai parameter panjang
		Random random = new SecureRandom();
		char[] result = new char[length];
		
		// melakukan random karakter set yang telah didefinisikan
		for (int i = 0; i < result.length; i++) {
			// ambil index random dari parameter panjang
			// jadikan index tersebut sebagai karakter yang diambil
			int randomCharIdx = random.nextInt(characterSet.length);
			result[i] = characterSet[randomCharIdx];
		}
		
		return new String(result);
	}
	
	public static void main(String[] args) {
		
		// uji hasil dengan jumlah 10 karakter
		for (int i = 0; i < 10; i++) {
			System.out.println(randomString(CHARSET_azAZ09, 10));
		}
		
	}
	
}
