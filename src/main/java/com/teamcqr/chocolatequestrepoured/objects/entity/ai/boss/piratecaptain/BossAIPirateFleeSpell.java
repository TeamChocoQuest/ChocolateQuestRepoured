package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class BossAIPirateFleeSpell extends AbstractEntityAISpell<EntityCQRPirateCaptain> implements IEntityAISpellAnimatedVanilla {

	protected final Predicate<EntityLiving> predicateAlly = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return BossAIPirateFleeSpell.this.isSuitableAlly(input);
	};
	
	public BossAIPirateFleeSpell(EntityCQRPirateCaptain entity, boolean needsTargetToStart, boolean needsTargetToContinue, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, needsTargetToStart, needsTargetToContinue, cooldown, chargingTicks, castingTicks);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute() && entity.getHealingPotions() <= 1 && entity.getHealth() / entity.getMaxHealth() <= 0.2) {
			return hasNearbyAllies();
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && hasNearbyAllies();
	}
	
	private boolean hasNearbyAllies() {
		Vec3d vec = new Vec3d(CQRConfig.bosses.pirateCaptainFleeCheckRadius, 0.5 * CQRConfig.bosses.pirateCaptainFleeCheckRadius, CQRConfig.bosses.pirateCaptainFleeCheckRadius);
		AxisAlignedBB aabb = new AxisAlignedBB(entity.getPositionVector().add(vec), entity.getPositionVector().subtract(vec));
		
		List<EntityLiving> allies = this.entity.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly);
		return !allies.isEmpty();
	}
	
	private int getNearbyAllies(EntityLiving o1) {
		Vec3d vec = new Vec3d(4,2,4);
		AxisAlignedBB aabb = new AxisAlignedBB(o1.getPositionVector().add(vec), o1.getPositionVector().subtract(vec));
		return o1.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly).size();
	}

	@Override
	public void castSpell() {
		super.castSpell();
		
		Vec3d vec = new Vec3d(CQRConfig.bosses.pirateCaptainFleeCheckRadius, 0.5 * CQRConfig.bosses.pirateCaptainFleeCheckRadius, CQRConfig.bosses.pirateCaptainFleeCheckRadius);
		AxisAlignedBB aabb = new AxisAlignedBB(entity.getPositionVector().add(vec), entity.getPositionVector().subtract(vec));
		
		List<EntityLiving> allies = this.entity.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly);
		allies.sort(new Comparator<EntityLiving>() {

			@Override
			public int compare(EntityLiving o1, EntityLiving o2) {
				int entCount1 = getNearbyAllies(o1);
				int entCount2 = getNearbyAllies(o2);
				return entCount2 - entCount1;
			}

		});
		Vec3d p = allies.get(0).getPositionVector();
		entity.attemptTeleport(p.x, p.y, p.z);
	}
	
	
	@Override
	public int getWeight() {
		return 2;
	}

	@Override
	public boolean ignoreWeight() {
		return true;
	}

	@Override
	public float getRed() {
		return 0;
	}

	@Override
	public float getGreen() {
		return 1;
	}

	@Override
	public float getBlue() {
		return 0;
	}
	
	protected boolean isSuitableAlly(EntityLiving possibleAlly) {
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!this.entity.getFaction().isAlly(possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigator().getPathToEntityLiving(this.entity);
		return path != null && path.getCurrentPathLength() <= 20;
	}

}
