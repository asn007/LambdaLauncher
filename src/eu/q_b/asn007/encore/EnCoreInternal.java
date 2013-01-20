package eu.q_b.asn007.encore;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import eu.q_b.asn007.lambda.Main;


public class EnCoreInternal {
	
	private Font laF;
	
	public Font getLauncherFont(float size) {
		if(laF == null) {
			try {
				Main.getFramework().log("loading fonts...", this.getClass());
				laF = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/eu/q_b/asn007/lambda/font.ttf")).deriveFont(size);
			} catch (Exception e) {
				Main.getFramework().log("failed to load fonts! Launcher will look ugly...", this.getClass());
				Main.getFramework().log(Main.getFramework().stack2string(e), this.getClass());
				e.printStackTrace();
				laF = new Font(Font.SANS_SERIF, Font.ITALIC, Math.round(size));
			}
		} 
		return laF.deriveFont(size);
	}
	
	public Font getLauncherFont(float size, int style) {
		if(laF == null) {
			try {
				Main.getFramework().log("Loading fonts...", this.getClass());
				laF = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/eu/q_b/asn007/lambda/font.ttf")).deriveFont(size).deriveFont(style);
			} catch (Exception e) {
				Main.getFramework().log("Failed to load fonts! Launcher will look ugly...", this.getClass());
				Main.getFramework().log(Main.getFramework().stack2string(e), this.getClass());
				e.printStackTrace();
				laF = new Font(Font.SANS_SERIF, Font.BOLD, Math.round(size));
			}
		} 
		return laF;
	}
	
	public BufferedImage getImage(String resname) {
		try {
			Main.getFramework().log("Loading image: " + resname, this.getClass());
			return  ImageIO.read(EnCoreInternal.class.getResourceAsStream("/eu/q_b/asn007/lambda/" + resname));
		} catch (IOException e) {
			Main.getFramework().log("Loading image " + resname + " failed!", this.getClass());
			Main.getFramework().log(Main.getFramework().stack2string(e), this.getClass());
			return null;
		}
	}
	
	public String readInputStreamAsString(InputStream in) {
		try {
		    BufferedInputStream bis = new BufferedInputStream(in);
		    ByteArrayOutputStream buf = new ByteArrayOutputStream();
		    int result = bis.read();
		    while(result != -1) {
		      byte b = (byte)result;
		      buf.write(b);
		      result = bis.read();
		    }        
		    return buf.toString();
		}catch(Exception e) {
			return "";
		}
		}

}
