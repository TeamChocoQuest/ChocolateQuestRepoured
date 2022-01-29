package team.cqr.cqrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements IProxy {

	@Override
	public void preInit() {

	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@Override
	public PlayerEntity getPlayer(MessageContext context) {
		return context.getServerHandler().player;
	}

	@Override
	public World getWorld(MessageContext context) {
		return context.getServerHandler().player.world;
	}

	@Override
	public Advancement getAdvancement(PlayerEntity player, ResourceLocation id) {
		if (player instanceof ServerPlayerEntity) {
			return ((ServerPlayerEntity) player).getServerWorld().getAdvancementManager().getAdvancement(id);
		}
		return null;
	}

	@Override
	public boolean hasAdvancement(PlayerEntity player, ResourceLocation id) {
		if (player instanceof ServerPlayerEntity) {
			Advancement advancement = this.getAdvancement(player, id);
			if (advancement != null) {
				return ((ServerPlayerEntity) player).getAdvancements().getProgress(advancement).isDone();
			}
		}
		return false;
	}

	@Override
	public void updateGui() {

	}

	@Override
	public boolean isOwnerOfIntegratedServer(PlayerEntity player) {
		return false;
	}

	@Override
	public void openGui(int id, PlayerEntity player, World world, int... args) {

	}

	@Override
	public boolean isPlayerCurrentClientPlayer(EntityPlayer player) {
		return false;
	}

}
