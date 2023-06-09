package team.cqr.cqrepoured.customtextures;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public interface IHasTextureOverride {

	public boolean hasTextureOverride();

	public ResourceLocation getTextureOverride();

	public void setCustomTexture(@Nonnull ResourceLocation texture);

}
