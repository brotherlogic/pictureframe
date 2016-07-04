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
		System.out.println(f.getName() + " -> " + Photo.class.getResourceAsStream(f.getName()));
		return ImageIO.read(Photo.class.getResourceAsStream(f.getName()));
	}

	public String getName() {
		return f.getName();
	}
}
