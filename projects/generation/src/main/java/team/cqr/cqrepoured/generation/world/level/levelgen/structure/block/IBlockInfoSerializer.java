package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;

public interface IBlockInfoSerializer<T extends IBlockInfo> {

	void write(T blockInfo, DataOutput out, SimplePalette palette) throws IOException;

	T read(DataInput in, SimplePalette palette) throws IOException;

}
