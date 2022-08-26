package team.cqr.cqrepoured.entity.boss.exterminator;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;

public class SubEntityExterminatorBackpack extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private Supplier<Boolean> funcGetAnyEmitterActive;
	private EntityCQRExterminator exterminator;
	
	public SubEntityExterminatorBackpack(EntityCQRExterminator parent, String partName, Supplier<Boolean> funcGetAnyEmitterActive) {
		super(parent, partName, 1.0F, 1.0F);
		this.exterminator = parent;
		this.funcGetAnyEmitterActive = funcGetAnyEmitterActive;
	}

	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
	public void kill() {
		this.exterminator.kill();
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		// If at least one emitter emits electricity, we are vulnerable!!
		if (source == DamageSource.DROWN) {
			if (this.funcGetAnyEmitterActive.get()) {
				this.exterminator.setStunned(true, 100);
			}
		}

		if (source == DamageSource.LIGHTNING_BOLT) {
			this.exterminator.setStunned(true, 75);
		}

		if (!this.exterminator.isStunned() && this.funcGetAnyEmitterActive.get() && !TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this.exterminator)) {
			return super.hurt(DamageSource.DROWN, amount /= 2);
		}

		return super.hurt(source, amount * 2);
	}

	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (this.exterminator == null || !this.exterminator.isAlive()) {
			return ActionResultType.FAIL;
		}
		return this.exterminator.interact(player, hand);
	}

	@Override
	protected Class<? extends CQRPartEntity<? extends Entity>> getClassForRenderer() {
		return SubEntityExterminatorBackpack.class;
	}

}
