package team.cqr.cqrepoured.generation.util;

import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;

public class BlockStatePaletteUtil {

	public static ListTag writeSimplePalette(SimplePalette palette) {
		ListTag listTag = new ListTag();
		palette.forEach(blockState -> listTag.add(NbtUtils.writeBlockState(blockState)));
		return listTag;
	}

	public static SimplePalette readSimplePalette(HolderGetter<Block> blockGetter, ListTag tag) {
		SimplePalette palette = new SimplePalette();
		for (int i = 0; i < tag.size(); i++) {
			palette.addMapping(NbtUtils.readBlockState(blockGetter, tag.getCompound(i)), i);
		}
		return palette;
	}

}
