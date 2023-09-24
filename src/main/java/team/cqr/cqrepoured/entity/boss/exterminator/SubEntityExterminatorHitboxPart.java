package team.cqr.cqrepoured.entity.boss.exterminator;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import team.cqr.cqrepoured.entity.multipart.MultiPartEntityPartSizable;

public class SubEntityExterminatorHitboxPart extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	public SubEntityExterminatorHitboxPart(EntityCQRExterminator parent, String partName, float width, float height) {
		super(parent, partName, width, height);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.getParent() == null || !((LivingEntity) this.getParent()).isAlive()) {
			return InteractionResult.FAIL;
		}
		return ((LivingEntity) this.getParent()).interact(player, hand);
	}

}
