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

	@Test
	public void testSaveAndLoadConfig() throws Exception {
		Config c = Config.newBuilder().setRecentImages(10).build();
		com.github.brotherlogic.pictureframe.Config config = new com.github.brotherlogic.pictureframe.Config(
				c.toByteArray());
		byte[] data = config.dumpConfig();
		com.github.brotherlogic.pictureframe.Config config2 = new com.github.brotherlogic.pictureframe.Config(data);
		Assert.assertEquals(config2.getConfig().getRecentImages(), 10);

	}

}
