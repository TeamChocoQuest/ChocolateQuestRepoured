package team.cqr.cqrepoured.client.structureprot;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProtectedRegionIndicator {

	private final UUID uuid;
	private BlockPos start;
	private BlockPos end;
	private int lifeTime = 60;

	public ProtectedRegionIndicator(UUID uuid, BlockPos start, BlockPos end) {
		this.uuid = uuid;
		this.start = start;
		this.end = end;
	}

	public void update() {
		this.lifeTime--;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public void setStart(BlockPos start) {
		this.start = start;
	}

	public BlockPos getStart() {
		return this.start;
	}

	public void setEnd(BlockPos end) {
		this.end = end;
	}

	public BlockPos getEnd() {
		return this.end;
	}

	public void resetLifeTime() {
		this.lifeTime = 60;
	}

	public int getLifeTime() {
		return this.lifeTime;
	}

}
