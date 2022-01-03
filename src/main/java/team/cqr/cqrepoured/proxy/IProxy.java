package team.cqr.cqrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {

	void preInit();

	void init();

	void postInit();

	PlayerEntity getPlayer(MessageContext ctx);

	World getWorld(MessageContext ctx);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	Advancement getAdvancement(PlayerEntity player, ResourceLocation id);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	boolean hasAdvancement(PlayerEntity player, ResourceLocation id);

	void updateGui();

	boolean isOwnerOfIntegratedServer(PlayerEntity player);

	void openGui(int id, PlayerEntity player, World world, int... args);

}
