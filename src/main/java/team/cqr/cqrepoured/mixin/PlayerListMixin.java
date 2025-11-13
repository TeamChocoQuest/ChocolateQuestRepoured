package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;
import team.cqr.cqrepoured.world.structure.generation.generation.PortalGenerationHandler;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

	@Inject(method = "transferEntityToWorld(Lnet/minecraft/entity/Entity;ILnet/minecraft/world/WorldServer;Lnet/minecraft/world/WorldServer;Lnet/minecraftforge/common/util/ITeleporter;)V", remap = false, at = @At(value = "INVOKE", target = "setLocationAndAngles(DDDFF)V", ordinal = 1, shift = Shift.AFTER))
	private void transferEntityToWorld(Entity entityIn, int lastDimension, WorldServer oldWorldIn, WorldServer toWorldIn, ITeleporter teleporter, CallbackInfo info) {
		if (entityIn instanceof EntityPlayer) {
			int chunkX = MathHelper.floor(entityIn.posX) >> 4;
			int chunkZ = MathHelper.floor(entityIn.posZ) >> 4;
			int radius = 4;

			((PortalGenerationHandler) toWorldIn).isGeneratingDestinationChunks(true);
			for (int x = -radius; x <= radius + 1; x++) {
				for (int z = -radius; z <= radius + 1; z++) {
					toWorldIn.getChunk(chunkX + x, chunkZ + z);
				}
			}
			((PortalGenerationHandler) toWorldIn).isGeneratingDestinationChunks(false);
		}
	}

}
