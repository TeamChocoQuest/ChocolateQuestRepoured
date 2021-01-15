package team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntitySpectreLordIllusion;

public class EntityAISpectreLordIllusionHeal extends AbstractEntityAISpell<EntitySpectreLordIllusion> implements IEntityAISpellAnimatedVanilla {

	private EntityLivingBase target;

	public EntityAISpectreLordIllusionHeal(EntitySpectreLordIllusion entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 0);
		this.setup(false, false, false, false);
	}

	@Override
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		}
		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.posX - 24.0D, this.entity.posY - 2.0D, this.entity.posZ - 24.0D, this.entity.posX + 24.0D, this.entity.posY + this.entity.height + 2.0D, this.entity.posZ + 24.0D);
		CQRFaction faction = this.entity.getFaction();
		List<EntityLivingBase> alliesToHeal = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> {
			if (e == this.entity) {
				return false;
			}
			if (e.getHealth() / e.getMaxHealth() > 0.9F) {
				return false;
			}
			if (!faction.isAlly(e)) {
				return false;
			}
			return this.entity.canEntityBeSeen(e);
		});
		return !alliesToHeal.isEmpty();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.target = null;
	}

	@Override
	public void startChargingSpell() {
		super.startChargingSpell();
		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.posX - 24.0D, this.entity.posY - 2.0D, this.entity.posZ - 24.0D, this.entity.posX + 24.0D, this.entity.posY + this.entity.height + 2.0D, this.entity.posZ + 24.0D);
		CQRFaction faction = this.entity.getFaction();
		List<EntityLivingBase> alliesToHeal = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> {
			if (e == this.entity) {
				return false;
			}
			if (e.getHealth() / e.getMaxHealth() > 0.9F) {
				return false;
			}
			if (!faction.isAlly(e)) {
				return false;
			}
			return this.entity.canEntityBeSeen(e);
		});
		this.target = TargetUtil.getNearestEntity(this.entity, alliesToHeal);
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		this.target.heal(10.0F);
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
		return 0.6F;
	}

	@Override
	public float getGreen() {
		return 0.6F;
	}

	@Override
	public float getBlue() {
		return 0.35F;
	}

}
