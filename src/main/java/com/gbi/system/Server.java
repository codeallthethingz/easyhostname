package com.gbi.system;

import org.apache.commons.lang.StringUtils;

public class Server implements Comparable<Server> {

	private String hostame;
	private String ip;

	private String uuid;

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
		uuid = pUuid;
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

	public String getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "[" + getHostame() + ":" + getIp() + ":" + getUuid() + "]";
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
		String one = new StringBuffer(getHostame()).append(getIp()).toString();
		String two = new StringBuffer(other.getHostame()).append(other.getIp())
				.toString();
		return one.equals(two);
	}

	@Override
	public int hashCode() {
		return new StringBuffer(getHostame()).append(getIp()).toString()
				.hashCode();
	}

	public int compareTo(Server pO) {
		return getHostame().compareTo(pO.getHostame());
	}

}
