package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.ai.spells.EntityAIAntiAirSpellWalker;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

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
		if (source.getImmediateSource() instanceof EntitySpectralArrow) {
			amount *= 2;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.spellHandler.addSpell(0, new EntityAIAntiAirSpellWalker(this));
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		this.heal(new Float(this.getMaxHealth() * 0.025));
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return CQRCreatureAttributes.CREATURE_TYPE_ABYSS_WALKER;
	}

}
