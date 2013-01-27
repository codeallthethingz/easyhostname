package com.gbi.system;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class EasyHost {
	private static final String USAGE = "usage: easyhost --testWith <hostfile>";

	private static final Logger log = Logger.getLogger(EasyHost.class);
	private static final Options options = new Options();
	private static final CommandLineParser parser = new GnuParser();

	static {
		options.addOption(null, "testWith", true,
				"This overrides the default location of your hosts file for testing");
	}

	public static void main(String[] args) throws Exception {
		CommandLine cmd = checkForErrors(args);
		if (cmd.hasOption("testWith")) {
			Hosts.setHostsFile(cmd.getOptionValue("testWith"));
		}
		if (log.isInfoEnabled()) {
			log.info("Starting");
		}
		new Cluster("easyhostname");
		while (true) {
			Thread.sleep(1000);
		}
	}

	private static CommandLine checkForErrors(String[] args) {
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (Exception e) {
			printHelp("Error - " + e.getMessage() + "\n", true);
			System.exit(-1);
		}

		if (cmd.hasOption("testWith")) {
			File privateKeyFile = new File(cmd.getOptionValue("testWith"));
			if (!privateKeyFile.exists()) {
				printHelp("Error - test file does not exist\n", true);
				System.exit(-1);
			}
		}
		return cmd;
	}

	@SuppressWarnings("unchecked")
	private static void printHelp(String pMessage, boolean pShowUsage) {
		List<Option> printOptions = new ArrayList<Option>();
		printOptions.addAll(options.getOptions());
		Collections.sort(printOptions, new Comparator<Option>() {
			public int compare(Option pA, Option pB) {
				String aShort = pA.getOpt();
				if (aShort == null) {
					aShort = pA.getLongOpt().substring(0, 1);
				}
				String bShort = pB.getOpt();
				if (bShort == null) {
					bShort = pB.getLongOpt().substring(0, 1);
				}
				return aShort.compareTo(bShort);
			}
		});
		PrintWriter pw = new PrintWriter(System.out);
		pw.print(pMessage);
		if (pShowUsage) {

			pw.print(USAGE + "\n");
			for (Option option : printOptions) {
				String opt = (option.getOpt() != null ? " -" + option.getOpt()
						: "")
						+ (option.getLongOpt() != null ? " --"
								+ option.getLongOpt() : "")
						+ (option.hasArg() ? " <arg> " : "");
				pw.println(StringUtils.rightPad(opt, 24)
						+ option.getDescription());
			}
		} else {
			pw.print("\n");
		}
		pw.flush();
	}
}
