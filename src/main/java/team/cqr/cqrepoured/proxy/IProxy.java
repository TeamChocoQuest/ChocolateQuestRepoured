package team.cqr.cqrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public interface IProxy {

	void postInit();

	Player getPlayer(Context ctx);

	Level getWorld(Context ctx);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	Advancement getAdvancement(Player player, ResourceLocation id);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	boolean hasAdvancement(Player player, ResourceLocation id);

	void updateGui();

	boolean isOwnerOfIntegratedServer(Player player);
	
	boolean isPlayerCurrentClientPlayer(Player player);

	void openGui(int id, Player player, Level world, int... args);

}
