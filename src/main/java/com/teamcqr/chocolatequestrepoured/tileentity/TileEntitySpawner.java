package com.teamcqr.chocolatequestrepoured.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySpawner extends TileEntitySyncClient implements ITickable
{
	public ItemStackHandler inventory = new ItemStackHandler(9);
	private boolean spawnedInDungeon = false;
   
    @Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) 
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) 
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}
	
    @Override
    public void readFromNBT(NBTTagCompound compound) 
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        if(compound.hasKey("isDungeonSpawner")) {
        	spawnedInDungeon = compound.getBoolean("isDungeonSpawner");
        }
    }
 
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) 
    {
        super.writeToNBT(compound);
        compound.setTag("inventory", inventory.serializeNBT());
        if(spawnedInDungeon) {
        	compound.setBoolean("isDungeonSpawner", true);
        }
        return compound;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(I18n.format("tile.spawner.name"));
    }
 
    @Override
    public void update() 
    {
        if(!this.world.isRemote && this.isNonCreativePlayerInRange((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D, Reference.CONFIG_HELPER_INSTANCE.getSpawnerActivationDistance())) 
        {
            this.turnBackIntoEntity();
        }
    }
    
    public void turnBackIntoEntity() 
    {
    	Random rand = new Random();
    	
    	boolean fail = false;
    	
    	for(int x = 0; x < this.inventory.getSlots(); x++)
    	{
    		ItemStack stack = this.inventory.getStackInSlot(x);
    		if(!stack.isEmpty() && stack.getCount() >= 1) {
    			try {
        			NBTTagCompound tag = stack.getTagCompound();
            		
        			NBTTagCompound entityTag = (NBTTagCompound)tag.getTag("EntityIn");
            		Entity entity = this.createEntityFromNBT(entityTag, this.world, this.pos.getX() + (int)rand.nextFloat(), this.pos.getY(), this.pos.getZ() + (int)rand.nextFloat());
            		if(entity instanceof EntityLiving && Reference.CONFIG_HELPER_INSTANCE.areMobsFromCQSpawnersPersistent()) {
            			((EntityLiving)entity).enablePersistence();
            		}
            		if(spawnedInDungeon && entity instanceof ICQREntity) {
            			((ICQREntity)entity).onSpawnFromCQRSpawnerInDungeon(this.pos.getX(), this.pos.getY(), this.pos.getZ());
            		}
            		entity.setUniqueId(MathHelper.getRandomUUID(rand));
            				
            		stack.shrink(1);
            		this.world.spawnEntity(entity);

    			} catch(NullPointerException npe) {
    				fail = true;
    			}
    		}
    	}
    	
    	if(!fail) {
    		this.world.setBlockToAir(this.pos);
    	}
    }
    
    private Entity createEntityFromNBT(NBTTagCompound tag, World worldIn, int x, int y, int z)
	{
		Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
		entity.readFromNBT(tag);
			
		if(entity != null)
		{
			entity.posX = (x + 0.5D);
			entity.posY = (y + 1.0D);
			entity.posZ = (z + 0.5D);
			entity.setPosition(entity.posX, entity.posY, entity.posZ);
		}
		return entity;
	}
   
    private boolean isNonCreativePlayerInRange(double x, double y, double z, double range) 
    {
        for(int i = 0; i < this.world.playerEntities.size(); ++i) 
        {
            EntityPlayer player = this.world.playerEntities.get(i);
 
            if(!(player.isCreative() || player.isSpectator())) 
            {
                double playerDistance = player.getDistanceSq(x, y, z);
 
                if(range < 0 || playerDistance < range * range) 
                {
                    return true;
                }
            }
        }
        return false;
    }

	public void setDungeonSpawner() {
		spawnedInDungeon = true;
	}
}