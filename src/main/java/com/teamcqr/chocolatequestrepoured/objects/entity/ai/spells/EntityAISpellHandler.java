package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;

public class EntityAISpellHandler extends AbstractCQREntityAI {

	private static final Comparator<EntityAISpellHandler.SpellEntry> SORTER = (spellEntry1, spellEntry2) -> {
		if (spellEntry1.priority < spellEntry2.priority) {
			return -1;
		}
		if (spellEntry1.priority > spellEntry2.priority) {
			return 1;
		}
		return 0;
	};
	private final List<EntityAISpellHandler.SpellEntry> spells = new ArrayList<>();
	private final int cooldown;
	private IEntityAISpell activeSpell;
	private int prevTimeCasted;

	public EntityAISpellHandler(AbstractEntityCQR entity, int cooldown) {
		super(entity);
		this.cooldown = cooldown;
		this.setMutexBits(7);
	}

	public void addSpell(int priority, IEntityAISpell spell) {
		this.spells.add(new SpellEntry(priority, spell));
		if (this.spells.size() >= 2 && this.spells.get(this.spells.size() - 2).priority > priority) {
			this.spells.sort(EntityAISpellHandler.SORTER);
		}
	}

	@Override
	public boolean shouldExecute() {
		if (this.spells.isEmpty()) {
			return false;
		}
		if (this.prevTimeCasted + this.cooldown >= this.entity.ticksExisted) {
			return false;
		}
		int sum = 0;
		List<IEntityAISpell> possibleSpells = new ArrayList<>();
		for (EntityAISpellHandler.SpellEntry spellEntry : this.spells) {
			IEntityAISpell spell = spellEntry.spell;
			if (spell.shouldExecute()) {
				if (spell.ignoreWeight()) {
					this.activeSpell = spell;
					return true;
				}
				if (spell.getWeight() > 0) {
					sum += spell.getWeight();
					possibleSpells.add(spell);
				}
			}
		}
		if (possibleSpells.isEmpty()) {
			return false;
		}
		int i = this.random.nextInt(sum);
		for (IEntityAISpell spell : possibleSpells) {
			i -= spell.getWeight();
			if (i < 0) {
				this.activeSpell = spell;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.activeSpell.shouldContinueExecuting();
	}

	@Override
	public boolean isInterruptible() {
		return this.activeSpell.isInterruptible();
	}

	@Override
	public void startExecuting() {
		this.activeSpell.startExecuting();
	}

	@Override
	public void resetTask() {
		this.activeSpell.resetTask();
		this.activeSpell = null;
		this.prevTimeCasted = this.entity.ticksExisted;
	}

	@Override
	public void updateTask() {
		this.activeSpell.updateTask();
		if (this.activeSpell instanceof IEntityAISpellAnimatedVanilla) {
			this.spawnAnimatedSpellParticles((IEntityAISpellAnimatedVanilla) this.activeSpell);
		}
	}

	private void spawnAnimatedSpellParticles(IEntityAISpellAnimatedVanilla animatedSpell) {
		double red = animatedSpell.getRed();
		double green = animatedSpell.getGreen();
		double blue = animatedSpell.getBlue();
		float f = this.entity.renderYawOffset * 0.017453292F + MathHelper.cos((float) this.entity.ticksExisted * 0.6662F) * 0.25F;
		float f1 = MathHelper.cos(f);
		float f2 = MathHelper.sin(f);
		((WorldServer) this.entity.world).spawnParticle(EnumParticleTypes.SPELL_MOB, this.entity.posX + (double) f1 * 0.6D, this.entity.posY + this.entity.height, this.entity.posZ + (double) f2 * 0.6D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		((WorldServer) this.entity.world).spawnParticle(EnumParticleTypes.SPELL_MOB, this.entity.posX - (double) f1 * 0.6D, this.entity.posY + this.entity.height, this.entity.posZ - (double) f2 * 0.6D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Nullable
	public IEntityAISpell getActiveSpell() {
		return this.activeSpell;
	}

	public boolean isSpellCharging() {
		return this.activeSpell != null && this.activeSpell.isCharging();
	}

	public boolean isSpellCasting() {
		return this.activeSpell != null && this.activeSpell.isCasting();
	}

	private static class SpellEntry {

		private int priority;
		private IEntityAISpell spell;

		private SpellEntry(int priority, IEntityAISpell spell) {
			this.priority = priority;
			this.spell = spell;
		}

	}

}
