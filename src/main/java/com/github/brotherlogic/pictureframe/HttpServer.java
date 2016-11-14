package com.github.brotherlogic.pictureframe;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {

	private Config conf;

	public HttpServer() throws IOException {
		super(8080);
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

	@Override
	public Response serve(IHTTPSession session) {
		String msg = "<html><body><h1>Hello server</h1>\n";
		Map<String, String> parms = session.getParms();
		if (parms.get("username") == null) {
			msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n"
					+ "</form>\n";
		} else {
			msg += "<p>Hello, " + parms.get("username") + "!</p>";
		}
		return newFixedLengthResponse(msg + "</body></html>\n");
	}
}