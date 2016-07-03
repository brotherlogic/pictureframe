package com.github.brotherlogic.pictureframe;

public class Frame extends FrameBase {
	public static void main(String[] args) {
		Frame f = new Frame();
		FrameDisplay d = new FrameDisplay();
		d.setSize(200, 200);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
	}
}