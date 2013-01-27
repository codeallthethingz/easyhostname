package com.gbi.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HostsTest {

	@BeforeClass
	public static void before() throws IOException {
		new File("target").mkdirs();
		Hosts.setLinuxHostFile("target/hostsLinux.txt");
		Hosts.setWindowsHostFile("target/hostsWindows.txt");
	}

	@Before
	public void each() throws IOException {
		FileUtils.writeStringToFile(new File(Hosts.getLinuxHostFile()),
				"# test hosts file - linux\n127.0.0.1        localhost");
		FileUtils.writeStringToFile(new File(Hosts.getWindowsHostFile()),
				"# test hosts file - windows\n127.0.0.1        localhost");
	}

	@Test
	public void testRemove() throws Exception {
		Hosts.removeEasyHostnames();
		assertEquals(-1, Hosts.read().indexOf(Hosts.START));
		assertEquals(-1, Hosts.read().indexOf(Hosts.END));
	}

	@Test
	public void testMultipleEasyHostnamesConfigs() throws Exception {
		FileUtils.writeStringToFile(new File(Hosts.getWindowsHostFile()),
				"# test hosts file - windows\n127.0.0.1        localhost\n"
						+ Hosts.START + "\n" + "127.0.0.1\t\tmyhost1\n"
						+ Hosts.END + "\n128.0.0.1         someother.com\n"
						+ Hosts.START + "\n" + "127.0.0.2\t\tmyhost2\n"
						+ Hosts.END);
		Hosts.remove(new Server("myhost1", "127.0.0.1"));
		assertEquals("# test hosts file - windows\n"
				+ "127.0.0.1        localhost\n"
				+ "128.0.0.1         someother.com\n"
				+ "#### Easy Hostname Configuration --- GENERATED START\n"
				+ "127.0.0.2		myhost2\n"
				+ "#### Easy Hostname Configuration --- GENERATED END\n" + "",
				Hosts.read());
	}

	@Test
	public void testAdd2SameHost() throws Exception {
		Hosts.add(new Server("myhost", "127.0.0.1"));
		Hosts.add(new Server("myhost", "127.0.0.2"));
		assertTrue(
				Hosts.read(),
				Hosts.read().contains(
						Hosts.START + "\n" + "127.0.0.2\t\tmyhost\n"
								+ Hosts.END));

	}

	@Test
	public void testAdd2DiffHost() throws Exception {

		Hosts.add(new Server("myhost1", "127.0.0.1"));
		Hosts.add(new Server("myhost2", "127.0.0.2"));
		assertTrue(
				Hosts.read(),
				Hosts.read().contains(
						Hosts.START + "\n" + "127.0.0.1\t\tmyhost1\n"
								+ "127.0.0.2\t\tmyhost2\n" + Hosts.END));
	}

	@Test
	public void testRemoveNonExistent() throws Exception {
		Hosts.remove(new Server("aoeu", "234"));
		assertEquals("# test hosts file - windows\n"
				+ "127.0.0.1        localhost\n", Hosts.read());
	}

	@Test
	public void testReadFile() throws Exception {
		assertTrue(Hosts.read(), Hosts.read().contains("localhost")
				&& Hosts.read().contains("127.0.0.1"));
	}

	@Test
	public void testWrite() throws Exception {
		TreeSet<Server> servers = new TreeSet<Server>();
		servers.add(new Server("myhost", "127.0.0.1"));
		Hosts.writeServers(servers);
		assertTrue(
				Hosts.read(),
				Hosts.read().contains(
						Hosts.START + "\n" + "127.0.0.1\t\tmyhost\n"
								+ Hosts.END));

	}

	@Test
	public void testParser() throws Exception {
		Server server = new Server("testhost", "127.0.0.1", "testuuid");
		assertEquals(0, Hosts.parseServers().size());
		Hosts.add(server);
		Hosts.add(server);
		assertEquals(1, Hosts.parseServers().size());
		Hosts.remove(server);
		assertEquals(0, Hosts.parseServers().size());
	}

	@Test
	public void testConfigArea() throws Exception {
		Server server = new Server("testhost", "127.0.0.1", "testuuid");
		assertFalse(Hosts.read(), Hosts.hasEasyHostnameConfig());
		Hosts.add(server);
		assertTrue(Hosts.read(), Hosts.hasEasyHostnameConfig());
		Hosts.remove(server);
		assertFalse(Hosts.read(), Hosts.hasEasyHostnameConfig());

	}
}
