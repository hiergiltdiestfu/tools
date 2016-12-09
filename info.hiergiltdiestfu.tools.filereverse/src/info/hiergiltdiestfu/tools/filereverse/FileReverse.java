package info.hiergiltdiestfu.tools.filereverse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Quick tool I made for geocachers that encounter ie. image
 * files that are obviously reversed on the byte level.
 * 
 * Quick job, no bells, whistles or grease, but it works, so yeah.
 * 
 * @author niob
 *
 */
public class FileReverse {

	public static void main(String[] args) throws IOException {
		
		File in = new File("in.file"),
			 out = new File("out.file");
		
		if (!in.exists()) {
			System.err.println("Expecting input at 'in.file'! (Output will be at 'out.file'.)");
			System.exit(100);
		}
		
		out.createNewFile();
		
		FileInputStream inStream = new FileInputStream(in);
		final LinkedList<byte[]> chunks = new LinkedList<>();
		int bytesRead = 0;
		int total = 0;
		byte[] buffer; 
		while (bytesRead != -1) {
			buffer= new byte[64*1024];
			bytesRead = inStream.read(buffer);
			
			if (bytesRead > 0) {
				total += bytesRead;
				if (bytesRead < buffer.length) {
					buffer = Arrays.copyOf(buffer, bytesRead);
				}
				
				chunks.add(buffer);
			}
		}
		inStream.close();

		final LinkedList<byte[]> reversedChunks = reverse(chunks);
		
		FileOutputStream outStream = new FileOutputStream(out);
		
		for (byte[] b: reversedChunks) {
			outStream.write(b);
		}
		
		outStream.close();
		
		System.out.println(String.format("Reversed %d bytes", total));
	}

	private static LinkedList<byte[]> reverse(LinkedList<byte[]> chunks) {
		final LinkedList<byte[]> result = new LinkedList<>();
		while (!chunks.isEmpty()) {
			final byte[] curr = chunks.removeLast();
			final byte[] currRev = new byte[curr.length];
			int i=curr.length-1, j=0;
			while (i --> 0) {
				currRev[j++] = curr[i];
			}
			result.add(currRev);
		}
		
		return result;
	}

}
