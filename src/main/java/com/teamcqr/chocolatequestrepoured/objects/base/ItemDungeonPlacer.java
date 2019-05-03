package com.teamcqr.chocolatequestrepoured.objects.base;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemDungeonPlacer extends Item implements IHasModel {
	
	//public static IForgeRegistry<Item> itemRegistry;
	
	private static final int HIGHEST_ICON_NUMBER = 15;
	
	private DungeonBase dungeon;
	private int iconID = 0;
	
	public ItemDungeonPlacer(DungeonBase dungeon) {
		this.dungeon = dungeon;
		
		setUnlocalizedName("dungeon_placer_" + dungeon.getDungeonName().toLowerCase());
		setRegistryName("dungeon_placer_" + dungeon.getDungeonName().toLowerCase());
		setCreativeTab(CQRMain.CQRDungeonPlacerTab);
		this.iconID = this.dungeon.getIconID();
		
		setMaxStackSize(1);
		
		ModItems.ITEMS.add(this);

		System.out.println("Created dungeon placer for dungeon: " + this.dungeon.getDungeonName() + "!");
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }
	
	@Override
	public void registerModels() {
		int textureID = Math.abs(this.iconID);
		if(textureID > HIGHEST_ICON_NUMBER) {
			textureID = 0;
		}
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("cqrepoured:dungeon_placer_d" + textureID, "normal"));
	}
	
	public DungeonBase getAssignedDungeon() {
		return this.dungeon;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return "Dungeon Placer - " + this.dungeon.getDungeonName();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItemMainhand().getItem(), 30);
			playerIn.getCooldownTracker().setCooldown(this, 30);
			this.dungeon.generate(playerIn.getPosition(), worldIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			if (!worldIn.isRemote) {
				player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), 30);
				player.getCooldownTracker().setCooldown(this, 30);
			}
		}
	}
	
	
}
