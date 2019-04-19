package com.teamcqr.chocolatequestrepoured.init.base;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemDungeonPlacer extends Item implements IHasModel {
	
	public static IForgeRegistry<Item> itemRegistry;
	
	private DungeonBase dungeon;
	
	public ItemDungeonPlacer(DungeonBase dungeon) {
		this.dungeon = dungeon;
		setUnlocalizedName("dungeon_placer_" + dungeon.getDungeonName().toLowerCase());
		setRegistryName("dungeon_placer_" + dungeon.getDungeonName().toLowerCase());
		setCreativeTab(CQRMain.CQRDungeonPlacerTab);
		
		setMaxStackSize(1);
		
		ModItems.ITEMS.add(this);
		if(ItemDungeonPlacer.itemRegistry != null) {
			ItemDungeonPlacer.itemRegistry.register(this);
		}
		System.out.println("Created dungeon placer for dungeon: " + this.dungeon.getDungeonName() + "!");
	}
	
	@Override
	public void registerModels() {
		CQRMain.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	public DungeonBase getAssignedDungeon() {
		return this.dungeon;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		//this.dungeon.generate(pos.getX(), pos.getZ(), worldIn, worldIn.getChunkFromBlockCoords(pos), rdm);
		this.dungeon.generate(pos, worldIn);
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
