package com.github.brotherlogic.pictureframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import io.grpc.BindableService;

public class Frame extends FrameBase {

	private DropboxConnector connector;
	private Config config;
	private File configFile;

	public Frame(String token, File configFile) {
		connector = new DropboxConnector(token);

		try {
			if (configFile != null) {
				FileInputStream fis = new FileInputStream(configFile);
				config = new Config(proto.ConfigOuterClass.Config.parseFrom(fis).toByteArray());
			} else {
				config = new Config();
			}
		} catch (IOException e) {
			config = new Config();
		}
	}

	public void runWebServer() throws IOException {
		new HttpServer(config, this);
	}

	public static void main(String[] args) throws Exception {
		Option optionHost = OptionBuilder.withLongOpt("host").hasArg().withDescription("Hostname of server")
				.create("h");
		Option optionPort = OptionBuilder.withLongOpt("port").hasArg().withDescription("Port number of server")
				.create("p");
		Option optionToken = OptionBuilder.withLongOpt("token").hasArg().withDescription("Token to use for dropbox")
				.create("t");
		Option optionConfig = OptionBuilder.withLongOpt("config").hasArg().withDescription("Config file to user")
				.create("c");
		Options options = new Options();
		options.addOption(optionHost);
		options.addOption(optionPort);
		options.addOption(optionToken);
		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);

		String host = "10.0.1.17";
		System.out.println("ARGS = " + Arrays.toString(args));
		if (line.hasOption("host"))
			host = line.getOptionValue("h");
		int port = 50051;
		if (line.hasOption("port"))
			port = Integer.parseInt(line.getOptionValue("p"));
		String token = "unknown";
		if (line.hasOption("token"))
			token = line.getOptionValue("t");

		String configLocation = ".config";
		if (line.hasOption("config"))
			configLocation = line.getOptionValue("c");

		Frame f = new Frame(token, new File(configLocation));
		f.runWebServer();
		f.Serve(host, port);
	}

	@Override
	public String getServerName() {
		return "PictureFrame";
	}

	public void saveConfig() {

	}

	@Override
	public List<BindableService> getServices() {
		return new LinkedList<BindableService>();
	}

	public void syncAndDisplay() {
		if (connector != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					d.getContentPane().removeAll();

					File out = new File("pics/");
					out.mkdirs();
					try {
						connector.syncFolder("/", out);

						Photo p = getTimedLatestPhoto(out.getAbsolutePath());
						System.out.println("Got picture: " + p.getName());
						if (p != null) {
							final ImagePanel imgPanel = new ImagePanel(p.getImage());
							d.add(imgPanel);
							System.out.println("Added picture");
							d.revalidate();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	public Config getConfig() {
		return config;
	}

	public void backgroundSync() {
		while (true) {
			syncAndDisplay();

			// Wait before updating the picture
			try {
				Thread.sleep(2 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	FrameDisplay d;

	@Override
	public void localServe() {
		d = new FrameDisplay();
		d.setSize(800, 480);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
		d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		backgroundSync();
	}
}
