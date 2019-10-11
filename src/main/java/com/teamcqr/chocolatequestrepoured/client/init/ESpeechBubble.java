package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;

public enum ESpeechBubble {

	BED("bubble_bed"),
	MAZE("bubble_maze"),
	SMILE("bubble_smile"),
	SWORD("bubble_sword");
	
	private final ResourceLocation resLoc;
	static final String folderPath = ":textures/misc/speechbubbles/";
	
	private ESpeechBubble(String name) {
		this.resLoc = new ResourceLocation(Reference.MODID, folderPath + name);
	}
	
	public ResourceLocation getResourceLocation() {
		return this.resLoc;
	}
	
}
