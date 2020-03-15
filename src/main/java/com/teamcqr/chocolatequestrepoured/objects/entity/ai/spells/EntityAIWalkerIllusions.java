package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerKingIllusion;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class EntityAIWalkerIllusions extends AbstractEntityAIUseSpell {

	public EntityAIWalkerIllusions(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected void castSpell() {
		if(entity.getAttackTarget() != null) {
			entity.getAttackTarget().addPotionEffect(new PotionEffect(Potion.getPotionById(15), 40));
		}
		Vec3d v = new Vec3d(2.5, 0, 0);
		for(int i = 0; i < 3; i++) {
			Vec3d pos = entity.getPositionVector().add(VectorUtil.rotateVectorAroundY(v, 120 * i));
			EntityWalkerKingIllusion illusion = new EntityWalkerKingIllusion(1200, (EntityCQRWalkerKing) entity, entity.getEntityWorld());
			illusion.setPosition(pos.x, pos.y, pos.z);
			entity.world.spawnEntity(illusion);
		}
	}

	@Override
	protected int getCastingTime() {
		return 10;
	}

	@Override
	protected int getCastingInterval() {
		return 400;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.WALKER_ILLUSION;
	}

}
