package com.github.brotherlogic.pictureframe;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Photo {
	private File f;

	public Photo(File f) {
		this.f = f;
	}

	public Image getImage() throws IOException {
		Image img = ImageIO.read(f);

		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);

		double scaleFactor = (imgHeight + 0.0) / 480;

		int scaledHeight = (int) (imgHeight / scaleFactor);
		int scaledWidth = (int) (imgWidth / scaleFactor);

		Image resizedImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
		return resizedImg;
	}

	public String getName() {
		return f.getName();
	}
}
