package team.cqr.cqrepoured.client.mcmetaserializers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.Tuple;

public class GlowingMetadataSection implements IMetadataSection {

	private List<Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>>> glowingSections = new ArrayList<>();

	public List<Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>>> getGlowingSections() {
		return this.glowingSections;
	}

	public void addSection(Tuple<Integer, Integer> pos1, Tuple<Integer, Integer> pos2) {
		Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> entry = new Tuple<>(pos1, pos2);
		this.glowingSections.add(entry);
	}

	public boolean isEmpty() {
		return this.glowingSections.isEmpty();
	}

}
