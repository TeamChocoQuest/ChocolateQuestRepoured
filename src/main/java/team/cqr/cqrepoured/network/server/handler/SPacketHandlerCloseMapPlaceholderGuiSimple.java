//package team.cqr.cqrepoured.network.server.handler;
//
//import net.minecraft.world.level.block.HorizontalBlock;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.network.NetworkEvent.Context;
//import team.cqr.cqrepoured.init.CQRBlocks;
//import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
//import team.cqr.cqrepoured.network.client.packet.CPacketCloseMapPlaceholderGuiSimple;
//import team.cqr.cqrepoured.tileentity.TileEntityMap;
//
//import java.util.function.Supplier;
//
//public class SPacketHandlerCloseMapPlaceholderGuiSimple extends AbstractPacketHandler<CPacketCloseMapPlaceholderGuiSimple> {
//
//	@Override
//	protected void execHandlePacket(CPacketCloseMapPlaceholderGuiSimple message, Supplier<Context> context, World world, PlayerEntity player) {
//		Direction facing = message.getFacing();
//		BlockPos pos = message.getPos().relative(facing);
//		//Original: DistanceToCenterSquared
//		if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 16 * 16) {
//			return;
//		}
//
//		int scale = message.getScale();
//		Direction orientation = message.getOrientation();
//		boolean lockOrientation = message.isLockOrientation();
//		boolean fillMap = message.isFillMap();
//		int fillRadius = message.getFillRadius();
//
//		Direction facingOpposite = facing.getOpposite();
//		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
//		for (int leftRight = -message.getSizeLeft(); leftRight <= message.getSizeRight(); leftRight++) {
//			for (int downUp = -message.getSizeDown(); downUp <= message.getSizeUp(); downUp++) {
//				int x = 0;
//				int z = 0;
//				switch (facingOpposite) {
//				case NORTH:
//					x = leftRight;
//					break;
//				case SOUTH:
//					x = -leftRight;
//					break;
//				case WEST:
//					z = -leftRight;
//					break;
//				case EAST:
//					z = leftRight;
//					break;
//				default:
//					break;
//				}
//				mutablePos.set(pos.getX() + x, pos.getY() + downUp, pos.getZ() + z);
//
//				if (world.isEmptyBlock(mutablePos)) {
//					if (!CQRBlocks.MAP_PLACEHOLDER.canAttachTo(world, mutablePos.relative(facingOpposite), facing)) {
//						continue;
//					}
//
//					//Is this correct?
//					world.setBlock(mutablePos, CQRBlocks.MAP_PLACEHOLDER.defaultBlockState().setValue(HorizontalBlock.FACING, facing), 0);
//				}
//
//				TileEntity tileEntity = world.getBlockEntity(mutablePos);
//				if (tileEntity instanceof TileEntityMap) {
//					int originX = 0;
//					int originZ = 0;
//					int offsetX = 0;
//					int offsetZ = 0;
//					switch (orientation) {
//					case NORTH:
//						originX = -leftRight;
//						originZ = 0;
//						offsetX = leftRight;
//						offsetZ = -downUp;
//						break;
//					case SOUTH:
//						originX = leftRight;
//						originZ = 0;
//						offsetX = -leftRight;
//						offsetZ = downUp;
//						break;
//					case WEST:
//						originX = 0;
//						originZ = leftRight;
//						offsetX = -downUp;
//						offsetZ = -leftRight;
//						break;
//					case EAST:
//						originX = 0;
//						originZ = -leftRight;
//						offsetX = downUp;
//						offsetZ = leftRight;
//						break;
//					default:
//						break;
//					}
//					((TileEntityMap) tileEntity).set(scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
//				}
//			}
//		}
//	}
//
//}
