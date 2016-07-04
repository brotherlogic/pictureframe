package com.github.brotherlogic.pictureframe;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class PhotoTest {

	@Test
	public void testPhoto() throws IOException {
		Photo f = new Photo(new File("images/IMG_5428.JPG"));
		Image i = f.getImage();
		Assert.assertNotNull(i);
	}

}
