package team.cqr.cqrepoured.util;

import net.minecraft.client.Minecraft;

public class PartialTicksUtil {

	public static float getCurrentPartialTicks() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.isGamePaused()) {
			return mc.renderPartialTicksPaused;
		}
		return mc.getRenderPartialTicks();
	}

}
