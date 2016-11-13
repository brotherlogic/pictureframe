package com.github.brotherlogic.pictureframe;

import org.junit.Assert;
import org.junit.Test;

import proto.ConfigOuterClass.Config;

public class ConfigTest {

	@Test
	public void testInitAndReturnConfig() throws Exception {
		Config c = Config.newBuilder().setRecentImages(10).build();
		com.github.brotherlogic.pictureframe.Config config = new com.github.brotherlogic.pictureframe.Config(
				c.toByteArray());
		Assert.assertEquals(config.getConfig().getRecentImages(), 10);
	}

}
