package team.cqr.cqrepoured.block;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class BlockExporterChest extends ChestBlock {

	private static final Set<BlockExporterChest> EXPORTER_CHESTS = new HashSet<>();

	protected static final AxisAlignedBB NORTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
	protected static final AxisAlignedBB WEST_CHEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB EAST_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

	private final ResourceLocation overlayTexture;

	protected BlockExporterChest(String resourceName) {
		this(new ResourceLocation(resourceName));
	}

	protected BlockExporterChest(String resourceDomain, String resourcePath) {
		this(new ResourceLocation(resourceDomain, resourcePath));
	}

	protected BlockExporterChest(ResourceLocation overlayTexture) {
		super(Properties.of(Material.WOOD)
				.sound(SoundType.WOOD)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.noOcclusion(), () -> TileEntityType.CHEST);
		this.overlayTexture = overlayTexture;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		EXPORTER_CHESTS.add(this);
	}

	public static Set<BlockExporterChest> getExporterChests() {
		return Collections.unmodifiableSet(EXPORTER_CHESTS);
	}

	public ResourceLocation getOverlayTexture() {
		return this.overlayTexture;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		return ActionResultType.PASS;
	}

	@Override
	public boolean is(Block block) {
		return block.getClass() == ChestBlock.class || block instanceof BlockExporterChest;
	}

}
