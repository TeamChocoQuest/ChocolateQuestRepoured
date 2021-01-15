package team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntitySpectreLordCurse;

public class EntityAISpectreLordChannelHate extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private final List<EntitySpectreLordCurse> curses = new ArrayList<>();
	private float healthLost;
	private float lastHealth;

	public EntityAISpectreLordChannelHate(EntityCQRSpectreLord entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, false, false, false);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!super.shouldContinueExecuting()) {
			return false;
		}
		if (!this.isCasting()) {
			return true;
		}
		if (!this.curses.isEmpty()) {
			boolean flag = false;
			for (EntitySpectreLordCurse curse : this.curses) {
				if (curse.isEntityAlive()) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				return false;
			}
		}
		return this.healthLost < 0.15F;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		for (EntitySpectreLordCurse curse : this.curses) {
			curse.setDead();
		}
		this.curses.clear();
		this.healthLost = 0.0F;
		this.lastHealth = 0.0F;
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		this.lastHealth = this.entity.getHealth() / this.entity.getMaxHealth();

		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.posX - 32.0D, this.entity.posY - 8.0D, this.entity.posZ - 32.0D, this.entity.posX + 32.0D, this.entity.posY + this.entity.height + 8.0D, this.entity.posZ + 32.0D);
		CQRFaction faction = this.entity.getFaction();
		for (EntityLivingBase e : this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> faction == null || !faction.isAlly(e))) {
			EntitySpectreLordCurse curse = new EntitySpectreLordCurse(this.world, this.entity, e);
			curse.setPosition(this.entity.posX, this.entity.posY, this.entity.posZ);
			this.world.spawnEntity(curse);
			this.curses.add(curse);
		}
	}

	@Override
	public void castSpell() {
		super.castSpell();
		float f = this.entity.getHealth() / this.entity.getMaxHealth();
		this.healthLost += Math.max(this.lastHealth - f, 0.0F);
		this.lastHealth = f;

		if (this.tick == this.chargingTicks + this.castingTicks - 1) {
			this.entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 1, false, true));
			this.entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 1, false, true));
			AxisAlignedBB aabb = new AxisAlignedBB(this.entity.posX - 32.0D, this.entity.posY - 8.0D, this.entity.posZ - 32.0D, this.entity.posX + 32.0D, this.entity.posY + this.entity.height + 8.0D, this.entity.posZ + 32.0D);
			CQRFaction faction = this.entity.getFaction();
			for (EntityLivingBase e : this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> faction == null || !faction.isAlly(e))) {
				e.attackEntityFrom(DamageSource.causeMobDamage(this.entity).setDamageBypassesArmor(), 4.0F);
				e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1, false, true));
			}
			this.entity.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
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
		return 0.7F;
	}

	@Override
	public float getGreen() {
		return 0.15F;
	}

	@Override
	public float getBlue() {
		return 0.1F;
	}

}
