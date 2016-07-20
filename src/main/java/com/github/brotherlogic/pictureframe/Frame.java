package com.github.brotherlogic.pictureframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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

	public Frame(String token) {
		connector = new DropboxConnector(token);
	}

	public static void main(String[] args) throws Exception {
		Option optionHost = OptionBuilder.withLongOpt("host").hasArg().withDescription("Hostname of server")
				.create("h");
		Option optionPort = OptionBuilder.withLongOpt("port").hasArg().withDescription("Port number of server")
				.create("p");
		Option optionToken = OptionBuilder.withLongOpt("token").hasArg().withDescription("Token to use for dropbox")
				.create("t");
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

		Frame f = new Frame(token);
		f.Serve(host, port);
	}

	@Override
	public String getServerName() {
		return "PictureFrame";
	}

	@Override
	public List<BindableService> getServices() {
		return new LinkedList<BindableService>();
	}

	public void syncAndDisplay() {
		if (connector != null) {
			try {
				File out = new File("pics/");
				out.mkdirs();
				connector.syncFolder("/", out);

				Photo p = getLatestPhoto(out.getAbsolutePath());
				if (p != null) {
					final ImagePanel imgPanel = new ImagePanel(p.getImage());
					d.add(imgPanel);
					d.validate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void backgroundSync() {
		while (true) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					syncAndDisplay();
				}
			});
			// Wait before updating the picture
			try {
				Thread.sleep(2 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Turn the display off
			try {
				System.out.println("Shutting down display");
				Process p = Runtime.getRuntime().exec("xset -display :0.0 dpms force off");
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				for (String line = reader.readLine(); line != null; line = reader.readLine())
					System.out.println("OUT = " + line);
			} catch (Exception e) {
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
