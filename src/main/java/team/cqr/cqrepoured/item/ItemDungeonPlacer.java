package team.cqr.cqrepoured.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.ModList;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemDungeonPlacer extends ItemLore {
//#TODO register
	private static final List<ClientDungeon> CLIENT_DUNGEON_LIST = new ArrayList<>();

	public static final int HIGHEST_ICON_NUMBER = 19;
	private int iconID;

	public ItemDungeonPlacer(int iconID, Properties properties)
	{
		super(properties.stacksTo(1));
		this.iconID = iconID;
	}

	@Override
	public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) {
		if (this.allowdedIn(tab)) {
			for (ClientDungeon fakeDungeon : CLIENT_DUNGEON_LIST) {
				int iconID = fakeDungeon.getIconID() <= HIGHEST_ICON_NUMBER ? fakeDungeon.getIconID() : 0;
				if (iconID == this.iconID) {
					ItemStack stack = new ItemStack(this);

					CompoundNBT compound = new CompoundNBT();
					compound.putString("dungeonName", fakeDungeon.getDungeonName());
					compound.putInt("iconID", iconID);
					ListNBT dependencies = new ListNBT();
					for (String dependency : fakeDungeon.getDependencies()) {
						dependencies.add(StringNBT.valueOf(dependency));
					}
					compound.put("dependencies", dependencies);
					stack.setTag(compound);

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
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == EquipmentSlotType.HEAD;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (stack.getTag() != null && stack.getTag().contains("dependencies")) {
			tooltip.add(new StringTextComponent("Mod Dependencies: "));
			for (INBT nbtTag : stack.getTag().getList("dependencies", Constants.NBT.TAG_STRING)) {
				String dependency = nbtTag.toString().replace("\"", "");
				if (ModList.get().isLoaded(dependency)) {
					tooltip.add(new StringTextComponent(TextFormatting.GRAY + "- " + TextFormatting.DARK_GREEN + dependency));
				} else {
					tooltip.add(new StringTextComponent(TextFormatting.GRAY + "- " + TextFormatting.RED + dependency));
				}
			}
		}
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		if (stack.hasTag()) {
			CompoundNBT compound = stack.getTag();
			return new StringTextComponent("Dungeon Placer - " + compound.getString("dungeonName"));
		}
		return new StringTextComponent("Dungeon Placer");
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isClientSide) {
			ItemStack stack = playerIn.getItemInHand(handIn);

			if (stack.hasTag()) {
				String dungeonName = stack.getTag().getString("dungeonName");
				DungeonBase dungeon = DungeonRegistry.getInstance().getDungeon(dungeonName);

				if (dungeon != null) {
					Vector3d vec = playerIn.getEyePosition(1.0F);
					Vector3d look = playerIn.getLookAngle();

					BlockRayTraceResult result = worldIn.clip(new RayTraceContext(vec, vec.add(look.scale(256.0D)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, playerIn));

					if (result != null) {
						BlockPos pos = result.getBlockPos().relative(result.getDirection());
						dungeon.generateWithOffsets(worldIn, pos.getX(), pos.getY(), pos.getZ(), new Random(), DungeonDataManager.DungeonSpawnType.DUNGEON_PLACER_ITEM, false);

						playerIn.getCooldowns().addCooldown(stack.getItem(), 30);
						if ((!playerIn.isCreative() && !playerIn.isSpectator())) {
							stack.shrink(1);
						}
					}
				}
			}
		}
		return ActionResult.success(playerIn.getItemInHand(handIn));
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
