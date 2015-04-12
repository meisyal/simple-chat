package id.ac.its.kij.simplechat;

public class RC4KeyGenerator {

	private byte[] S = new byte[256];
	private static int index_1;
	private static int index_2;
	
	// Constructor byte-key
	public RC4KeyGenerator(String key) {
		this(key.getBytes());
	}

	public RC4KeyGenerator(byte[] key) {
		// key schedulling algorithm
		for (int i = 0; i < 256; i++) {
			S[i] = (byte) i;
		}
		
		index_1 = 0;
		index_2 = 0;
		
		byte temp;
		
		if (key == null || key.length == 0){
			throw new NullPointerException();
		}
		
		for (int i = 0; i < 256; i++) {
			index_2 = ((key[index_1] & 0xff) + (S[i] & 0xff) + index_2) & 0xff;
			
			swap(S, i, index_2);
			
			index_1 = (index_1 + 1) % key.length;
		}
	}

//	key schedulling agorithm
//	public void ksa(int n, byte[] key, boolean status) {
//		int keylength = key.length;
//		
//		for (int i = 0; i <= 255; i++)
//			S[i] = (byte) i;
//		
//	}
	
	// swapping function
	public static void swap(byte[] S, int i, int j) {
		byte temp = S[i];
		S[i] = S[j];
		S[j] = temp;
	}
	
}
