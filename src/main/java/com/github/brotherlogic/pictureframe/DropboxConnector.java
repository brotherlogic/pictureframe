package com.github.brotherlogic.pictureframe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class DropboxConnector {

	DbxClient client;

	DbxRequestConfig config = new DbxRequestConfig("TuckerPictureFrame/1.0",
			Locale.getDefault().toString());

	public DropboxConnector(String token) {
		client = new DbxClient(config, token);
	}

	public void login(String APP_KEY, String APP_SECRET) throws Exception {
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		// Have the user sign in and authorize your app.
		String authorizeUrl = webAuth.start();
		System.out.println("1. Go to: " + authorizeUrl);
		System.out
				.println("2. Click \"Allow\" (you might have to log in first)");
		System.out.println("3. Copy the authorization code.");
		String code = new BufferedReader(new InputStreamReader(System.in))
				.readLine().trim();

		// This will fail if the user enters an invalid authorization code.
		DbxAuthFinish authFinish = webAuth.finish(code);
		String accessToken = authFinish.accessToken;
		System.out.println("TOKEN = " + accessToken);
	}

	public void syncFolder(String inputDirectory, File outputDirectory)
			throws Exception {
		DbxEntry.WithChildren listing = client
				.getMetadataWithChildren(inputDirectory);
		for (DbxEntry child : listing.children) {
			FileOutputStream outputStream = new FileOutputStream(
					outputDirectory.getAbsolutePath() + "/" + child.name);
			try {
				client.getFile(child.path, null, outputStream);
			} finally {
				outputStream.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		DropboxConnector connector = new DropboxConnector(args[0]);
		connector.syncFolder("/", new File("testout"));
	}
}
