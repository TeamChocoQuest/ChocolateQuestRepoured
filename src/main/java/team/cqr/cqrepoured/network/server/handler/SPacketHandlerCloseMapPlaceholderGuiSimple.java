package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.network.client.packet.CPacketCloseMapPlaceholderGuiSimple;
import team.cqr.cqrepoured.tileentity.TileEntityMap;

public class SPacketHandlerCloseMapPlaceholderGuiSimple implements IMessageHandler<CPacketCloseMapPlaceholderGuiSimple, IMessage> {

	@Override
	public IMessage onMessage(CPacketCloseMapPlaceholderGuiSimple message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);

				EnumFacing facing = message.getFacing();
				BlockPos pos = message.getPos().offset(facing);
				if (player.getDistanceSqToCenter(pos) > 16 * 16) {
					return;
				}

				int scale = message.getScale();
				EnumFacing orientation = message.getOrientation();
				boolean lockOrientation = message.isLockOrientation();
				boolean fillMap = message.isFillMap();
				int fillRadius = message.getFillRadius();

				EnumFacing facingOpposite = facing.getOpposite();
				BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
				for (int leftRight = -message.getSizeLeft(); leftRight <= message.getSizeRight(); leftRight++) {
					for (int downUp = -message.getSizeDown(); downUp <= message.getSizeUp(); downUp++) {
						int x = 0;
						int z = 0;
						switch (facingOpposite) {
						case NORTH:
							x = leftRight;
							break;
						case SOUTH:
							x = -leftRight;
							break;
						case WEST:
							z = -leftRight;
							break;
						case EAST:
							z = leftRight;
							break;
						default:
							break;
						}
						mutablePos.setPos(pos.getX() + x, pos.getY() + downUp, pos.getZ() + z);

						if (world.isAirBlock(mutablePos)) {
							if (!CQRBlocks.MAP_PLACEHOLDER.canAttachTo(world, mutablePos.offset(facingOpposite), facing)) {
								continue;
							}

							world.setBlockState(mutablePos, CQRBlocks.MAP_PLACEHOLDER.getDefaultState().withProperty(BlockHorizontal.FACING, facing));
						}

						TileEntity tileEntity = world.getTileEntity(mutablePos);
						if (tileEntity instanceof TileEntityMap) {
							int originX = 0;
							int originZ = 0;
							int offsetX = 0;
							int offsetZ = 0;
							switch (orientation) {
							case NORTH:
								originX = -leftRight;
								originZ = 0;
								offsetX = leftRight;
								offsetZ = -downUp;
								break;
							case SOUTH:
								originX = leftRight;
								originZ = 0;
								offsetX = -leftRight;
								offsetZ = downUp;
								break;
							case WEST:
								originX = 0;
								originZ = leftRight;
								offsetX = -downUp;
								offsetZ = -leftRight;
								break;
							case EAST:
								originX = 0;
								originZ = -leftRight;
								offsetX = downUp;
								offsetZ = leftRight;
								break;
							default:
								break;
							}
							((TileEntityMap) tileEntity).set(scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
						}
					}
				}
			});
		}
		return null;
	}

}
