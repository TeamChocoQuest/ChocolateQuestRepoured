package team.cqr.cqrepoured.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDungeonBrick extends Block {

	public BlockDungeonBrick() {
		this(Properties.
				of(Material.STONE)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 10.0F)
		);
	}
	
	protected BlockDungeonBrick(Properties prop) {
		super(prop);
	}

}
