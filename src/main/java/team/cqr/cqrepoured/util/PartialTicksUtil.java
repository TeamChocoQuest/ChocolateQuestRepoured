package team.cqr.cqrepoured.util;

import net.minecraft.client.Minecraft;

public class PartialTicksUtil {

	public static float getCurrentPartialTicks() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.isPaused()) {
			return mc.pausePartialTick;
		}
		return mc.getFrameTime();
	}

}
