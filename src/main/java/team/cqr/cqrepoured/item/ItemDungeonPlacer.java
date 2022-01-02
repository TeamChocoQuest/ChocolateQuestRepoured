package team.cqr.cqrepoured.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class ItemDungeonPlacer extends Item {

	private static final List<ClientDungeon> CLIENT_DUNGEON_LIST = new ArrayList<>();

	public static final int HIGHEST_ICON_NUMBER = 19;
	private int iconID;

	public ItemDungeonPlacer(int iconID) {
		this.setMaxStackSize(1);
		this.iconID = iconID;
	}

	@Override
	public void getSubItems(ItemGroup tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (ClientDungeon fakeDungeon : CLIENT_DUNGEON_LIST) {
				int iconID = fakeDungeon.getIconID() <= HIGHEST_ICON_NUMBER ? fakeDungeon.getIconID() : 0;
				if (iconID == this.iconID) {
					ItemStack stack = new ItemStack(this);

					CompoundNBT compound = new CompoundNBT();
					compound.setString("dungeonName", fakeDungeon.getDungeonName());
					compound.setInteger("iconID", iconID);
					ListNBT dependencies = new ListNBT();
					for (String dependency : fakeDungeon.getDependencies()) {
						dependencies.appendTag(new StringNBT(dependency));
					}
					compound.setTag("dependencies", dependencies);
					stack.setTagCompound(compound);

					items.add(stack);
				}
			}
		}
	}

	/**
	 * Overriding this instead of {@link Item#getEquipmentSlot(ItemStack)} to prevent this item being placed into the head
	 * slot when shift-clicked.
	 */
	@Override
	public boolean isValidArmor(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == EquipmentSlotType.HEAD;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("dependencies")) {
			tooltip.add("Mod Dependencies: ");
			for (INBT nbtTag : stack.getTagCompound().getTagList("dependencies", Constants.NBT.TAG_STRING)) {
				String dependency = nbtTag.toString().replace("\"", "");
				if (Loader.isModLoaded(dependency)) {
					tooltip.add(TextFormatting.GRAY + "- " + TextFormatting.DARK_GREEN + dependency);
				} else {
					tooltip.add(TextFormatting.GRAY + "- " + TextFormatting.RED + dependency);
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.hasTagCompound()) {
			CompoundNBT compound = stack.getTagCompound();
			return "Dungeon Placer - " + compound.getString("dungeonName");
		}
		return "Dungeon Placer";
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote) {
			ItemStack stack = playerIn.getHeldItem(handIn);

			if (stack.hasTagCompound()) {
				String dungeonName = stack.getTagCompound().getString("dungeonName");
				DungeonBase dungeon = DungeonRegistry.getInstance().getDungeon(dungeonName);

				if (dungeon != null) {
					Vector3d vec = playerIn.getPositionEyes(1.0F);
					Vector3d look = playerIn.getLookVec();

					RayTraceResult result = worldIn.rayTraceBlocks(vec, vec.add(look.scale(256.0D)));

					if (result != null) {
						BlockPos pos = result.getBlockPos().offset(result.sideHit);
						dungeon.generateWithOffsets(worldIn, pos.getX(), pos.getY(), pos.getZ(), new Random(), DungeonDataManager.DungeonSpawnType.DUNGEON_PLACER_ITEM, false);

						playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);
						if ((!playerIn.isCreative() && !playerIn.isSpectator())) {
							stack.shrink(1);
						}
					}
				}
			}
		}
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	public static void updateClientDungeonList(List<ClientDungeon> list) {
		CLIENT_DUNGEON_LIST.clear();
		CLIENT_DUNGEON_LIST.addAll(list);
	}

	public static class ClientDungeon {

		private String dungeonName;
		private int iconID;
		private String[] dependencies;

		public ClientDungeon(String dungeonName, int iconID, String[] dependencies) {
			this.dungeonName = dungeonName;
			this.iconID = iconID;
			this.dependencies = dependencies;
		}

		public String getDungeonName() {
			return this.dungeonName;
		}

		public int getIconID() {
			return this.iconID;
		}

		public String[] getDependencies() {
			return this.dependencies;
		}

	}

}
