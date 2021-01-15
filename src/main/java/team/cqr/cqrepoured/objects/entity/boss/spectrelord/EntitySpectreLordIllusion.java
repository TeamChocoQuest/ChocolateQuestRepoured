package team.cqr.cqrepoured.objects.entity.boss.spectrelord;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord.EntityAISpectreLordIllusionExplosion;
import team.cqr.cqrepoured.objects.entity.ai.boss.spectrelord.EntityAISpectreLordIllusionHeal;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRSpectre;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntitySpectreLordIllusion extends EntityCQRSpectre {

	private EntityLivingBase caster;
	private int lifeTime;
	private boolean canCastHeal;
	private boolean canCastExplosion;

	public EntitySpectreLordIllusion(World worldIn) {
		this(worldIn, null, 200, false, false);
	}

	public EntitySpectreLordIllusion(World worldIn, EntityLivingBase caster, int lifeTime, boolean canCastHeal, boolean canCastExplosion) {
		super(worldIn);
		if (caster != null) {
			this.caster = caster;
			CQRFaction faction = FactionRegistry.instance().getFactionOf(caster);
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
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		super.onInitialSpawn(difficulty, livingdata);
		if (this.rand.nextDouble() < 0.3D) {
			switch (this.rand.nextInt(3)) {
			case 0:
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.SPEAR_IRON));
				break;
			case 1:
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.DAGGER_IRON));
				break;
			case 2:
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.GREAT_SWORD_IRON));
				break;
			}
		} else {
			this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
		}
		this.setHealingPotions(0);
		return livingdata;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(this.canCastHeal || this.canCastExplosion ? 8.0D : 12.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(this.canCastHeal || this.canCastExplosion ? 4.0D : 6.0D);
	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote && this.lifeTime-- <= 0) {
			this.setDead();
		}

		super.onEntityUpdate();
	}

	@Override
	public float getBaseHealth() {
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
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (this.caster != null && this.caster.isEntityAlive()) {
			compound.setUniqueId("Summoner", this.caster.getPersistentID());
		}
		compound.setInteger("lifeTime", this.lifeTime);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("SummonerMost", Constants.NBT.TAG_LONG)) {
			UUID uuid = compound.getUniqueId("Summoner");
			Entity e = EntityUtil.getEntityByUUID(this.world, uuid);
			if (e instanceof EntityCQRSpectreLord) {
				this.caster = (EntityCQRSpectreLord) e;
			}
		}
		this.lifeTime = compound.getInteger("lifeTime");
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (this.caster != null) {
			this.caster.attackEntityFrom(DamageSource.causeMobDamage(this).setDamageBypassesArmor(), this.caster.getMaxHealth() * 0.025F);
		}
	}

}
