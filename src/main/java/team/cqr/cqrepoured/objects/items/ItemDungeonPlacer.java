package team.cqr.cqrepoured.objects.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.DungeonRegistry;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.Reference;

public class ItemDungeonPlacer extends Item {

	private static final List<ClientDungeon> CLIENT_DUNGEON_LIST = new ArrayList<>();

	public static final int HIGHEST_ICON_NUMBER = 19;
	private int iconID;

	public ItemDungeonPlacer(int iconID) {
		this.setMaxStackSize(1);
		this.iconID = iconID;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (ClientDungeon fakeDungeon : CLIENT_DUNGEON_LIST) {
				int iconID = fakeDungeon.getIconID() <= HIGHEST_ICON_NUMBER ? fakeDungeon.getIconID() : 0;
				if (iconID == this.iconID) {
					ItemStack stack = new ItemStack(this);

					NBTTagCompound compound = new NBTTagCompound();
					compound.setString("dungeonName", fakeDungeon.getDungeonName());
					compound.setInteger("iconID", iconID);
					NBTTagList dependencies = new NBTTagList();
					for (String dependency : fakeDungeon.getDependencies()) {
						dependencies.appendTag(new NBTTagString(dependency));
					}
					compound.setTag("dependencies", dependencies);
					stack.setTagCompound(compound);

					items.add(stack);
				}
			}
		}
	}

	/**
	 * Overriding this instead of {@link Item#getEquipmentSlot(ItemStack)} to prevent this item being placed into the head slot when shift-clicked.
	 */
	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
		return armorType == EntityEquipmentSlot.HEAD;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("dependencies")) {
			tooltip.add("Mod Dependencies: ");
			for (NBTBase nbtTag : stack.getTagCompound().getTagList("dependencies", Constants.NBT.TAG_STRING)) {
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
			NBTTagCompound compound = stack.getTagCompound();
			return "Dungeon Placer - " + compound.getString("dungeonName");
		}
		return "Dungeon Placer";
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			ItemStack stack = playerIn.getHeldItem(handIn);

			if (stack.hasTagCompound()) {
				String dungeonName = stack.getTagCompound().getString("dungeonName");
				DungeonBase dungeon = DungeonRegistry.getInstance().getDungeon(dungeonName);

				if (dungeon != null) {
					Vec3d vec = playerIn.getPositionEyes(1.0F);
					Vec3d look = playerIn.getLookVec();

					RayTraceResult result = worldIn.rayTraceBlocks(vec, vec.add(look.scale(256.0D)));

					if (result != null) {
						BlockPos pos = result.getBlockPos().offset(result.sideHit);
						dungeon.generateWithOffsets(worldIn, pos.getX(), pos.getY(), pos.getZ(), new Random(), DungeonDataManager.DungeonSpawnType.DUNGEON_PLACER_ITEM, false);

						playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);
						if (!(playerIn.isCreative() || playerIn.isSpectator())) {
							stack.shrink(1);
						}
					}
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
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

	@EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.player.world.isRemote) {
				CQRMain.NETWORK.sendTo(new SPacketDungeonSync(DungeonRegistry.getInstance().getDungeons()), (EntityPlayerMP) event.player);
			}
		}

	}

}
