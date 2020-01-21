package com.teamcqr.chocolatequestrepoured.objects.items;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemSuperTool extends Item
{
	private String MODE_TAG = "Mode";
	private String BLOCK_TAG = "Block";
	private AttributeModifier attackDamage;
	
	public ItemSuperTool() 
	{
		this.setMaxDamage(2048);
		this.setMaxStackSize(1);
		
		this.attackDamage = new AttributeModifier("SuperToolDamageModifier", 10000D, 0);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
    {
        return blockIn.getBlock() != Blocks.BEDROCK;
    }
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), this.attackDamage);
		}
		return multimap;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTTagCompound stackTag = stack.getTagCompound();
		
	/*	if(stackTag == null)
		{
			stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
		} */
		
		if(playerIn.isSneaking())
		{
			int mode = getMode(stack);
			
			if(mode == 0 || mode == 1 || mode == 2 || mode == 3)
			{
				mode += 1;
			}
			
			if(mode == 4)
			{
				mode = 1;
			}
			
			setMode(stack, mode);
			
			if(!worldIn.isRemote)
			{
				String stringMode = "";
				
				if(mode == 1)
				{
					stringMode = "Build";
				}
				
				if(mode == 2)
				{
					stringMode = "Fill";
				}
				
				if(mode == 3)
				{
					stringMode = "Mine";
				}
				
				playerIn.sendStatusMessage(new TextComponentString("Pickaxe Mode:" + " " + stringMode), true);
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		
		if(player.isSneaking() && getMode(stack) != 0)
		{
			setBlock(stack, worldIn.getBlockState(pos).getBlock());
		}
		
		int posX = pos.getX();
		int posY = pos.getY();
		int posZ = pos.getZ();
			
		int size = 2;
		
		if(facing == EnumFacing.UP)
		{
			for(int t = -size + 1; t < size; t++) 
			{
				for(int v = -size + 1; v < size; v++)
				{
					performAction(player, worldIn, new BlockPos(posX + t, posY, posZ + v), stack);
				}
			}
		}
			
		if(facing == EnumFacing.DOWN)
		{
			for(int t = -size + 1; t < size; t++) 
			{
				for(int v = -size + 1; v < size; v++)
				{
					performAction(player, worldIn, new BlockPos(posX + t, posY, posZ + v), stack);
				}
			}
		}
			
		if(facing == EnumFacing.SOUTH)
		{
			for(int t = -size + 1; t < size; t++) 
			{
				for(int v = -size + 1; v < size; v++)
				{
					performAction(player, worldIn, new BlockPos(posX + t, posY + v, posZ), stack);
				}
			}
		}
			
		if(facing == EnumFacing.NORTH)
		{
			for(int t = -size + 1; t < size; t++) 
				{
			        for(int v = -size + 1; v < size; v++)
			        {
			        	performAction(player, worldIn, new BlockPos(posX + t, posY + v, posZ), stack);
			        }
			    }
			}
			
			if(facing == EnumFacing.EAST)
			{
				for(int t = -size + 1; t < size; t++) 
				{
			        for(int v = -size + 1; v < size; v++)
			        {
			        	performAction(player, worldIn, new BlockPos(posX, posY + t, posZ + v), stack);
			        }
				}
			}
			
			if(facing == EnumFacing.WEST)
			{
				for(int t = -size + 1; t < size; t++) 
				{
			        for(int v = -size + 1; v < size; v++)
			        {
			        	performAction(player, worldIn, new BlockPos(posX, posY + t, posZ + v), stack);
			        }
				}
			}
		
        return EnumActionResult.PASS;
    }
	
	private void performAction(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack)
	{
		if(getMode(stack) == 1)
		{
			worldIn.setBlockState(pos, getBlock(stack).getDefaultState());
		}
		
		if(getMode(stack) == 2)
		{
			Block block = worldIn.getBlockState(pos).getBlock();
			
			if(block != Blocks.AIR)
			{
				worldIn.setBlockState(pos, getBlock(stack).getDefaultState());
			}
		}
		
		if(getMode(stack) == 3)
		{
			worldIn.setBlockToAir(pos);
			
			for(int r = 0; r < 5; r++)
			{
				worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, Block.getIdFromBlock(getBlock(stack))); //#TODO Not always spawn proper particles
			}
		}
	}
	
	private int getMode(ItemStack stack)
	{
		if(stack.getTagCompound() != null)
		{
			if(stack.getTagCompound().hasKey(MODE_TAG))
			{
				return stack.getTagCompound().getInteger(MODE_TAG);
			}
		}
		return 0;
	}
	
	public Block getBlock(ItemStack stack)
	{
		if(stack.getTagCompound() != null)
		{
			if(stack.getTagCompound().hasKey(BLOCK_TAG))
			{
				int blockID = stack.getTagCompound().getInteger(BLOCK_TAG);
				return Block.getBlockById(blockID);
			}
		}
		return Blocks.STONE;
	}
	
	public void setMode(ItemStack stack, int mode)
	{
		NBTTagCompound stackTag = stack.getTagCompound();
		
		if(stackTag == null)
		{
			stackTag = new NBTTagCompound();
		}
		
		stackTag.setInteger(MODE_TAG, mode);
	}
	
	public void setBlock(ItemStack stack, Block blockIn)
	{
		NBTTagCompound stackTag = stack.getTagCompound();
		
		if(stackTag == null)
		{
			stackTag = new NBTTagCompound();
		}
		
		stackTag.setInteger(BLOCK_TAG, Block.getIdFromBlock(blockIn));
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(stack.getTagCompound() == null)
		{
			NBTTagCompound stackTag = stack.getTagCompound();
			stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
			
			setBlock(stack, Blocks.STONE);
			setMode(stack, 1);
		}
	}
}