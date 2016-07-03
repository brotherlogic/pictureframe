package com.github.brotherlogic.pictureframe;

import javax.swing.JFrame;

public class Frame extends FrameBase {
	public static void main(String[] args) {
		Frame f = new Frame();
		FrameDisplay d = new FrameDisplay();
		d.setLocationRelativeTo(null);
		d.setSize(800, 480);
		d.setVisible(true);
	}
}
