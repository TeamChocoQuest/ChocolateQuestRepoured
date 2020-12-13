package team.cqr.cqrepoured.client.structureprot;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;

@SideOnly(Side.CLIENT)
public class ProtectedRegionIndicator {

	private ProtectedRegion protectedRegion;
	private int lifeTime = 60;

	public ProtectedRegionIndicator(ProtectedRegion protectedRegion) {
		this.protectedRegion = protectedRegion;
	}

	public void update() {
		this.lifeTime--;
	}

	public ProtectedRegion getProtectedRegion() {
		return this.protectedRegion;
	}

	public void resetLifeTime() {
		this.lifeTime = 60;
	}

	public int getLifeTime() {
		return this.lifeTime;
	}

}
