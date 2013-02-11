package eu.q_b.asn007.lambda;

import java.io.File;
import java.io.FilenameFilter;

public class MinecraftFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith("jar") && Main.getFramework().strcont(name, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}) || name.toString().contains("minecraft.jar");
	}

}
