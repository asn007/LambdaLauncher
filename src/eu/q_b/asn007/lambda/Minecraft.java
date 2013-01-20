package eu.q_b.asn007.lambda;

import java.util.ArrayList;

public class Minecraft implements Comparable<Minecraft> {

	private String version;
	private String minecrafJarPath;
	private String type;
	private static final ArrayList<String> relations = new ArrayList<String>();
	
	public Minecraft(String version, String path, String type) {
		this.version = version;
		this.minecrafJarPath = path;
		this.type = type;
	}

	public String getMinecrafJarPath() {
		return minecrafJarPath;
	}

	public void setMinecrafJarPath(String minecrafJarPath) {
		this.minecrafJarPath = minecrafJarPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString() {
		return version;
	}

	private void fillRelations() {
		relations.add("release");
		relations.add("snapshot");
		relations.add("beta");
		relations.add("alpha");
		relations.add("indev");
		relations.add("infdev");
		relations.add("rc2");
		relations.add("rc1");
		relations.add("pcdemo");
	}

	@Override
	public int compareTo(Minecraft o) {
		if(relations == null || relations.size() <= 0) fillRelations();
		if(relations.indexOf(type) <  relations.indexOf(o.getType())) return -10;
		if(relations.indexOf(type) == relations.indexOf(o.getType())) {
			if(Integer.parseInt(Main.getFramework().filterString(this.version)) > Integer.parseInt(Main.getFramework().filterString(o.version))) return -10;
			else return 10;
		}
		else return 10;
		
	}

	private String getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
}
