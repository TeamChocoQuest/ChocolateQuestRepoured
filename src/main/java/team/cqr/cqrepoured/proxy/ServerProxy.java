package team.cqr.cqrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerProxy implements IProxy {

	@Override
	public void postInit() {

	}

	//Correct?
	@Override
	public Player getPlayer(Context context) {
		return context.getSender();
	}

	@Override
	public Level getWorld(Context context) {
		return context.getSender().level();
	}

	@Override
	public Advancement getAdvancement(Player player, ResourceLocation id) {
		if (player instanceof ServerPlayer) {
			return ((ServerPlayer) player).level().getServer().getAdvancements().getAdvancement(id);
		}
		return null;
	}

	@Override
	public boolean hasAdvancement(Player player, ResourceLocation id) {
		if (player instanceof ServerPlayer) {
			Advancement advancement = this.getAdvancement(player, id);
			if (advancement != null) {
				return ((ServerPlayer) player).getAdvancements().getOrStartProgress(advancement).isDone();
			}
		}
		return false;
	}

	@Override
	public void updateGui() {

	}

	@Override
	public boolean isOwnerOfIntegratedServer(Player player) {
		return false;
	}

	@Override
	public void openGui(int id, Player player, Level world, int... args) {

	}

	@Override
	public boolean isPlayerCurrentClientPlayer(Player player) {
		return false;
	}

}
