package com.github.brotherlogic.pictureframe;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class FrameTest {

	@Test
	public void testGreeting() {
		Frame f = new Frame("madeup", null, false);
		Assert.assertEquals("Running", f.getGreeting());
	}

	@Test
	public void testCompareFile() {
		File f1 = Mockito.mock(File.class);
		File f2 = Mockito.mock(File.class);
		Mockito.when(f1.lastModified()).thenReturn(10L);
		Mockito.when(f2.lastModified()).thenReturn(20L);

		Frame f = new Frame("madeup", null, false);
		Assert.assertTrue(f.compareFiles(f1, f2) < 0);
		Assert.assertTrue(f.compareFiles(f2, f1) > 0);
	}

	@Test
	public void testGetImage() {
		Frame f = new Frame("madeup", null, false);
		Photo p = f.getLatestPhoto("images");
		//Assert.assertEquals("IMG_5428.JPG", p.getName());
	}

	@Test
	public void testTimedGetImage() {
		Frame f = new Frame("madeup", null, false);
		Photo p = f.getTimedLatestPhoto("images");
		Assert.assertTrue(p.getName().length() > 0);
	}

	@Test
	public void testGetRandomImage() {
		Frame f = new Frame("madeup", null, false);
		Photo p = f.getRandomPhoto("images");
		Assert.assertTrue(p.getName().length() > 0);

		Photo p2 = f.getRandomPhoto("images");
		Assert.assertNull(p2);
	}

	@Test
	public void testGetImageWithFakeDirectory() {
		Frame f = new Frame("madeup", null, false);
		Photo p = f.getLatestPhoto("imageslblb");
		Assert.assertNull(p);
	}

	@Test
	public void testGetTimedImageWithFakeDirectory() {
		Frame f = new Frame("madeup", null, false);
		Photo p = f.getTimedLatestPhoto("imageslblb");
		Assert.assertNull(p);
	}

}
