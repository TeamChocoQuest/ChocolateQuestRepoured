package com.teamcqr.chocolatequestrepoured.customtextures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.IImageBuffer;

public class UniversalImageBuffer implements IImageBuffer {

	private int imageWidth;
	private int imageHeight;

	@Override
	@Nullable
	public BufferedImage parseUserSkin(BufferedImage image) {
		if (image == null) {
			return null;
		} else {
			this.imageWidth = image.getWidth();
			this.imageHeight = image.getHeight();
			BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
			Graphics graphics = bufferedimage.getGraphics();
			graphics.drawImage(image, 0, 0, (ImageObserver) null);

			graphics.dispose();

			return bufferedimage;
		}
	}

	@Override
	public void skinAvailable() {
	}

}
