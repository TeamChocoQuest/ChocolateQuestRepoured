package team.cqr.cqrepoured.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.gui.IUpdatableGui;

public class ClientOnlyMethods {
	
	@OnlyIn(Dist.CLIENT)
	public static Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@OnlyIn(Dist.CLIENT)
	public static Level getWorld() {
		return Minecraft.getInstance().level;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void updateUpdatableGUIs() {
		Screen gui = Minecraft.getInstance().screen;
		if (gui instanceof IUpdatableGui) {
			((IUpdatableGui) gui).update();
		}
	}

}
