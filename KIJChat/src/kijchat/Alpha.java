/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kijchat;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author dimas
 */
public class Alpha {
	
	// inisialisasi set karakter (alpha-numeric)
	public char[] CHARSET_azAZ09 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	
	public String randomString(int length) {
		// secure random dan simpan hasil sesuai parameter panjang
		Random random = new SecureRandom();
		char[] result = new char[length];
		
		// melakukan random karakter set yang telah didefinisikan
		for (int i = 0; i < result.length; i++) {
			// ambil index random dari parameter panjang
			// jadikan index tersebut sebagai karakter yang diambil
			int randomCharIdx = random.nextInt(CHARSET_azAZ09.length);
			result[i] = CHARSET_azAZ09[randomCharIdx];
		}
		
		return new String(result);
	}
	
	
}
