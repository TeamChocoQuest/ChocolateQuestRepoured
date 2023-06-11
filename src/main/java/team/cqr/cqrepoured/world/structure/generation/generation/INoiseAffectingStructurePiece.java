package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.gen.feature.structure.Structure;

public interface INoiseAffectingStructurePiece {

	List<Structure<?>> NOISE_AFFECTING_STRUCTURES = new ArrayList<>();

	double getContribution(int x, int y, int z);

}
