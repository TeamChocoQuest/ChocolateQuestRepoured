package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.levelgen.structure.Structure;

public interface INoiseAffectingStructurePiece {

	List<Structure> NOISE_AFFECTING_STRUCTURES = new ArrayList<>();

	double getContribution(int x, int y, int z);

}
