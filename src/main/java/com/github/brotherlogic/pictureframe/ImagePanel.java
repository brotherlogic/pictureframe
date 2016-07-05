package com.github.brotherlogic.pictureframe;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	Image img;

	public ImagePanel(Image image) {
		img = image;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize() {
		return getParent().getSize();
	}

}
