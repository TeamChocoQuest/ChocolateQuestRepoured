package team.cqr.cqrepoured.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.gui.IUpdatableGui;

public class ClientOnlyMethods {
	
	@OnlyIn(Dist.CLIENT)
	public static PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@OnlyIn(Dist.CLIENT)
	public static World getWorld() {
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
