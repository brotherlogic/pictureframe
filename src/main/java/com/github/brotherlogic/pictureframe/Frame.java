package com.github.brotherlogic.pictureframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import frametracker.FrameTrackerServiceGrpc;
import frametracker.Frametracker.StatusRequest;
import frametracker.Frametracker.Status;

import io.grpc.BindableService;

public class Frame extends FrameBase {
    
    private DropboxConnector connector;
    private Config config;
    private File configFile;
    private boolean random = false;
    private int tokenHash;

    public static Status.Builder status = Status.newBuilder();
    
	public Frame(String token, File configFile, boolean r) {
		random = r;
		tokenHash = token.hashCode();
		connector = new DropboxConnector(token);

		try {
			if (configFile != null) {
				this.configFile = configFile;
				FileInputStream fis = new FileInputStream(configFile);
				config = new Config(proto.ConfigOuterClass.Config.parseFrom(fis).toByteArray());
			} else {
				config = new Config();
			}
		} catch (IOException e) {
			config = new Config();
		}
	}

	public void sendStatus() throws Exception {
		ManagedChannel channel = ManagedChannelBuilder.forAddress(getHost("proxy"), getPort("proxy")).usePlaintext(true).build();
		FrameTrackerServiceGrpc.FrameTrackerServiceBlockingStub client = FrameTrackerServiceGrpc.newBlockingStub(channel);
		client.withDeadlineAfter(30, TimeUnit.SECONDS).recordStatus(StatusRequest.newBuilder().setStatus(status.build()).build());
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void runWebServer() throws IOException {
		new HttpServer(config, this);
	}

	public static void main(String[] args) throws Exception {

		// Read the resources and print to stdout
		try {
			Properties p = new Properties();
			p.load((Frame.class.getResourceAsStream("properties.txt")));
		} catch (Exception e) {
		    e.printStackTrace();
		}

		Option optionServer = OptionBuilder.withLongOpt("server").hasArg().withDescription("Hostname of server")
				.create("s");
		Option optionToken = OptionBuilder.withLongOpt("token").hasArg().withDescription("Token to use for dropbox")
				.create("t");
		Option optionConfig = OptionBuilder.withLongOpt("config").hasArg().withDescription("Config file to user")
				.create("c");
		Option optionRandom = OptionBuilder.withLongOpt("random").hasArg().withDescription("Choose photo at random")
				.create("r");
		Option optionStart = OptionBuilder.withLongOpt("stime").hasArg().withDescription("Start Time")
				.create("st");
		Option optionEnd = OptionBuilder.withLongOpt("etime").hasArg().withDescription("End Time")
				.create("et");

		Options options = new Options();
		options.addOption(optionServer);
		options.addOption(optionToken);
		options.addOption(optionRandom);
		options.addOption(optionStart);
		options.addOption(optionEnd);
		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);

		String server = "10.0.1.17";
		if (line.hasOption("server"))
			server = line.getOptionValue("s");
		String token = "unknown";
		if (line.hasOption("token"))
			token = line.getOptionValue("t");
		boolean random = false;
		if (line.hasOption("random"))
			random = (line.getOptionValue("r").equals("true"));

		String configLocation = ".config";
		if (line.hasOption("config"))
			configLocation = line.getOptionValue("c");

		Frame f = new Frame(token, new File(configLocation), random);

		if (line.hasOption("stime")) {
		    int startTime = Integer.parseInt(line.getOptionValue("st"));
		    int endTime = Integer.parseInt(line.getOptionValue("et"));

		    f.setTime(startTime,endTime);
		}
		
		f.runWebServer();
		f.Serve(server);
	}

	@Override
	public String getServerName() {
		return "PictureFrame";
	}

    
	public void saveConfig() {
		try {
			FileOutputStream fos = new FileOutputStream(configFile);
			config.getConfig().writeTo(fos);
			fos.close();
		} catch (IOException e) {
		    //Pass
		}
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
					    connector.syncFolder("", out);
					    status.setTimeLastSync(System.currentTimeMillis());
					} catch (Exception e) {
					    e.printStackTrace();
					    status.setDropboxFailure("" + e);
					}
					try{
						Photo p = null;
						if (random)
							p = getRandomPhoto(out.getAbsolutePath());
						else
							p = getTimedLatestPhoto(out.getAbsolutePath());

						if (p != null) {
							final ImagePanel imgPanel = new ImagePanel(p.getImage());
							d.add(imgPanel);
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
			    //pass
			}
		}
	}

	FrameDisplay d;

	@Override
	public void localServe() {
	    try{
		status.setOrigin(getHostName());
	    } catch (Exception e){
		e.printStackTrace();
	    }
	    
		d = new FrameDisplay();
		d.setSize(800, 480);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
		d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
					    Thread.sleep(1000*60*5);
					    sendStatus();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();

		backgroundSync();
	}
}
