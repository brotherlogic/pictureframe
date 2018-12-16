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

    	@Test
	public void testPhotoSizing() throws IOException {
		Photo f = new Photo(new File("images/christmas.jpg"));
		Image i = f.getImage();
		Assert.assertNotNull(i);

		Assert.assertEquals(480, i.getHeight(null));
		Assert.assertEquals(360, i.getWidth(null));
	}


}
