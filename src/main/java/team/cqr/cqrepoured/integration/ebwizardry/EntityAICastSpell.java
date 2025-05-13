package team.cqr.cqrepoured.integration.ebwizardry;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.event.SpellCastEvent.Source;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.packet.PacketNPCCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WandHelper;
import it.unimi.dsi.fastutil.ints.IntArrays;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAICastSpell extends AbstractCQREntityAI<AbstractEntityCQR> {

	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;
	private int useTick = -1;
	private SpellModifiers modifiers;

	public EntityAICastSpell(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	protected boolean hasSpell() {
		Spell[] spells = WandHelper.getSpells(this.entity.getHeldItemMainhand());
		if (spells.length == 0) {
			return false;
		}
		return Arrays.stream(spells).anyMatch(spell -> spell != null && spell != Spells.none);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.hasSpell()) {
			return false;
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(attackTarget);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.hasSpell()) {
			return false;
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		return this.entity.getLastTimeSeenAttackTarget() + 100 >= this.entity.ticksExisted;
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void resetTask() {
		if (this.useTick >= 0) {
			ItemStack stack = this.entity.getHeldItemMainhand();
			Spell spell = WandHelper.getCurrentSpell(stack);
			int chargeup = (int) (spell.getChargeup() * this.modifiers.get("chargeup"));
			int castingTick = this.useTick - chargeup;
			if (spell.isContinuous && this.useTick >= chargeup) {
				MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Finish(Source.NPC, spell, this.entity, this.modifiers, castingTick));
				IMessage msg = new PacketNPCCastSpell.Message(this.entity.getEntityId(), -1, EnumHand.MAIN_HAND, Spells.none, new SpellModifiers());
				WizardryPacketHandler.net.sendToAllTracking(msg, this.entity);
				// TODO set cooldown
			}
		}
		this.useTick = -1;
		this.entity.stopActiveHand();
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return;
		}
		double distanceSq = this.entity.getDistanceSq(attackTarget);
		double attackRangeSq = CQRConfig.wizardry.range * CQRConfig.wizardry.range;

		if (this.entity.getEntitySenses().canSee(attackTarget) && (distanceSq < attackRangeSq * 0.9D * 0.9D || (distanceSq < attackRangeSq && !this.entity.hasPath()))) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
			this.checkAndPerformAttack(attackTarget);
			this.entity.getNavigator().clearPath();
			this.strafingTime++;
		} else {
			this.entity.getNavigator().tryMoveToEntityLiving(attackTarget, 1.0D);
			this.strafingTime = -1;
		}

		if (this.strafingTime >= 20) {
			if (this.random.nextDouble() < 0.3D) {
				this.strafingClockwise = !this.strafingClockwise;
			}

			if (this.random.nextDouble() < 0.3D) {
				this.strafingBackwards = !this.strafingBackwards;
			}

			this.strafingTime = 0;
		}

		if (this.canStrafe() && this.strafingTime > -1) {
			if (distanceSq > attackRangeSq * 0.75D * 0.75D) {
				this.strafingBackwards = false;
			} else if (distanceSq < attackRangeSq * 0.25D * 0.25D) {
				this.strafingBackwards = true;
			}

			float f = this.getStrafingSpeed();
			this.entity.getMoveHelper().strafe(this.strafingBackwards ? -f : f, this.strafingClockwise ? f : -f);
		}
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		ItemStack stack = this.entity.getHeldItemMainhand();

		if (!this.isCasting() && stack.getItem() instanceof ItemWand) {
			Spell[] spells = WandHelper.getSpells(stack);
			int[] cooldowns = WandHelper.getCooldowns(stack);
			for (int i : IntArrays.shuffle(IntStream.range(0, spells.length).toArray(), this.random)) {
				Spell spell = spells[i];
				if (spell == null) continue;
				if (spell == Spells.none) continue;
				if (spell.getTier().level > ((ItemWand) stack.getItem()).tier.level) continue;
				if (cooldowns[i] > 0) continue;
				SpellModifiers modifiers = calculateModifiers(stack, this.entity, spell);
				if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(Source.NPC, spell, this.entity, modifiers))) continue;

				WandHelper.selectSpell(stack, i);
				int chargeup = (int) (spell.getChargeup() * modifiers.get("chargeup"));
				if (!spell.isContinuous && chargeup <= 0) {
					cast(stack, spell, this.entity, EnumHand.MAIN_HAND, 0, attackTarget, modifiers);
					setCooldown(stack, spell, this.entity, modifiers, CQRConfig.wizardry.minCooldown, CQRConfig.wizardry.maxCooldown);
				} else {
					this.startCasting(modifiers);
				}
				break;
			}
		}
		if (this.isCasting()) {
			Spell spell = WandHelper.getCurrentSpell(stack);
			int chargeup = (int) (spell.getChargeup() * modifiers.get("chargeup"));
			if (this.useTick >= chargeup) {
				if (!spell.isContinuous) {
					cast(stack, spell, this.entity, EnumHand.MAIN_HAND, 0, attackTarget, modifiers);
					setCooldown(stack, spell, this.entity, modifiers, CQRConfig.wizardry.minCooldown, CQRConfig.wizardry.maxCooldown);
					this.stopCasting();
				} else {
					int castingTick = this.useTick - chargeup;
					if (castingTick == 0) {
						cast(stack, spell, this.entity, EnumHand.MAIN_HAND, castingTick, attackTarget, modifiers);
					} else {
						boolean canContinue;
						if (canContinue = !MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Tick(Source.NPC, spell, this.entity, modifiers, castingTick))) {
							cast(stack, spell, this.entity, EnumHand.MAIN_HAND, castingTick, attackTarget, modifiers);
						}
						if (!canContinue || castingTick >= CQRConfig.wizardry.continuousDuration - 1) {
							MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Finish(Source.NPC, spell, this.entity, modifiers, castingTick));
							IMessage msg = new PacketNPCCastSpell.Message(this.entity.getEntityId(), -1, EnumHand.MAIN_HAND, Spells.none, new SpellModifiers());
							WizardryPacketHandler.net.sendToAllTracking(msg, this.entity);
							setCooldown(stack, spell, this.entity, modifiers, CQRConfig.wizardry.minCooldown, CQRConfig.wizardry.maxCooldown);
							this.stopCasting();
						}
					}
				}
			}
		}
		if (this.isCasting()) {
			this.useTick++;
		}
	}

	private boolean isCasting() {
		return this.useTick >= 0;
	}

	private void startCasting(SpellModifiers modifiers) {
		this.useTick = 0;
		this.modifiers = modifiers;
		this.entity.setActiveHand(EnumHand.MAIN_HAND);
	}

	private void stopCasting() {
		this.useTick = -1;
		this.modifiers = null;
		this.entity.stopActiveHand();
	}

	public static boolean cast(ItemStack stack, Spell spell, AbstractEntityCQR caster, EnumHand hand, int castingTick, EntityLivingBase target, SpellModifiers modifiers) {
		if (!spell.cast(caster.world, caster, hand, castingTick, target, modifiers)) {
			return false;
		}

		if (castingTick == 0) {
			MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(Source.NPC, spell, caster, modifiers));

			if (spell.isContinuous || spell.requiresPacket()) {
				IMessage msg = new PacketNPCCastSpell.Message(caster.getEntityId(), target.getEntityId(), hand, spell, modifiers);
				WizardryPacketHandler.net.sendToAllTracking(msg, caster);
			}
		}

		return true;
	}

	public static void setCooldown(ItemStack stack, Spell spell, AbstractEntityCQR caster, SpellModifiers modifiers, int minCooldown, int maxCooldown) {
		WandHelper.setCurrentCooldown(stack, Math.min((int) (spell.getCooldown() * modifiers.get(WizardryItems.cooldown_upgrade)), maxCooldown));
		int[] cooldowns = WandHelper.getCooldowns(stack);
		for (int i = 0; i < cooldowns.length; i++) {
			cooldowns[i] = Math.max(cooldowns[i], minCooldown);
		}
		WandHelper.selectSpell(stack, caster.getRNG().nextInt(WandHelper.getSpells(stack).length));
	}

	/**
	 * Based on {@link ItemWand#calculateModifiers(ItemStack, EntityPlayer, Spell)}
	 */
	public static SpellModifiers calculateModifiers(ItemStack stack, EntityLivingBase entity, Spell spell) {
		SpellModifiers modifiers = new SpellModifiers();

		int level = WandHelper.getUpgradeLevel(stack, WizardryItems.range_upgrade);
		if (level > 0) {
			modifiers.set(WizardryItems.range_upgrade, 1.0F + level * Constants.RANGE_INCREASE_PER_LEVEL, true);
		}

		level = WandHelper.getUpgradeLevel(stack, WizardryItems.duration_upgrade);
		if (level > 0) {
			modifiers.set(WizardryItems.duration_upgrade, 1.0F + level * Constants.DURATION_INCREASE_PER_LEVEL, false);
		}

		level = WandHelper.getUpgradeLevel(stack, WizardryItems.blast_upgrade);
		if (level > 0) {
			modifiers.set(WizardryItems.blast_upgrade, 1.0F + level * Constants.BLAST_RADIUS_INCREASE_PER_LEVEL, true);
		}

		level = WandHelper.getUpgradeLevel(stack, WizardryItems.cooldown_upgrade);
		if (level > 0) {
			modifiers.set(WizardryItems.cooldown_upgrade, 1.0F - level * Constants.COOLDOWN_REDUCTION_PER_LEVEL, true);
		}

		return modifiers;
	}

}
