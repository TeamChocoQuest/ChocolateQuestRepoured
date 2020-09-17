package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface IMayHaveTextureOverride {

	public boolean hasTextureOverride();

	@Nullable
	public ResourceLocation getTextureOverride();

	public Entity getEntity();

}
