package team.cqr.cqrepoured.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientPlayerUtil {
	
	@OnlyIn(Dist.CLIENT)
	public static PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@OnlyIn(Dist.CLIENT)
	public static World getWorld() {
		return Minecraft.getInstance().level;
	}

}
