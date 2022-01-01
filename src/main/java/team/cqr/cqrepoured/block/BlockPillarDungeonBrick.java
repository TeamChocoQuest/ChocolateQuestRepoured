package team.cqr.cqrepoured.block;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockPillarDungeonBrick extends RotatedPillarBlock {

	public BlockPillarDungeonBrick() {
		super(Material.ROCK);

		this.setSoundType(SoundType.STONE);
		this.setHardness(1.5F);
		this.setResistance(10.0F);
	}

}
