package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerKingIllusion;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAIWalkerIllusions extends AbstractEntityAISpell implements IEntityAISpellAnimatedVanilla {

	public EntityAIWalkerIllusions(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, cooldown, chargeUpTicks, 1);
	}

	@Override
	protected void chargeUpSpell() {
		if (this.tick == 0) {
			this.entity.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
		}
	}

	@Override
	protected void castSpell() {
		if (this.tick == this.chargeUpTicks) {
			this.entity.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
		}
		//entity.getAttackTarget().addPotionEffect(new PotionEffect(Potion.getPotionById(15), 40));
		entity.world.getEntitiesInAABBexcluding(entity, new AxisAlignedBB(entity.getPosition().add(-20,-10,-20), entity.getPosition().add(20,10,20)), TargetUtil.createPredicateNonAlly(entity.getFaction())).forEach(new Consumer<Entity>() {

			@Override
			public void accept(Entity t) {
				if(t instanceof EntityLivingBase) {
					((EntityLivingBase) t).addPotionEffect(new PotionEffect(Potion.getPotionById(15), 40));
				}
			}
		});
		Vec3d v = new Vec3d(2.5, 0, 0);
		for(int i = 0; i < 3; i++) {
			Vec3d pos = entity.getPositionVector().add(VectorUtil.rotateVectorAroundY(v, 120 * i));
			EntityWalkerKingIllusion illusion = new EntityWalkerKingIllusion(1200, (EntityCQRWalkerKing) entity, entity.getEntityWorld());
			illusion.setPosition(pos.x, pos.y, pos.z);
			entity.world.spawnEntity(illusion);
		}
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.55F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 0.8F;
	}

}
