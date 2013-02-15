package eu.q_b.asn007.lambda;

public class DefaultMinecraft extends Minecraft {

	public DefaultMinecraft(String version, String path, String type) {
		super(version, path, type);
	}
	
	public String getMinecraftJarName() {
		return "minecraft.jar";
	}
	
	public String toString() {
		return Main.bundle.getString("lambda.yourJar");
	}
	
	// This should be the first one in the list :3
	public int compareTo(Minecraft mc) {
		return 10;
	}
	
	public String getJarURL() {
		return "http://s3.amazonaws.com/MinecraftDownload/minecraft.jar";
	}

}
