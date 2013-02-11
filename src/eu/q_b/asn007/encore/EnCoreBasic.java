package eu.q_b.asn007.encore;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import eu.q_b.asn007.lambda.LauncherConf;



public class EnCoreBasic {

	private static final double encoreVersion = 0.75;
	private final static DateFormat df = new SimpleDateFormat ("dd.MM.yyyy  hh:mm:ss ");
	private FileWriter logWriter;
	private final static String sep = System.getProperty("line.separator");
	private static String currentTime;
	private static Date now;
	private EnCoreOS osmodule;
	private EnCoreFile filemodule; 
	private EnCoreNetwork netmodule;
	private EnCoreInternal intmodule;
	private static EnCoreBasic _instance;
	
	public EnCoreBasic() {
		_instance = this;
		init();
		
	}
	
	public void init() {
		long l = System.currentTimeMillis();
		
		osmodule = new EnCoreOS();
		filemodule = new EnCoreFile();
		netmodule = new EnCoreNetwork();
		intmodule = new EnCoreInternal();
		
		if(osmodule == null) System.out.println("OS module is null..");
		setupLogger(new File(this.filemodule.getWorkingDirectory() + File.separator + LauncherConf.logFileName));
		log("version " + encoreVersion + " is loading...", this.getClass());
		
		log("initialized in " + (System.currentTimeMillis() - l) + "ms", this.getClass());
	}
	
	public void log(String text, Class<?> c) {
		if(logWriter == null) setupLogger(new File(this.filemodule.getWorkingDirectory() + File.separator + LauncherConf.logFileName));
		try {
			now = new Date();
			currentTime = df.format(now); 
			String nm = "";
			if(c.getSimpleName().equals("")) nm = "UNDEF"; else nm = c.getSimpleName();
			logWriter.write("[" + currentTime + "][" + nm + "] " + getLocalized(text) + sep);
			System.out.println("[ " + currentTime + "][" + nm + "] " + getLocalized(text));
		
			logWriter.flush();
		} catch(Exception e) {
			System.out.println(stack2string(e));
		}
		
	}

	
	public String getLocalized(String s) {
		return s;
	}
	
	public void setupLogger(File logFile) {
		try {
			if(!logFile.exists()) {
				//logFile.mkdirs();
				logFile.createNewFile();
			}
			if(getFileModule().readFileAsString(logFile.toString(), "").split("\n").length >= 500 /*To make logs more readable.. Approx. 5-7 launches*/) {
				logFile.delete();
				logFile.createNewFile();
			}
			logWriter = new FileWriter(logFile, true);
		} catch(Exception e) {
			System.out.println("[enCore] failed to initialize logger...");
			System.out.println(stack2string(e));
		}
	}
	
	public String stack2string(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "------\r\n" + sw.toString() + "------\r\n";
		} catch (Exception e2) {
			return "bad stack2string";
		}
	}

	public URL toURL(String url) {
		try {
			return new URL(url.replace(" ", "%20"));
		} catch (Exception e) {
			log("failed to transform String to URL.. Sorry..", this.getClass());
			return null;
		}
	}
	
	public URI toURI(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			log("failed to transform URL to URI.. Sorry..", this.getClass());
			return null;
		}
	}
	
	
	
	public EnCoreOS getOsmodule() {
		return osmodule;
	}

	public void setOsmodule(EnCoreOS osmodule) {
		this.osmodule = osmodule;
	}

	public EnCoreNetwork getNetmodule() {
		return netmodule;
	}

	public void setNetmodule(EnCoreNetwork netmodule) {
		this.netmodule = netmodule;
	}

	public EnCoreInternal getIntmodule() {
		return intmodule;
	}

	
	public void setIntmodule(EnCoreInternal intmodule) {
		this.intmodule = intmodule;
	}


	public EnCoreFile getFileModule() {
		return filemodule;
	}
	
	public void setFilemodule(EnCoreFile intmodule) {
		this.filemodule = intmodule;
	}
	
	public static EnCoreBasic getInstance() {
		return _instance;
	}

	public static void setInstance(EnCoreBasic _instance) {
		EnCoreBasic._instance = _instance;
	}
	
	
	
	public int randomInRange(int min, int max) {
		Random r = new Random(System.nanoTime());
		return r.nextInt(max - min + 1) + min;

	}
	

	public String buildRandomSession() {
		String s = "";
		for (int i = 0; i < 22; i++) {
			Random r = new Random(System.currentTimeMillis()
					+ System.nanoTime() * System.currentTimeMillis() + i
					+ Math.round(Math.random()));
			s = s + r.nextInt(9);
			r = null;
		}
		return s;
	}
	
public String filterString(String s) {
	return clear(new String[]{"!", "_", ",", ".", "?", ";"},s.replaceAll("[^\\d.]", ""));
}

public String clear(String[] symbols, String toclear) {
	for(String s: symbols) {
		toclear = toclear.replace(s, "");
	}
	if(toclear.equals("")) return "1";
	return toclear;
}

public boolean strcont(String name, String[] strings) {
	for(String s: strings) { if(name.contains(s)) return true;}
	return false;
}
	
}
