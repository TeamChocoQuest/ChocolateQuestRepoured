package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySummoningCircle extends Entity implements IEntityAdditionalSpawnData {

	protected static final int BORDER_WHEN_TO_SPAWN_IN_TICKS = 60;

	protected ResourceLocation entityToSpawn;
	protected float timeMultiplierForSummon;
	protected ECircleTexture texture;
	protected ISummoner summoner;
	protected EntityLivingBase summonerLiving;
	protected Vec3d velForSummon = null;

	public enum ECircleTexture {
		ZOMBIE(), SKELETON(), FLYING_SKULL(), FLYING_SWORD(), METEOR();

		static {
			values();
		}
	}

	public EntitySummoningCircle(World worldIn) {
		this(worldIn, new ResourceLocation("minecraft", "zombie"), 1F, ECircleTexture.ZOMBIE, null);
	}

	public EntitySummoningCircle(World worldIn, ResourceLocation entityToSpawn, float timeMultiplier, ECircleTexture texture, ISummoner summoner) {
		this(worldIn, entityToSpawn, timeMultiplier, texture, summoner, null);
	}

	public EntitySummoningCircle(World worldIn, ResourceLocation entityToSpawn, float timeMultiplier, ECircleTexture texture, ISummoner isummoner, EntityLivingBase summoner) {
		super(worldIn);
		this.setSize(2.0F, 0.005F);
		this.entityToSpawn = entityToSpawn;
		this.timeMultiplierForSummon = timeMultiplier;
		this.texture = texture;
		this.summoner = isummoner;
		this.summonerLiving = summoner;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) {
			if (this.ticksExisted >= EntitySummoningCircle.BORDER_WHEN_TO_SPAWN_IN_TICKS * this.timeMultiplierForSummon) {
				Entity summon = EntityList.createEntityByIDFromName(this.entityToSpawn, this.world);

				if (summon != null) {
					summon.setPosition(this.posX, this.posY + 0.5D, this.posZ);

					if (this.velForSummon != null) {
						summon.motionX = this.velForSummon.x;
						summon.motionY = this.velForSummon.y;
						summon.motionZ = this.velForSummon.z;
						summon.velocityChanged = true;
					}

					this.world.spawnEntity(summon);

					if (this.summonerLiving != null && summon instanceof AbstractEntityCQR) {
						((AbstractEntityCQR) summon).setLeader(this.summonerLiving);
						CQRFaction faction = FactionRegistry.instance().getFactionOf(this.summonerLiving);
						if (faction != null) {
							((AbstractEntityCQR) summon).setFaction(faction.getName());
						}
					}

					if (this.summoner != null && !this.summoner.getSummoner().isDead) {
						this.summoner.setSummonedEntityFaction(summon);
						this.summoner.addSummonedEntityToList(summon);
					}
				}

				this.setDead();
			}
		} else {
			if (this.ticksExisted >= EntitySummoningCircle.BORDER_WHEN_TO_SPAWN_IN_TICKS * this.timeMultiplierForSummon * 0.8F) {
				for (int i = 0; i < 4; i++) {
					this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble());
				}

				if (!this.world.isRemote) {
					CQRFaction faction = this.summoner != null ? this.summoner.getSummonerFaction() : null;
					for (Entity ent : this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(getPosition().add(this.width / 2, 0, this.width / 2), getPosition().add(-this.width / 2, 3, -this.width / 2)), faction != null ? TargetUtil
							.createPredicateNonAlly(faction) : TargetUtil.PREDICATE_LIVING)) {
						if (ent != null && ent.isEntityAlive() && ent instanceof EntityLivingBase) {
							((EntityLivingBase) ent).addPotionEffect(new PotionEffect(MobEffects.WITHER, 80, 0));
						}
					}
				}
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, 1.0F, 0.0F, 0.0F, 20);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, 0.5F, 0.0F, 0.5F, 1);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, 0.5F, 0.0F, -0.5F, 1);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, -0.5F, 0.0F, 0.5F, 1);
				// this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX, this.posY + 0.02D, this.posZ, -0.5F, 0.0F, -0.5F, 1);
			}
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.timeMultiplierForSummon = compound.getFloat("cqrdata.timeMultiplier");
		String resD = compound.getString("cqrdata.entityToSpawn.Domain");
		String resP = compound.getString("cqrdata.entityToSpawn.Path");
		this.entityToSpawn = new ResourceLocation(resD, resP);
		this.ticksExisted = compound.getInteger("ticksExisted");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setFloat("cqrdata.timeMultiplier", this.timeMultiplierForSummon);
		compound.setString("cqrdata.entityToSpawn.Domain", this.entityToSpawn.getNamespace());
		compound.setString("cqrdata.entityToSpawn.Path", this.entityToSpawn.getPath());
		compound.setInteger("ticksExisted", this.ticksExisted);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeByte(this.texture.ordinal());
		buffer.writeFloat(this.timeMultiplierForSummon);
		buffer.writeInt(this.ticksExisted);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.texture = ECircleTexture.values()[additionalData.readByte()];
		this.timeMultiplierForSummon = additionalData.readFloat();
		this.ticksExisted = additionalData.readInt();
	}

	@SideOnly(Side.CLIENT)
	public int getTextureID() {
		return this.texture.ordinal();
	}

	public void setVelocityForSummon(Vec3d v) {
		this.velForSummon = v;
	}

	public void setSummon(ResourceLocation summon) {
		if (summon != null) {
			this.entityToSpawn = summon;
		}
	}

}
