package eu.q_b.asn007.encore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import eu.q_b.asn007.lambda.Main;

public class EnCoreNetwork {

	public void download(URL url, File f, JLabel p, JProgressBar progressbar)
			throws Exception {
		f.mkdirs();

		f.delete();
		f.createNewFile();
		URLConnection connection = url.openConnection();

		long down = connection.getContentLength();

		long downm = f.length();

		if (downm != down) {

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());

			FileOutputStream fw = new FileOutputStream(f);

			byte[] b = new byte[1024];
			int count = 0;
			long total = 0;

			while ((count = bis.read(b)) != -1) {
				total += count;
				fw.write(b, 0, count);
				progressbar.setValue(Math.round((100 * total) / down));
				progressbar.setString(f.getName() + " ("
						+ Math.round((100 * total) / down) + "%)");
			}
			fw.close();
		}

		else
			return;

	}

	public void openLink(URI uri) {
		try {
			Object o = Class.forName("java.awt.Desktop")
					.getMethod("getDesktop", new Class[0])
					.invoke(null, new Object[0]);
			o.getClass().getMethod("browse", new Class[] { URI.class })
					.invoke(o, new Object[] { uri });
		} catch (Throwable e) {
			Main.getFramework().log("failed to open " + uri.toString(),
					this.getClass());
			e.printStackTrace();
		}
	}

	public String runGET(String URL, String param) {
		try {
			URL localURL;
			localURL = new URL(URL + "?" + param);

			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localURL.openStream()));
			StringBuffer sb = new StringBuffer();
			String result;
			while ((result = localBufferedReader.readLine()) != null)
				sb.append(result + "\n");
			return sb.toString();
		} catch (Exception e) {
			Main.getFramework()
					.log("Error while running GET request! Returning empty string...",
							this.getClass());
			Main.getFramework().log(Main.getFramework().stack2string(e),
					this.getClass());
			return "";
		}

	}

	public String runPOST(String URL, String param) {

		HttpURLConnection connection = null;
		try {
			URL url = new URL(URL);
			connection = (HttpURLConnection) url.openConnection();

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			connection.connect();

			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			StringBuffer response = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();

			String str1 = response.toString();

			return str1;
		} catch (Exception e) {
			return "";
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String[] getServerOnline(String ip, String port) throws Exception {
		try {
			Socket var3 = null;
			DataInputStream var4 = null;
			DataOutputStream var5 = null;

			var3 = new Socket();
			var3.setSoTimeout(3000);
			var3.setTcpNoDelay(true);
			var3.setTrafficClass(18);
			var3.connect(new InetSocketAddress(ip, Integer.parseInt(port)),
					3000);
			var4 = new DataInputStream(var3.getInputStream());
			var5 = new DataOutputStream(var3.getOutputStream());
			var5.write(254);

			if (var4.read() != 255) {
				throw new IOException("Bad message");
			}

			String var6 = readString(var4, 256);
			char[] var7 = var6.toCharArray();
			var6 = new String(var7);
			String[] var27 = var6.split("\u00a7");
			var6 = var27[0];

			int var9 = Integer.parseInt(var27[1]);
			int var10 = Integer.parseInt(var27[2]);

			Main.getFramework().log("Online: " + var9 + " of " + var10,
					this.getClass());
			return new String[] { var9 + "", var10 + "" };
		} catch (Exception e) {
			throw new Exception("Failed to get server online!");
		}

	}

	public static String readString(DataInputStream par0DataInputStream,
			int par1) throws IOException {
		short var2 = par0DataInputStream.readShort();

		if (var2 > par1) {
			throw new IOException(
					"Received string length longer than maximum allowed ("
							+ var2 + " > " + par1 + ")");
		} else if (var2 < 0) {
			throw new IOException(
					"Received string length is less than zero! Weird string!");
		} else {
			StringBuilder var3 = new StringBuilder();

			for (int var4 = 0; var4 < var2; ++var4) {
				var3.append(par0DataInputStream.readChar());
			}

			return var3.toString();
		}
	}

	public boolean isOnline() {

		URL url;
		URLConnection urlconn;

		try {
			url = new URL("http://google.com/");
		} catch (MalformedURLException e) {
			return false;
		}

		try {
			urlconn = url.openConnection();
			urlconn.connect();
			return true;
		} catch (IOException e1) {
			return false;
		}

	}

	public long getFileLength(String url) {
		try {
			return Main.getFramework().toURL(url).openConnection()
					.getContentLength();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return -1;
		}

	}

}
