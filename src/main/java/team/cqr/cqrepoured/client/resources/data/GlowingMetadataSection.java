package team.cqr.cqrepoured.client.resources.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.JsonUtils;

public class GlowingMetadataSection implements IMetadataSection {

	private final List<Section> glowingSections;
	private final Set<String> glowingParts;

	public GlowingMetadataSection(Collection<Section> sections, Collection<String> parts) {
		this.glowingSections = new ArrayList<>(sections);
		this.glowingParts = new HashSet<>(parts);
	}

	public boolean isEmpty() {
		return this.glowingSections.isEmpty();
	}

	public Collection<Section> getGlowingSections() {
		return Collections.unmodifiableCollection(this.glowingSections);
	}

	public Collection<String> getGlowingParts() {
		return Collections.unmodifiableCollection(glowingParts);
	}

	public static class Section {

		private final int minX;
		private final int minY;
		private final int maxX;
		private final int maxY;

		public Section(JsonObject jsonObject) {
			this(JsonUtils.getInt(jsonObject, "x1"), JsonUtils.getInt(jsonObject, "y1"), JsonUtils.getInt(jsonObject, "x2"),
					JsonUtils.getInt(jsonObject, "y2"));
		}

		public Section(int x1, int y1, int x2, int y2) {
			this.minX = Math.min(x1, x2);
			this.minY = Math.min(y1, y2);
			this.maxX = Math.max(x1, x2);
			this.maxY = Math.max(y1, y2);
		}

		public int getMinX() {
			return minX;
		}

		public int getMinY() {
			return minY;
		}

		public int getMaxX() {
			return maxX;
		}

		public int getMaxY() {
			return maxY;
		}

	}

}
