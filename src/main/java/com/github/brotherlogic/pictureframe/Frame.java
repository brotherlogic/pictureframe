package com.github.brotherlogic.pictureframe;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import io.grpc.BindableService;

public class Frame extends FrameBase {

	public static void main(String[] args) throws Exception {
		Option optionHost = OptionBuilder.withLongOpt("host").hasArg().withDescription("Hostname of server")
				.create("h");
		Option optionPort = OptionBuilder.withLongOpt("port").hasArg().withDescription("Port number of server")
				.create("p");
		Options options = new Options();
		options.addOption(optionHost);
		options.addOption(optionPort);
		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);

		String host = "10.0.1.17";
		System.out.println("ARGS = " + Arrays.toString(args));
		if (line.hasOption("host"))
			host = line.getOptionValue("h");
		int port = 50051;
		if (line.hasOption("port"))
			port = Integer.parseInt(line.getOptionValue("p"));

		Frame f = new Frame();
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

	@Override
	public void localServe() {
		FrameDisplay d = new FrameDisplay();
		d.setSize(800, 480);
		d.setLocationRelativeTo(null);
		d.setVisible(true);

		// Display an image
		try {
			ImagePanel p = new ImagePanel(new Photo(new File("images/IMG_5428.JPG")).getImage());
			d.add(p);
			d.validate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
