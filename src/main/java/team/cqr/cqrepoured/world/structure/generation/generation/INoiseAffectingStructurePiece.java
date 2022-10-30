package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.Collections;
import java.util.List;

import net.minecraft.world.gen.feature.structure.Structure;

public interface INoiseAffectingStructurePiece {

	List<Structure<?>> NOISE_AFFECTING_STRUCTURES = Collections.emptyList();

	double getContribution(int x, int y, int z);

}
