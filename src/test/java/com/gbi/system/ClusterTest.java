package com.gbi.system;

import org.junit.Before;

public class ClusterTest {

	Cluster cluster;

	@Before
	public void setup() throws Exception {
		cluster = new Cluster("test");
	}

}
