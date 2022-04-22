package team.cqr.cqrepoured.entity.boss.exterminator;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.MultiPartEntityPartSizable;

public class SubEntityExterminatorHitboxPart extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	public SubEntityExterminatorHitboxPart(EntityCQRExterminator parent, String partName, float width, float height) {
		super(parent, partName, width, height);
	}
	
	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (this.getParent() == null || !((LivingEntity) this.getParent()).isAlive()) {
			return ActionResultType.FAIL;
		}
		return ((LivingEntity) this.getParent()).interact(player, hand);
	}

	@Override
	protected Class<? extends CQRPartEntity<?>> getClassForRenderer() {
		return SubEntityExterminatorHitboxPart.class;
	}

}
