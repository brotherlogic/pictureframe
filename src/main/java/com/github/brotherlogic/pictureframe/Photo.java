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
		Image img = ImageIO.read(Photo.class.getResourceAsStream(f.getName()));
		Image resizedImg = img.getScaledInstance(800, 480, Image.SCALE_SMOOTH);
		return resizedImg;
	}

	public String getName() {
		return f.getName();
	}
}
