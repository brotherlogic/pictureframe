package com.github.brotherlogic.pictureframe;

import com.google.protobuf.InvalidProtocolBufferException;

public class Config {

	private proto.ConfigOuterClass.Config protoConfig;

	public Config(byte[] data) throws InvalidProtocolBufferException {
		protoConfig = proto.ConfigOuterClass.Config.parseFrom(data);
	}

	public byte[] dumpConfig() {
		return protoConfig.toByteArray();
	}

	public proto.ConfigOuterClass.Config getConfig() {
		return protoConfig;
	}
}
