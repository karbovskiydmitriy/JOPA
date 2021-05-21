package app;

import static util.OGLUtil.getVersion;

import java.util.Arrays;

public class SystemInfo {

	public String arch;
	public String os;
	public String version;
	public int majorVersion;
	public int minorVersion;
	public String versionRawString;

	public static SystemInfo init() {
		SystemInfo system = new SystemInfo();

		system.arch = System.getProperty("os.arch");
		system.os = System.getProperty("os.name");
		system.version = System.getProperty("os.version");

		return system;
	}

	public boolean checkSystem() {
		if (!Arrays.asList("x86", "amd64").contains(arch)) {
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
			String[] versionParts = versionRawString.split(" ")[0].split("\\.");
			majorVersion = Integer.parseInt(versionParts[0]);
			minorVersion = Integer.parseInt(versionParts[1]);
		}

		if (majorVersion < 4 && minorVersion < 3) {
			return false;
		}

		return true;
	}

}