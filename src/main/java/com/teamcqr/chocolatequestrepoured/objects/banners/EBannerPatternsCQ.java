package com.teamcqr.chocolatequestrepoured.objects.banners;

import net.minecraft.tileentity.BannerPattern;

public enum EBannerPatternsCQ {
	
	//TODO Wait for Artsquad to finish the textures
	;
	
	private BannerPattern mcPattern;
	
	private EBannerPatternsCQ(BannerPattern pattern) {
		this.mcPattern = pattern;
	}
	
	public BannerPattern getPattern() {
		return this.mcPattern;
	}
	
	public void changePattern(BannerPattern newPattern) {
		this.mcPattern = newPattern;
	}

}
