package com.github.brotherlogic.pictureframe;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

public class Frame extends FrameBase {
	public static void main(String[] args) throws IOException {
		Frame f = new Frame();
		FrameDisplay d = new FrameDisplay();
		d.setSize(800, 480);
		d.setLocationRelativeTo(null);
		d.setVisible(true);

		// Display an image
		GraphicsPanel p = new GraphicsPanel(new Photo(new File("images/IMG_5428.JPG")).getImage());
		d.add(p);
		d.validate();

		d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
