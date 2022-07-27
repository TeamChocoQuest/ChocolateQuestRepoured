package team.cqr.cqrepoured.world;

import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import net.minecraft.block.JigsawBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.EmptyJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class CQRJigsawManager {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void addPieces(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_, CQRJigsawManager.IPieceFactory p_242837_2_, ChunkGenerator p_242837_3_, TemplateManager p_242837_4_, BlockPos p_242837_5_,
			List<? super AbstractVillagePiece> p_242837_6_, Random p_242837_7_, boolean p_242837_8_, boolean p_242837_9_) {
		addPieces(p_242837_0_, p_242837_1_, p_242837_2_, p_242837_3_, p_242837_4_, p_242837_5_, p_242837_6_, p_242837_7_, p_242837_8_, p_242837_9_, Rotation.getRandom(p_242837_7_));
	}

	public static void addPieces(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_, CQRJigsawManager.IPieceFactory p_242837_2_, ChunkGenerator p_242837_3_, TemplateManager p_242837_4_, BlockPos p_242837_5_,
			List<? super AbstractVillagePiece> p_242837_6_, Random p_242837_7_, boolean p_242837_8_, boolean p_242837_9_, Rotation rotation) {
		Structure.bootstrap();
		MutableRegistry<JigsawPattern> mutableregistry = p_242837_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
		JigsawPattern jigsawpattern = p_242837_1_.startPool().get();
		JigsawPiece jigsawpiece = jigsawpattern.getRandomTemplate(p_242837_7_);
		AbstractVillagePiece abstractvillagepiece = p_242837_2_.create(p_242837_4_, jigsawpiece, p_242837_5_, jigsawpiece.getGroundLevelDelta(), rotation, jigsawpiece.getBoundingBox(p_242837_4_, p_242837_5_, rotation));
		MutableBoundingBox mutableboundingbox = abstractvillagepiece.getBoundingBox();
		int i = (mutableboundingbox.x1 + mutableboundingbox.x0) / 2;
		int j = (mutableboundingbox.z1 + mutableboundingbox.z0) / 2;
		int k;
		if (p_242837_9_) {
			k = p_242837_5_.getY() + p_242837_3_.getFirstFreeHeight(i, j, Heightmap.Type.WORLD_SURFACE_WG);
		} else {
			k = p_242837_5_.getY();
		}

		int l = mutableboundingbox.y0 + abstractvillagepiece.getGroundLevelDelta();
		abstractvillagepiece.move(0, k - l, 0);
		p_242837_6_.add(abstractvillagepiece);
		if (p_242837_1_.maxDepth() > 0) {
			int i1 = 80;
			AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) (i - 80), (double) (k - 80), (double) (j - 80), (double) (i + 80 + 1), (double) (k + 80 + 1), (double) (j + 80 + 1));
			CQRJigsawManager.Assembler jigsawmanager$assembler = new CQRJigsawManager.Assembler(mutableregistry, p_242837_1_.maxDepth(), p_242837_2_, p_242837_3_, p_242837_4_, p_242837_6_, p_242837_7_);
			jigsawmanager$assembler.placing.addLast(new CQRJigsawManager.Entry(abstractvillagepiece, new MutableObject<>(VoxelShapes.join(VoxelShapes.create(axisalignedbb), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)),
					IBooleanFunction.ONLY_FIRST)), k + 80, 0));

			while (!jigsawmanager$assembler.placing.isEmpty()) {
				CQRJigsawManager.Entry jigsawmanager$entry = jigsawmanager$assembler.placing.removeFirst();
				jigsawmanager$assembler.tryPlacingChildren(jigsawmanager$entry.piece, jigsawmanager$entry.free, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, p_242837_8_);
			}

		}
	}

	public static void addPieces(DynamicRegistries p_242838_0_, AbstractVillagePiece p_242838_1_, int p_242838_2_, CQRJigsawManager.IPieceFactory p_242838_3_, ChunkGenerator p_242838_4_, TemplateManager p_242838_5_,
			List<? super AbstractVillagePiece> p_242838_6_, Random p_242838_7_) {
		MutableRegistry<JigsawPattern> mutableregistry = p_242838_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
		CQRJigsawManager.Assembler jigsawmanager$assembler = new CQRJigsawManager.Assembler(mutableregistry, p_242838_2_, p_242838_3_, p_242838_4_, p_242838_5_, p_242838_6_, p_242838_7_);
		jigsawmanager$assembler.placing.addLast(new CQRJigsawManager.Entry(p_242838_1_, new MutableObject<>(VoxelShapes.INFINITY), 0, 0));

		while (!jigsawmanager$assembler.placing.isEmpty()) {
			CQRJigsawManager.Entry jigsawmanager$entry = jigsawmanager$assembler.placing.removeFirst();
			jigsawmanager$assembler.tryPlacingChildren(jigsawmanager$entry.piece, jigsawmanager$entry.free, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, false);
		}

	}

	static final class Assembler {
		private final Registry<JigsawPattern> pools;
		private final int maxDepth;
		private final CQRJigsawManager.IPieceFactory factory;
		private final ChunkGenerator chunkGenerator;
		private final TemplateManager structureManager;
		private final List<? super AbstractVillagePiece> pieces;
		private final Random random;
		private final Deque<CQRJigsawManager.Entry> placing = Queues.newArrayDeque();

		private Assembler(Registry<JigsawPattern> pPools, int pMaxDepth, CQRJigsawManager.IPieceFactory pFactory, ChunkGenerator pChunkGenerator, TemplateManager pStructureManager, List<? super AbstractVillagePiece> pPieces, Random pRandom) {
			this.pools = pPools;
			this.maxDepth = pMaxDepth;
			this.factory = pFactory;
			this.chunkGenerator = pChunkGenerator;
			this.structureManager = pStructureManager;
			this.pieces = pPieces;
			this.random = pRandom;
		}

		private void tryPlacingChildren(AbstractVillagePiece p_236831_1_, MutableObject<VoxelShape> p_236831_2_, int p_236831_3_, int p_236831_4_, boolean p_236831_5_) {
			JigsawPiece jigsawpiece = p_236831_1_.getElement();
			BlockPos blockpos = p_236831_1_.getPosition();
			Rotation rotation = p_236831_1_.getRotation();
			JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = jigsawpiece.getProjection();
			boolean flag = jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID;
			MutableObject<VoxelShape> mutableobject = new MutableObject<>();
			MutableBoundingBox mutableboundingbox = p_236831_1_.getBoundingBox();
			int i = mutableboundingbox.y0;

			label139: for (Template.BlockInfo template$blockinfo : jigsawpiece.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
				Direction direction = JigsawBlock.getFrontFacing(template$blockinfo.state);
				BlockPos blockpos1 = template$blockinfo.pos;
				BlockPos blockpos2 = blockpos1.relative(direction);
				int j = blockpos1.getY() - i;
				int k = -1;
				ResourceLocation resourcelocation = new ResourceLocation(template$blockinfo.nbt.getString("pool"));
				Optional<JigsawPattern> optional = this.pools.getOptional(resourcelocation);
				if (optional.isPresent() && (optional.get().size() != 0 || Objects.equals(resourcelocation, JigsawPatternRegistry.EMPTY.location()))) {
					ResourceLocation resourcelocation1 = optional.get().getFallback();
					Optional<JigsawPattern> optional1 = this.pools.getOptional(resourcelocation1);
					if (optional1.isPresent() && (optional1.get().size() != 0 || Objects.equals(resourcelocation1, JigsawPatternRegistry.EMPTY.location()))) {
						boolean flag1 = mutableboundingbox.isInside(blockpos2);
						MutableObject<VoxelShape> mutableobject1;
						int l;
						if (flag1) {
							mutableobject1 = mutableobject;
							l = i;
							if (mutableobject.getValue() == null) {
								mutableobject.setValue(VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)));
							}
						} else {
							mutableobject1 = p_236831_2_;
							l = p_236831_3_;
						}

						List<JigsawPiece> list = Lists.newArrayList();
						if (p_236831_4_ != this.maxDepth) {
							list.addAll(optional.get().getShuffledTemplates(this.random));
						}

						list.addAll(optional1.get().getShuffledTemplates(this.random));

						for (JigsawPiece jigsawpiece1 : list) {
							if (jigsawpiece1 == EmptyJigsawPiece.INSTANCE) {
								break;
							}

							for (Rotation rotation1 : Rotation.getShuffled(this.random)) {
								List<Template.BlockInfo> list1 = jigsawpiece1.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation1, this.random);
								MutableBoundingBox mutableboundingbox1 = jigsawpiece1.getBoundingBox(this.structureManager, BlockPos.ZERO, rotation1);
								int i1;
								if (p_236831_5_ && mutableboundingbox1.getYSpan() <= 16) {
									i1 = list1.stream().mapToInt((p_242841_2_) -> {
										if (!mutableboundingbox1.isInside(p_242841_2_.pos.relative(JigsawBlock.getFrontFacing(p_242841_2_.state)))) {
											return 0;
										} else {
											ResourceLocation resourcelocation2 = new ResourceLocation(p_242841_2_.nbt.getString("pool"));
											Optional<JigsawPattern> optional2 = this.pools.getOptional(resourcelocation2);
											Optional<JigsawPattern> optional3 = optional2.flatMap((p_242843_1_) -> {
												return this.pools.getOptional(p_242843_1_.getFallback());
											});
											int k3 = optional2.map((p_242842_1_) -> {
												return p_242842_1_.getMaxSize(this.structureManager);
											}).orElse(0);
											int l3 = optional3.map((p_242840_1_) -> {
												return p_242840_1_.getMaxSize(this.structureManager);
											}).orElse(0);
											return Math.max(k3, l3);
										}
									}).max().orElse(0);
								} else {
									i1 = 0;
								}

								for (Template.BlockInfo template$blockinfo1 : list1) {
									if (JigsawBlock.canAttach(template$blockinfo, template$blockinfo1)) {
										BlockPos blockpos3 = template$blockinfo1.pos;
										BlockPos blockpos4 = new BlockPos(blockpos2.getX() - blockpos3.getX(), blockpos2.getY() - blockpos3.getY(), blockpos2.getZ() - blockpos3.getZ());
										MutableBoundingBox mutableboundingbox2 = jigsawpiece1.getBoundingBox(this.structureManager, blockpos4, rotation1);
										int j1 = mutableboundingbox2.y0;
										JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour1 = jigsawpiece1.getProjection();
										boolean flag2 = jigsawpattern$placementbehaviour1 == JigsawPattern.PlacementBehaviour.RIGID;
										int k1 = blockpos3.getY();
										int l1 = j - k1 + JigsawBlock.getFrontFacing(template$blockinfo.state).getStepY();
										int i2;
										if (flag && flag2) {
											i2 = i + l1;
										} else {
											if (k == -1) {
												k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
											}

											i2 = k - k1;
										}

										int j2 = i2 - j1;
										MutableBoundingBox mutableboundingbox3 = mutableboundingbox2.moved(0, j2, 0);
										BlockPos blockpos5 = blockpos4.offset(0, j2, 0);
										if (i1 > 0) {
											int k2 = Math.max(i1 + 1, mutableboundingbox3.y1 - mutableboundingbox3.y0);
											mutableboundingbox3.y1 = mutableboundingbox3.y0 + k2;
										}

										if (!VoxelShapes.joinIsNotEmpty(mutableobject1.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox3).deflate(0.25D)), IBooleanFunction.ONLY_SECOND)) {
											mutableobject1.setValue(VoxelShapes.joinUnoptimized(mutableobject1.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox3)), IBooleanFunction.ONLY_FIRST));
											int j3 = p_236831_1_.getGroundLevelDelta();
											int l2;
											if (flag2) {
												l2 = j3 - l1;
											} else {
												l2 = jigsawpiece1.getGroundLevelDelta();
											}

											AbstractVillagePiece abstractvillagepiece = this.factory.create(this.structureManager, jigsawpiece1, blockpos5, l2, rotation1, mutableboundingbox3);
											int i3;
											if (flag) {
												i3 = i + j;
											} else if (flag2) {
												i3 = i2 + k1;
											} else {
												if (k == -1) {
													k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
												}

												i3 = k + l1 / 2;
											}

											p_236831_1_.addJunction(new JigsawJunction(blockpos2.getX(), i3 - j + j3, blockpos2.getZ(), l1, jigsawpattern$placementbehaviour1));
											abstractvillagepiece.addJunction(new JigsawJunction(blockpos1.getX(), i3 - k1 + l2, blockpos1.getZ(), -l1, jigsawpattern$placementbehaviour));
											this.pieces.add(abstractvillagepiece);
											if (p_236831_4_ + 1 <= this.maxDepth) {
												this.placing.addLast(new CQRJigsawManager.Entry(abstractvillagepiece, mutableobject1, l, p_236831_4_ + 1));
											}
											continue label139;
										}
									}
								}
							}
						}
					} else {
						CQRJigsawManager.LOGGER.warn("Empty or none existent fallback pool: {}", (Object) resourcelocation1);
					}
				} else {
					CQRJigsawManager.LOGGER.warn("Empty or none existent pool: {}", (Object) resourcelocation);
				}
			}

		}
	}

	static final class Entry {
		private final AbstractVillagePiece piece;
		private final MutableObject<VoxelShape> free;
		private final int boundsTop;
		private final int depth;

		private Entry(AbstractVillagePiece pPiece, MutableObject<VoxelShape> pFree, int pBoundsTop, int pDepth) {
			this.piece = pPiece;
			this.free = pFree;
			this.boundsTop = pBoundsTop;
			this.depth = pDepth;
		}
	}

	public interface IPieceFactory {
		AbstractVillagePiece create(TemplateManager p_create_1_, JigsawPiece p_create_2_, BlockPos p_create_3_, int p_create_4_, Rotation p_create_5_, MutableBoundingBox p_create_6_);
	}
}