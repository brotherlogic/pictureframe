package com.github.brotherlogic.pictureframe;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class FrameTest {

	@Test
	public void testGreeting() {
		Frame f = new Frame("madeup");
		Assert.assertEquals("Running", f.getGreeting());
	}

	@Test
	public void testCompareFile() {
		File f1 = Mockito.mock(File.class);
		File f2 = Mockito.mock(File.class);
		Mockito.when(f1.lastModified()).thenReturn(10L);
		Mockito.when(f2.lastModified()).thenReturn(20L);

		Frame f = new Frame("madeup");
		Assert.assertTrue(f.compareFiles(f1, f2) < 0);
		Assert.assertTrue(f.compareFiles(f2, f1) > 0);
	}

	@Test
	public void testGetImage() {
		Frame f = new Frame("madeup");
		Photo p = f.getLatestPhoto("images");
		Assert.assertEquals("IMG_5394.JPG", p.getName());
	}

	@Test
	public void testGetImageWithFakeDirectory() {
		Frame f = new Frame("madeup");
		Photo p = f.getLatestPhoto("imageslblb");
		Assert.assertNull(p);
	}

}
