package com.gbi.system;

import org.apache.commons.lang.StringUtils;

public class Server implements Comparable<Server> {

	private String hostame;
	private String ip;

	public Server(String pHostame, String pIp, String pUuid) {
		super();
		hostame = pHostame;
		ip = pIp;
		if (StringUtils.isBlank(hostame)) {
			throw new IllegalStateException("Hostname was empty");
		}
		if (StringUtils.isBlank(ip)) {
			throw new IllegalStateException("IP was empty");
		}
	}

	public Server(String pHostame, String pIp) {
		this(pHostame, pIp, null);
	}

	public String getHostame() {
		return hostame;
	}

	public String getIp() {
		return ip;
	}

	@Override
	public String toString() {
		return "[" + getHostame() + ":" + getIp() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Server other = (Server) obj;
		String one = new StringBuffer(getHostame()).toString();
		String two = new StringBuffer(other.getHostame()).toString();
		return one.equals(two);
	}

	@Override
	public int hashCode() {
		return new StringBuffer(getHostame()).toString().hashCode();
	}

	public int compareTo(Server pO) {
		return getHostame().compareTo(pO.getHostame());
	}

}
