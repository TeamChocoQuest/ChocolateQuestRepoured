package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIAntiAirSpellWalker;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQRWalker extends AbstractEntityCQR {

	public EntityCQRWalker(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.AbyssWalker;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.WALKERS;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.WALKER_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.WALKER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.WALKER_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_WALKER;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof SpectralArrowEntity) {
			amount *= 2;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.spellHandler.addSpell(0, new EntityAIAntiAirSpellWalker(this));
	}

	@Override
	public void thunderHit(ServerWorld serverLevel,LightningBoltEntity lightningBolt) {
		this.heal(this.getMaxHealth() * 0.025F);
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

}
