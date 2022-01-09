package team.cqr.cqrepoured.util;

import net.minecraft.client.Minecraft;

public class PartialTicksUtil {

	public static float getCurrentPartialTicks() {
		return Minecraft.getInstance().getFrameTime();
	}

}
