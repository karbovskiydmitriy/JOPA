package jopa.main;

import static jopa.util.JOPAOGLUtil.getVersion;

import java.util.Arrays;

public class JOPASystem {

	public String arch;
	public String os;
	public String version;
	public int majorVersion;
	public int minorVersion;
	public String versionRawString;

	public static JOPASystem init() {
		JOPASystem system = new JOPASystem();

		system.arch = System.getProperty("os.arch");
		system.os = System.getProperty("os.name");
		system.version = System.getProperty("os.version");

		// System.out.println(system.arch);
		// System.out.println(system.os);
		// System.out.println(system.version);

		return system;
	}

	public boolean checkSystem() {
		if (!Arrays.asList("x86", "x64").contains(arch)) {
			return false;
		}
		if (os.contains("Windows")) {
			String majorVersion = version.split("\\.")[0];
			if (Integer.parseInt(majorVersion) < 5) {
				return false;
			}
		} else if (os.contains("Ubuntu")) {
			String majorVersion = version.split("\\.")[0];
			if (Integer.parseInt(majorVersion) < 20) {
				return false;
			}
		}

		return true;
	}

	public boolean checkVersion() {
		if (versionRawString == null) {
			versionRawString = getVersion();
			// System.out.println("OpenGL version: " + versionRawString);
			String[] versionParts = versionRawString.split(" ")[0].split("\\.");
			majorVersion = Integer.parseInt(versionParts[0]);
			minorVersion = Integer.parseInt(versionParts[1]);
		}

		if (majorVersion < 4 && minorVersion < 3) {
			// uncomment later
			return true;
			// return false;
		}

		return true;
	}

}