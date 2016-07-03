package com.github.brotherlogic.pictureframe;

import org.junit.Assert;
import org.junit.Test;

public class FrameTest {

	@Test
	public void testGreeting() {
		Frame f = new Frame();
		Assert.assertEquals("Running", f.getGreeting());
	}

}
