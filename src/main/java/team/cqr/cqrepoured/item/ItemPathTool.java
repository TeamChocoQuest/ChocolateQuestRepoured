package team.cqr.cqrepoured.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.pathtool.CapabilityPathProvider;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.pathfinding.CQRNPCPath;
import team.cqr.cqrepoured.util.GuiHandler;

import javax.annotation.Nullable;

public class ItemPathTool extends ItemLore {

	public ItemPathTool() {
		this.setMaxStackSize(1);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return CapabilityPathProvider.createProvider(stack);
	}

	public static CQRNPCPath getPath(ItemStack stack) {
		return stack.getCapability(CapabilityPathProvider.PATH, null).getPath();
	}

	public static void setSelectedNode(ItemStack stack, CQRNPCPath.PathNode node) {
		stack.getCapability(CapabilityPathProvider.PATH, null).setSelectedNode(node);
	}

	@Nullable
	public static CQRNPCPath.PathNode getSelectedNode(ItemStack stack) {
		return stack.getCapability(CapabilityPathProvider.PATH, null).getSelectedNode();
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		/*
		 * sneak + left click -> apply path points to entity left click -> get path points from entity
		 */
		if (!player.world.isRemote && entity instanceof AbstractEntityCQR) {
			if (player.isSneaking()) {
				BlockPos pos = ((AbstractEntityCQR) entity).getHomePositionCQR();
				((AbstractEntityCQR) entity).getPath().copyFrom(getPath(stack), pos != null ? new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ()) : BlockPos.ORIGIN);
				((ServerWorld) player.world).spawnParticle((ServerPlayerEntity) player, ParticleTypes.VILLAGER_HAPPY, false, entity.posX, entity.posY + 0.5D, entity.posZ, 8, 0.5D, 0.5D, 0.5D, 0.1D);
				player.sendMessage(new StringTextComponent("Applied path!"));
			} else {
				BlockPos pos = ((AbstractEntityCQR) entity).getHomePositionCQR();
				getPath(stack).copyFrom(((AbstractEntityCQR) entity).getPath(), pos != null ? pos : BlockPos.ORIGIN);
				player.sendMessage(new StringTextComponent("Copied path!"));
			}
		}

		return true;
	}

	@Override
	public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		/*
		 * sneak + right click -> edit existing position right click -> select existing position or add new position
		 */
		ItemStack stack = player.getHeldItem(hand);
		BlockPos position = pos.offset(side);
		CQRNPCPath path = getPath(stack);
		CQRNPCPath.PathNode node = path.getNode(position);
		CQRNPCPath.PathNode selectedNode = getSelectedNode(stack);

		if (world.isRemote) {
			if (node == null) {
				CQRMain.proxy.openGui(GuiHandler.ADD_PATH_NODE_GUI_ID, player, world, hand.ordinal(), selectedNode != null ? selectedNode.getIndex() : -1, position.getX(), position.getY(), position.getZ());
			}
		} else if (node != null) {
			if (selectedNode != null && player.isSneaking()) {
				if (selectedNode.addConnectedNode(node, false)) {
					setSelectedNode(stack, node);
					player.sendMessage(new StringTextComponent("Added connection!"));
				}
			} else {
				setSelectedNode(stack, node);
				player.sendMessage(new StringTextComponent("Selected node!"));
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		/*
		 * sneak + right click -> delete path
		 */
		if (!worldIn.isRemote && playerIn.isSneaking()) {
			ItemStack stack = playerIn.getHeldItem(handIn);
			getPath(stack).clear();
			playerIn.sendMessage(new StringTextComponent("Cleared Path!"));
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if (isSelected && entityIn instanceof PlayerEntity && !worldIn.isRemote) {
			CQRNPCPath path = getPath(stack);
			CQRNPCPath.PathNode selectedNode = getSelectedNode(stack);

			for (CQRNPCPath.PathNode node : path.getNodes()) {
				BlockPos pos = node.getPos();
				Vector3d vec = new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

				// Draw start point
				((ServerWorld) worldIn).spawnParticle((ServerPlayerEntity) entityIn, node != selectedNode ? ParticleTypes.VILLAGER_HAPPY : ParticleTypes.FLAME, true, vec.x, vec.y, vec.z, 0, 0.0D, 0.025D, 0.0D, 1.0D);

				// Draw connection lines
				for (int index : node.getConnectedNodes()) {
					CQRNPCPath.PathNode connectedNode = path.getNode(index);
					BlockPos pos1 = connectedNode.getPos();
					Vector3d vec1 = new Vector3d(pos1.getX() + 0.5D - vec.x, pos1.getY() + 0.5D - vec.y, pos1.getZ() + 0.5D - vec.z);
					double dist = vec1.length();
					vec1 = vec1.normalize();

					for (double d = 0.25D; d < dist; d += 0.5D) {
						((ServerWorld) worldIn).spawnParticle((ServerPlayerEntity) entityIn, ParticleTypes.CRIT_MAGIC, true, vec.x + d * vec1.x, vec.y + d * vec1.y, vec.z + d * vec1.z, 0, vec1.x * 0.1D, vec1.y * 0.1D, vec1.z * 0.1D, 1.0D);
					}
				}
			}
		}
	}

}
