package team.cqr.cqrepoured.generation.mixin;

import java.util.Collection;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.INoiseAffectingStructurePiece;

@Mixin(Beardifier.class)
public class MixinBeardifier {

	@Unique
	private ObjectListIterator<INoiseAffectingStructurePiece> noiseAffectingStructurePieces;
	@Unique
	private DensityFunction.FunctionContext context;

	@Inject(method = "forStructuresInChunk", at = @At("RETURN"))
	private static void forStructuresInChunk(StructureManager pStructureManager, ChunkPos pChunkPos, CallbackInfoReturnable<Beardifier> info) {
		((MixinBeardifier) (Object) info.getReturnValue()).noiseAffectingStructurePieces = pStructureManager.startsForStructure(pChunkPos, structure -> true)
				.stream()
				.map(StructureStart::getPieces)
				.flatMap(Collection::stream)
				.filter(INoiseAffectingStructurePiece.class::isInstance)
				.map(INoiseAffectingStructurePiece.class::cast)
				.collect(Collectors.toCollection(ObjectArrayList::new))
				.iterator();
	}

	@ModifyVariable(method = "compute", at = @At(value = "RETURN", shift = Shift.BEFORE), index = 1, ordinal = 0, name = "pContext")
	private DensityFunction.FunctionContext compute0(DensityFunction.FunctionContext context) {
		this.context = context;
		return context;
	}

	@ModifyVariable(method = "compute", at = @At(value = "RETURN", shift = Shift.BEFORE), index = 5, ordinal = 0, name = "d0")
	private double compute(double d0) {
		while (noiseAffectingStructurePieces.hasNext()) {
			INoiseAffectingStructurePiece noiseAffectingStructurePiece = noiseAffectingStructurePieces.next();
			d0 += noiseAffectingStructurePiece.getContribution(context.blockX(), context.blockY(), context.blockZ());
		}
		noiseAffectingStructurePieces.back(Integer.MAX_VALUE);
		return d0;
	}

	@ModifyVariable(method = "compute", at = @At(value = "RETURN", shift = Shift.BEFORE), index = 1, ordinal = 0, name = "pContext")
	private DensityFunction.FunctionContext compute1(DensityFunction.FunctionContext context) {
		this.context = null;
		return context;
	}

}
