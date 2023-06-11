package team.cqr.cqrepoured.entity.boss.spectrelord;

import java.util.UUID;

import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordIllusionExplosion;
import team.cqr.cqrepoured.entity.ai.boss.spectrelord.EntityAISpectreLordIllusionHeal;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSpectre;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntitySpectreLordIllusion extends EntityCQRSpectre {

	private LivingEntity caster;
	private int lifeTime;
	private boolean canCastHeal;
	private boolean canCastExplosion;

	public EntitySpectreLordIllusion(EntityType<? extends EntitySpectreLordIllusion> type, World worldIn) {
		this(type, worldIn, null, 200, false, false);
	}

	public EntitySpectreLordIllusion(World worldIn, LivingEntity caster, int lifeTime, boolean canCastHeal, boolean canCastExplosion) {
		this(CQREntityTypes.SPECTRE_LORD_ILLUSION.get(), worldIn, caster, lifeTime, canCastHeal, canCastExplosion);
	}
	
	public EntitySpectreLordIllusion(EntityType<? extends EntitySpectreLordIllusion> type, World worldIn, LivingEntity caster, int lifeTime, boolean canCastHeal, boolean canCastExplosion) {
		super(type, worldIn);
		if (caster != null) {
			this.caster = caster;
			Faction faction = FactionRegistry.instance(this).getFactionOf(caster);
			if (faction != null) {
				this.setFaction(faction.getName(), true);
			}
		}
		this.lifeTime = lifeTime;
		this.canCastHeal = canCastHeal;
		this.canCastExplosion = canCastExplosion;
		if (canCastHeal) {
			this.spellHandler.addSpell(0, new EntityAISpectreLordIllusionHeal(this, 160, 40));
		}
		if (canCastExplosion) {
			this.spellHandler.addSpell(0, new EntityAISpectreLordIllusionExplosion(this, 160, 40));
		}
	}

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, SpawnReason p_213386_3_, ILivingEntityData livingdata, CompoundNBT p_213386_5_) {
		ILivingEntityData ld = super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, livingdata, p_213386_5_);
		
		if (this.random.nextDouble() < 0.3D) {
			switch (this.random.nextInt(3)) {
			case 0:
				this.setItemInHand(Hand.MAIN_HAND, new ItemStack(CQRItems.SPEAR_IRON.get()));
				break;
			case 1:
				this.setItemInHand(Hand.MAIN_HAND, new ItemStack(CQRItems.DAGGER_IRON.get()));
				break;
			case 2:
				this.setItemInHand(Hand.MAIN_HAND, new ItemStack(CQRItems.GREAT_SWORD_IRON.get()));
				break;
			}
		} else {
			this.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
		}
		this.setHealingPotions(0);
		return ld;
	}
	
	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();
		
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.225D);
		this.getAttribute(Attributes.ARMOR).setBaseValue(this.canCastHeal || this.canCastExplosion ? 8.0D : 12.0D);
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(this.canCastHeal || this.canCastExplosion ? 4.0D : 6.0D);
	}
	
	@Override
	public void aiStep() {
		if (!this.level.isClientSide && this.lifeTime-- <= 0) {
			this.remove();
		}
		
		super.aiStep();
	}

	@Override
	public double getBaseHealth() {
		return 20.0F;
	}

	@Override
	public boolean canPutOutFire() {
		return false;
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}

	@Override
	public boolean canTameEntity() {
		return false;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	protected boolean shouldDropLoot() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		
		if (this.caster != null && this.caster.isAlive()) {
			
			compound.put("Summoner", NBTUtil.createUUID(this.caster.getUUID()));
		}
		compound.putInt("lifeTime", this.lifeTime);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		
		if (compound.contains("SummonerMost", Constants.NBT.TAG_LONG)) {
			UUID uuid = NBTUtil.loadUUID(compound.get("Summoner"));
			Entity e = EntityUtil.getEntityByUUID(this.level, uuid);
			if (e instanceof EntityCQRSpectreLord) {
				this.caster = (EntityCQRSpectreLord) e;
			}
		}
		this.lifeTime = compound.getInt("lifeTime");
	}
	
	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		
		if (this.caster != null) {
			this.caster.hurt(DamageSource.mobAttack(this).bypassArmor(), this.caster.getMaxHealth() * 0.025F);
		}
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
}
