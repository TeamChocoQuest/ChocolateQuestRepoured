package team.cqr.cqrepoured.mixin.structure;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.INoiseAffectingStructurePiece;

@Mixin(NoiseChunkGenerator.class)
public abstract class MixinNoiseChunkGenerator {

	@Unique
	private List<INoiseAffectingStructurePiece> noiseAffectingStructurePieces;
	@Unique
	private int x;
	@Unique
	private int y;
	@Unique
	private int z;

	@Inject(method = "fillFromNoise", at = @At("HEAD"))
	private void pre_fillFromNoise(Level level, StructureManager structureManager, IChunk chunk, CallbackInfo info) {
		ChunkPos chunkPos = chunk.getPos();
		SectionPos sectionPos = SectionPos.of(chunkPos, 0);
		noiseAffectingStructurePieces = INoiseAffectingStructurePiece.NOISE_AFFECTING_STRUCTURES.stream()
				.flatMap(structure -> structureManager.startsForFeature(sectionPos, structure))
				.map(StructureStart::getPieces)
				.flatMap(List::stream)
				.filter(INoiseAffectingStructurePiece.class::isInstance)
				.filter(structurePiece -> {
					//Ignore structures that don't have support hills enabled
					if(structurePiece instanceof GeneratableDungeon) {
						GeneratableDungeon gd = (GeneratableDungeon) structurePiece;
						Optional<DungeonBase> optDunBase = gd.getDungeonConfig();
						
						if(optDunBase.isPresent()) {
							return optDunBase.get().doBuildSupportPlatform();
						}
					}
					return true;
				})
				.map(INoiseAffectingStructurePiece.class::cast)
				.collect(Collectors.toList());
	}

	@ModifyVariable(method = "fillFromNoise", at = @At(value = "INVOKE", target = "back", remap = false, ordinal = 1, shift = Shift.AFTER), index = 53, ordinal = 12, name = "i3")
	private int get_i3(int i3) {
		x = i3;
		return i3;
	}

	@ModifyVariable(method = "fillFromNoise", at = @At(value = "INVOKE", target = "back", remap = false, ordinal = 1, shift = Shift.AFTER), index = 39, ordinal = 8, name = "i2")
	private int get_i2(int i2) {
		y = i2;
		return i2;
	}

	@ModifyVariable(method = "fillFromNoise", at = @At(value = "INVOKE", target = "back", remap = false, ordinal = 1, shift = Shift.AFTER), index = 62, ordinal = 15, name = "l3")
	private int get_l3(int l3) {
		z = l3;
		return l3;
	}

	@ModifyVariable(method = "fillFromNoise", at = @At(value = "INVOKE", target = "back", remap = false, ordinal = 1, shift = Shift.AFTER), index = 68, ordinal = 18, name = "d18")
	private double set_d18(double d18) {
		for (INoiseAffectingStructurePiece structure : noiseAffectingStructurePieces) {
			d18 += structure.getContribution(x, y, z);
		}
		return d18;
	}

	@Inject(method = "fillFromNoise", at = @At("RETURN"))
	private void post_fillFromNoise(Level level, StructureManager structureManager, IChunk chunk, CallbackInfo info) {
		noiseAffectingStructurePieces = null;
	}

}
