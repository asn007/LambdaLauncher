package eu.q_b.asn007.lambda;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

public class BSPatcher {

	@SuppressWarnings({"resource", "unused"})
	public static void bspatch (File oldFile, OutputStream newFile, File diffFile)
	throws IOException {

		int oldpos, newpos;

		DataInputStream diffIn = new DataInputStream (new FileInputStream(diffFile));

		// headerMagic at header offset 0 (length 8 bytes)
		long headerMagic = diffIn.readLong();

		// ctrlBlockLen after gzip compression at heater offset 8 (length 8 bytes)
		long ctrlBlockLen = diffIn.readLong();

		// diffBlockLen after gzip compression at header offset 16 (length 8 bytes)
		long diffBlockLen = diffIn.readLong();

		// size of new file at header offset 24 (length 8 bytes)
		int newsize = (int)diffIn.readLong();
		FileInputStream in;
		in = new FileInputStream (diffFile);
		in.skip(ctrlBlockLen + 32);
		GZIPInputStream diffBlockIn = new GZIPInputStream(in);

		in = new FileInputStream (diffFile);
		in.skip (diffBlockLen + ctrlBlockLen + 32);
		GZIPInputStream extraBlockIn = new GZIPInputStream(in);

		/*
		 * Read in old file (file to be patched) to oldBuf
		 */
		int oldsize = (int) oldFile.length();
		byte[] oldBuf = new byte[oldsize + 1];
		FileInputStream oldIn = new FileInputStream(oldFile);
		readFromStream(oldIn, oldBuf, 0, oldsize);
		oldIn.close();

		byte[] newBuf = new byte[newsize + 1];

		oldpos = 0;
		newpos = 0;
		int[] ctrl = new int[3];
		int nbytes;
		while (newpos < newsize) {

			for (int i = 0; i <= 2; i++) {
				ctrl[i] = diffIn.readInt();
				//System.err.println ("  ctrl[" + i + "]=" + ctrl[i]);
			}

			if (newpos + ctrl[0] > newsize) {
				System.err.println("Corrupt patch\n");
				return;
			}

			/*
			 * Read ctrl[0] bytes from diffBlock stream
			 */

			if (!readFromStream(diffBlockIn, newBuf, newpos, ctrl[0])) {
				System.err.println ("error reading from diffIn");
				return;
			}

			for (int i = 0; i < ctrl[0]; i++) {
				if ((oldpos + i >= 0) && (oldpos + i < oldsize)) {
					newBuf[newpos + i] += oldBuf[oldpos + i];
				}
			}

			newpos += ctrl[0];
			oldpos += ctrl[0];

			if (newpos + ctrl[1] > newsize) {
				System.err.println("Corrupt patch");
				return;
			}


			if (!readFromStream(extraBlockIn, newBuf, newpos, ctrl[1])) {
				System.err.println ("error reading from extraIn");
				return;
			}

			newpos += ctrl[1];
			oldpos += ctrl[2];
		}
		diffBlockIn.close();
		extraBlockIn.close();
		diffIn.close();

		newFile.write(newBuf,0,newBuf.length-1);
		newFile.close();
	}
	@SuppressWarnings({"resource", "unused"})
	public static void bspatchDirect (File oldFile, OutputStream newFile, File diffFile)
	throws IOException {

		int oldpos, newpos;

		DataInputStream diffIn = new DataInputStream (new FileInputStream(diffFile));

		// headerMagic at header offset 0 (length 8 bytes)
		long headerMagic = diffIn.readLong();

		// ctrlBlockLen after gzip compression at heater offset 8 (length 8 bytes)
		long ctrlBlockLen = diffIn.readLong();

		// diffBlockLen after gzip compression at header offset 16 (length 8 bytes)
		long diffBlockLen = diffIn.readLong();

		// size of new file at header offset 24 (length 8 bytes)
		int newsize = (int)diffIn.readLong();

		/*
		System.err.println ("newsize=" + newsize);
		System.err.println ("ctrlBlockLen=" + ctrlBlockLen);
		System.err.println ("diffBlockLen=" + diffBlockLen);
		System.err.println ("newsize=" + newsize);
		*/

		FileInputStream in;
		in = new FileInputStream (diffFile);
		in.skip(ctrlBlockLen + 32);
		DataInputStream diffBlockIn = new DataInputStream(new GZIPInputStream(in));

		in = new FileInputStream (diffFile);
		in.skip (diffBlockLen + ctrlBlockLen + 32);
		DataInputStream extraBlockIn = new DataInputStream(new GZIPInputStream(in));

		/*
		 * Read in old file (file to be patched) to oldBuf
		 */
		int oldsize = (int) oldFile.length();
		byte[] oldBuf = new byte[oldsize+1];
		DataInputStream oldIn = new DataInputStream(new FileInputStream(oldFile));
		oldIn.readFully(oldBuf, 0, oldsize);
		oldIn.close();

		oldpos = 0;
		newpos = 0;
		int[] ctrl = new int[3];
		byte[] newBuf;
		int nbytes;
		while (newpos < newsize) {

			for (int i = 0; i <= 2; i++) {
				ctrl[i] = diffIn.readInt();
				System.out.println ("  ctrl[" + i + "]=" + ctrl[i]);
			}

			if (newpos + ctrl[0] > newsize) {
				System.err.println("Corrupt patch\n");
				return;
			}

			/*
			 * Read ctrl[0] bytes from diffBlock stream
			 */
			newBuf = new byte[ctrl[0]];
			diffBlockIn.readFully(newBuf);

			for (int i = 0; i < ctrl[0]; i++) {
				if ((oldpos + i >= 0) && (oldpos + i < oldsize)) {
					newBuf[i] += oldBuf[oldpos + i];
				}
			}
			newFile.write(newBuf);

			newpos += ctrl[0];
			oldpos += ctrl[0];

			if (newpos + ctrl[1] > newsize) {
				System.err.println("Corrupt patch");
				return;
			}

			newBuf = new byte[ctrl[1]];			
			extraBlockIn.readFully(newBuf, 0, ctrl[1]);
			newFile.write(newBuf);
			newpos += ctrl[1];
			oldpos += ctrl[2];
		}
		diffBlockIn.close();
		extraBlockIn.close();
		diffIn.close();
		newFile.close();
	}
	public static final boolean readFromStream (InputStream in, byte[] buf, int offset, int len)
	throws IOException 
	{
			
		int totalBytesRead = 0;
		int nbytes;
		
		while ( totalBytesRead < len) {
			nbytes = in.read(buf,offset+totalBytesRead,len-totalBytesRead);
			if (nbytes < 0) {
				System.err.println ("readFromStream(): returning prematurely. Read " 
						+ totalBytesRead + " bytes");
				return false;
			}
			totalBytesRead+=nbytes;
		}
		
		return true;
	}
	
}
