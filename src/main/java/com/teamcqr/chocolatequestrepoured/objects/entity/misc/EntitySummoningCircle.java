package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySummoningCircle extends Entity {

	protected ResourceLocation entityToSpawn;
	protected float timeMultiplierForSummon = 1F;
	protected ECircleTexture texture = ECircleTexture.METEOR;
	
	protected static final DataParameter<Boolean> IS_SPAWNING_PARTICLES = EntityDataManager.<Boolean>createKey(EntitySummoningCircle.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Float> ANIMATION_PROGRESS = EntityDataManager.<Float>createKey(EntitySummoningCircle.class, DataSerializers.FLOAT);
	protected static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.<Integer>createKey(EntitySummoningCircle.class, DataSerializers.VARINT);
	
	public enum ECircleTexture {
		ZOMBIE(0),
		SKELETON(1),
		FLYING_SKULL(2),
		FLYING_SWORD(3),
		METEOR(4),
		;
		
		private int textureID = 0;
		
		public int getTextureID() {
			return this.textureID;
		}
		
		private ECircleTexture(int textureID) {
			this.textureID = textureID;
		}
	}
	
	public EntitySummoningCircle(World worldIn) {
		this(worldIn, new ResourceLocation("minecraft:skeleton"), 1F, ECircleTexture.SKELETON);
	}
	
	public EntitySummoningCircle(World worldIn, ResourceLocation entityToSpawn, float timeMultiplier, ECircleTexture textre) {
		super(worldIn);
		this.entityToSpawn = entityToSpawn;
		this.timeMultiplierForSummon = timeMultiplier;
		this.texture = textre;
		this.dataManager.set(TEXTURE_INDEX, this.texture.getTextureID());
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(IS_SPAWNING_PARTICLES, false);
		this.dataManager.register(ANIMATION_PROGRESS, 0F);
		this.dataManager.register(TEXTURE_INDEX, this.texture != null ? this.texture.getTextureID() : 1);
	}
	
	@SideOnly(Side.CLIENT)
	public int getTextureID() {
		return this.dataManager.get(TEXTURE_INDEX);
	}
	
	public boolean isSpawningParticles() {
		return this.dataManager.get(IS_SPAWNING_PARTICLES);
	}
	
	public float getAnimationProgress() {
		return this.dataManager.get(ANIMATION_PROGRESS);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		this.dataManager.set(IS_SPAWNING_PARTICLES, compound.getBoolean("cqrdata.isSpawningParticles"));
		this.dataManager.set(ANIMATION_PROGRESS, compound.getFloat("cqrdata.animationProgress"));
		this.dataManager.set(TEXTURE_INDEX, compound.getInteger("cqrdata.textureID"));
		
		this.timeMultiplierForSummon = compound.getFloat("cqrdata.timeMultiplier");
		this.entityToSpawn = new ResourceLocation(compound.getString("cqrdata.entityToSpawn"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setBoolean("cqrdata.isSpawningParticles", this.dataManager.get(IS_SPAWNING_PARTICLES));
		compound.setFloat("cqrdata.animationProgress", this.dataManager.get(ANIMATION_PROGRESS));
		compound.setFloat("cqrdata.timeMultiplier", this.timeMultiplierForSummon);
		compound.setInteger("cqrdata.textureID", this.dataManager.get(TEXTURE_INDEX));
		compound.setString("cqrdata.entityToSpawn", entityToSpawn.getResourceDomain() + ":" + entityToSpawn.getResourcePath());
	}

}
