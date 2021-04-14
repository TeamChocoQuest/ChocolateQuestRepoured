package team.cqr.cqrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.pathtool.CapabilityPathProvider;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.pathfinding.Path;
import team.cqr.cqrepoured.util.Reference;

public class ItemPathTool extends ItemLore {

	public ItemPathTool() {
		this.setMaxStackSize(1);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityPathProvider.createProvider(stack);
	}

	public static Path getPath(ItemStack stack) {
		return stack.getCapability(CapabilityPathProvider.PATH, null).getPath();
	}

	public static void setSelectedNode(ItemStack stack, Path.PathNode node) {
		stack.getCapability(CapabilityPathProvider.PATH, null).setSelectedNode(node);
	}

	@Nullable
	public static Path.PathNode getSelectedNode(ItemStack stack) {
		return stack.getCapability(CapabilityPathProvider.PATH, null).getSelectedNode();
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		/*
		 * sneak + left click -> apply path points to entity left click -> get path points from entity
		 */
		if (!player.world.isRemote && entity instanceof AbstractEntityCQR) {
			if (player.isSneaking()) {
				BlockPos pos = ((AbstractEntityCQR) entity).getHomePositionCQR();
				((AbstractEntityCQR) entity).getPath().copyFrom(getPath(stack), pos != null ? new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ()) : BlockPos.ORIGIN);
				((WorldServer) player.world).spawnParticle((EntityPlayerMP) player, EnumParticleTypes.VILLAGER_HAPPY, false, entity.posX, entity.posY + 0.5D, entity.posZ, 8, 0.5D, 0.5D, 0.5D, 0.1D);
				player.sendMessage(new TextComponentString("Applied path!"));
			} else {
				BlockPos pos = ((AbstractEntityCQR) entity).getHomePositionCQR();
				getPath(stack).copyFrom(((AbstractEntityCQR) entity).getPath(), pos != null ? pos : BlockPos.ORIGIN);
				player.sendMessage(new TextComponentString("Copied path!"));
			}
		}

		return true;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		/*
		 * sneak + right click -> edit existing position right click -> select existing position or add new position
		 */
		ItemStack stack = player.getHeldItem(hand);
		BlockPos position = pos.offset(side);
		Path path = getPath(stack);
		Path.PathNode node = path.getNode(position);
		Path.PathNode selectedNode = getSelectedNode(stack);

		if (world.isRemote) {
			if (node == null) {
				CQRMain.proxy.openGui(Reference.ADD_PATH_NODE_GUI_ID, player, world, hand.ordinal(), selectedNode != null ? selectedNode.getIndex() : -1, position.getX(), position.getY(), position.getZ());
			}
		} else if (node != null) {
			if (selectedNode != null && player.isSneaking()) {
				if (selectedNode.addConnectedNode(node, false)) {
					setSelectedNode(stack, node);
					player.sendMessage(new TextComponentString("Added connection!"));
				}
			} else {
				setSelectedNode(stack, node);
				player.sendMessage(new TextComponentString("Selected node!"));
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		/*
		 * sneak + right click -> delete path
		 */
		if (!worldIn.isRemote && playerIn.isSneaking()) {
			ItemStack stack = playerIn.getHeldItem(handIn);
			getPath(stack).clear();
			playerIn.sendMessage(new TextComponentString("Cleared Path!"));
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		// TODO add tooltip
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if (isSelected && entityIn instanceof EntityPlayer && !worldIn.isRemote) {
			Path path = getPath(stack);
			Path.PathNode selectedNode = getSelectedNode(stack);

			for (Path.PathNode node : path.getNodes()) {
				BlockPos pos = node.getPos();
				Vec3d vec = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

				// Draw start point
				((WorldServer) worldIn).spawnParticle((EntityPlayerMP) entityIn, node != selectedNode ? EnumParticleTypes.VILLAGER_HAPPY : EnumParticleTypes.FLAME, true, vec.x, vec.y, vec.z, 0, 0.0D, 0.025D, 0.0D, 1.0D);

				// Draw connection lines
				for (int index : node.getConnectedNodes()) {
					Path.PathNode connectedNode = path.getNode(index);
					BlockPos pos1 = connectedNode.getPos();
					Vec3d vec1 = new Vec3d(pos1.getX() + 0.5D - vec.x, pos1.getY() + 0.5D - vec.y, pos1.getZ() + 0.5D - vec.z);
					double dist = vec1.length();
					vec1 = vec1.normalize();

					for (double d = 0.25D; d < dist; d += 0.5D) {
						((WorldServer) worldIn).spawnParticle((EntityPlayerMP) entityIn, EnumParticleTypes.CRIT_MAGIC, true, vec.x + d * vec1.x, vec.y + d * vec1.y, vec.z + d * vec1.z, 0, vec1.x * 0.1D, vec1.y * 0.1D, vec1.z * 0.1D, 1.0D);
					}
				}
			}
		}
	}

}
