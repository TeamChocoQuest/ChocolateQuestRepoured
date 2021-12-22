package team.cqr.cqrepoured.customtextures;

import java.awt.image.BufferedImage;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.IImageBuffer;

public class UniversalImageBuffer implements IImageBuffer {

	@Override
	@Nullable
	public BufferedImage parseUserSkin(BufferedImage image) {
		return image;
	}

	@Override
	public void skinAvailable() {
	}

}
