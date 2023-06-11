package team.cqr.cqrepoured.entity.ai.boss.spectrelord;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntitySpectreLordCurse;
import team.cqr.cqrepoured.faction.Faction;

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
				if (curse.isAlive()) {
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
			curse.remove();
		}
		this.curses.clear();
		this.healthLost = 0.0F;
		this.lastHealth = 0.0F;
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		this.lastHealth = this.entity.getHealth() / this.entity.getMaxHealth();

		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.getX() - 32.0D, this.entity.getY() - 8.0D, this.entity.getZ() - 32.0D, this.entity.getX() + 32.0D, this.entity.getY() + this.entity.getBbHeight() + 8.0D, this.entity.getZ() + 32.0D);
		Faction faction = this.entity.getFaction();
		List<LivingEntity> list = this.world.getEntitiesOfClass(LivingEntity.class, aabb, e -> TargetUtil.PREDICATE_ATTACK_TARGET.apply(e) && (faction == null || !faction.isAlly(e)));
		list.sort((e1, e2) -> {
			if (faction != null) {
				boolean flag1 = faction.isEnemy(e1);
				boolean flag2 = faction.isEnemy(e2);
				if (flag1 && !flag2) {
					return -1;
				}
				if (!flag1 && flag2) {
					return 1;
				}
			}
			boolean flag1 = e1 instanceof PlayerEntity;
			boolean flag2 = e2 instanceof PlayerEntity;
			if (flag1 && !flag2) {
				return -1;
			}
			if (!flag1 && flag2) {
				return 1;
			}
			double d1 = this.entity.distanceToSqr(e1);
			double d2 = this.entity.distanceToSqr(e2);
			if (d1 < d2) {
				return -1;
			}
			if (d1 > d2) {
				return 1;
			}
			return 0;
		});
		for (int i = 0; i < 8 && i < list.size(); i++) {
			LivingEntity e = list.get(i);
			EntitySpectreLordCurse curse = new EntitySpectreLordCurse(this.world, this.entity, e);
			curse.setPos(this.entity.getX(), this.entity.getY(), this.entity.getZ());
			this.world.addFreshEntity(curse);
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
			this.entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 200, 1, false, true));
			this.entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 200, 1, false, true));
			AxisAlignedBB aabb = new AxisAlignedBB(this.entity.getX() - 32.0D, this.entity.getY() - 8.0D, this.entity.getZ() - 32.0D, this.entity.getX() + 32.0D, this.entity.getY() + this.entity.getBbHeight() + 8.0D, this.entity.getZ() + 32.0D);
			Faction faction = this.entity.getFaction();
			for (LivingEntity e : this.world.getEntitiesOfClass(LivingEntity.class, aabb, e -> TargetUtil.PREDICATE_ATTACK_TARGET.apply(e) && (faction == null || !faction.isAlly(e)))) {
				e.hurt(DamageSource.mobAttack(this.entity).bypassArmor(), 4.0F);
				e.addEffect(new EffectInstance(Effects.WEAKNESS, 200, 1, false, true));
			}
			this.entity.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
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
