package com.game.render.fbo.psProcess.lights.type;

public class EscapyLightSrcFactory {

	public static EscapyLightType RND_64() {
		return new EscapyLightType(64);
	}
	public static EscapyLightType RND_128() {
		return new EscapyLightType(128);
	}
	public static EscapyLightType RND_256() {
		return new EscapyLightType(256);
	}
	public static EscapyLightType RND_512() {
		return new EscapyLightType(512);
	}
	public static EscapyLightType RND_1024() {
		return new EscapyLightType(1024);
	}
	public static EscapyLightType RND_2048() {
		return new EscapyLightType(2048);
	}
	public static EscapyLightType RND_4096() {
		return new EscapyLightType(4096);
	}

	public EscapyLightType RND_64 = RND_64();
	public EscapyLightType RND_128 = RND_128();
	public EscapyLightType RND_256 = RND_256();
	public EscapyLightType RND_512 = RND_512();
	public EscapyLightType RND_1024 = RND_1024();
	public EscapyLightType RND_2048 = RND_2048();
	public EscapyLightType RND_4096 = RND_4096();
}
