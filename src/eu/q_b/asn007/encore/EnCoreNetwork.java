package eu.q_b.asn007.encore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

			return -1;
		}

	}

}
