package com.gbi.system;

import static org.junit.Assert.assertEquals;

import org.jgroups.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class ClusterTest {

	Cluster cluster;

	@Before
	public void setup() throws Exception {
		cluster = new Cluster("test");
	}

	@Test
	public void testJoin() throws Exception {
		Thread.sleep(200);
		assertEquals(1, cluster.getServers().size());
		final String uuid = cluster.getServers().iterator().next().getUuid();
		cluster.suspect(new UUID() {

			@Override
			public String toString() {
				return uuid;
			}
		});
		assertEquals(0, cluster.getServers().size());
	}
}
