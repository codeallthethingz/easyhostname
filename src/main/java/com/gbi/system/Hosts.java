package com.gbi.system;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class Hosts {
	private static final String DEFAULT_WINDOWS_HOST_FILE = "c:\\windows\\system32\\drivers\\etc\\hosts";
	private static final String DEFAULT_LINUX_HOST_FILE = "/etc/hosts";

	private static final String START_INFO = "Easy Hostname Configuration --- GENERATED START";
	private static final String START_PREFIX = "#### ";
	public static final String START = START_PREFIX + START_INFO;
	private static final String END_INFO = "Easy Hostname Configuration --- GENERATED END";
	private static final String END_PREFIX = "#### ";
	public static final String END = END_PREFIX + END_INFO;
	private static final Pattern EASY_SECTION_START = Pattern.compile(
			"[#]+\\s*" + START_INFO, Pattern.CASE_INSENSITIVE);
	private static final Pattern EASY_SECTION_END = Pattern.compile("[#]+\\s*"
			+ END_INFO, Pattern.CASE_INSENSITIVE);
	private static String windowsHostFile = DEFAULT_WINDOWS_HOST_FILE;
	private static String linuxHostFile = DEFAULT_LINUX_HOST_FILE;

	public static String read() throws IOException {
		return FileUtils.readFileToString(getHostsFile());
	}

	private static File getHostsFile() {
		if (SystemUtils.IS_OS_WINDOWS) {
			return new File(windowsHostFile);
		}

		return new File(linuxHostFile);
	}

	public static void setHostsFile(String pHostFile) {
		if (SystemUtils.IS_OS_WINDOWS) {
			windowsHostFile = pHostFile;
		} else {
			linuxHostFile = pHostFile;
		}
	}

	public static boolean hasEasyHostnameConfig() throws IOException {
		String hostsFileContents = read();
		Matcher matcher = EASY_SECTION_START.matcher(hostsFileContents);
		return matcher.find();
	}

	public static void add(Server pServer) throws IOException {
		TreeSet<Server> servers = parseServers();
		if (servers.contains(pServer)) {
			servers.remove(pServer);
		}
		servers.add(pServer);
		writeServers(servers);
	}

	public static void writeServers(TreeSet<Server> pServers)
			throws IOException {
		StringBuffer newHosts = new StringBuffer();
		String hosts = read();
		boolean start = false;
		String[] lines = hosts.split("\n");
		for (String line : lines) {
			if (line.matches(EASY_SECTION_START.pattern())) {

				start = true;
			}
			if (line.matches(EASY_SECTION_END.pattern())) {
				start = false;
				continue;
			}
			if (!start) {
				newHosts.append(line).append("\n");
			}
		}

		appendHosts(pServers, newHosts);

		FileUtils.writeStringToFile(getHostsFile(), newHosts.toString());
	}

	private static void appendHosts(TreeSet<Server> pServers,
			StringBuffer newHosts) {
		if (pServers == null || pServers.size() == 0) {
			return;
		}
		Iterator<Server> iterator = pServers.iterator();
		newHosts.append("\n").append(START).append("\n");
		while (iterator.hasNext()) {
			Server server = iterator.next();
			newHosts.append(server.getIp()).append("\t\t")
					.append(server.getHostame()).append("\n");
		}
		newHosts.append(END).append("\n");
	}

	public static TreeSet<Server> parseServers() throws IOException {
		TreeSet<Server> servers = new TreeSet<Server>();
		String hosts = read();
		boolean start = false;
		String[] lines = hosts.split("\n");
		for (String line : lines) {
			if (line.matches(EASY_SECTION_START.pattern())) {
				start = true;
			}
			if (line.matches(EASY_SECTION_END.pattern())) {
				start = false;
			}
			if (start) {
				if (StringUtils.isNotBlank(line)
						&& !line.trim().startsWith("#")) {
					String[] ipName = line.trim().split("[ \t]", 2);
					servers.add(new Server(ipName[1].trim(), ipName[0].trim()));
				}
			}
		}
		return servers;
	}

	public static void remove(Server pServer) throws IOException {
		TreeSet<Server> servers = parseServers();
		servers.remove(pServer);
		writeServers(servers);
	}

	public static void removeEasyHostnames() throws IOException {
		writeServers(new TreeSet<Server>());
	}

	public static void setLinuxHostFile(String pLinuxHostFile) {
		linuxHostFile = pLinuxHostFile;
	}

	public static void setWindowsHostFile(String pWindowsHostFile) {
		windowsHostFile = pWindowsHostFile;
	}

	public static String getLinuxHostFile() {
		return linuxHostFile;
	}

	public static String getWindowsHostFile() {
		return windowsHostFile;
	}

}
