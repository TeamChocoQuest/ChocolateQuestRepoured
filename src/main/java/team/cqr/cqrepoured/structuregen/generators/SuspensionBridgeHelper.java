package team.cqr.cqrepoured.structuregen.generators;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerator;

public class SuspensionBridgeHelper {

	private float tension = 5.0F;
	private int width = 5;

	private final BlockPos startPos, endPos;
	
	private IBlockState pathBlock, fenceBlock, railingBlock, anchorBlock;

	/*
	 * @param bridgePoints needs to contain at least two elements
	 */
	public SuspensionBridgeHelper(float tension, int width, BlockPos start, BlockPos end, IBlockState pathBlock, IBlockState fenceBlock, IBlockState railingBlock, IBlockState anchorBlock) {
		this.tension = tension;
		this.width = width;
		this.pathBlock = pathBlock;
		this.fenceBlock = fenceBlock;
		this.anchorBlock = anchorBlock;
		this.railingBlock = railingBlock;
		
		this.startPos = start;
		this.endPos = end;
	}

	public boolean generate(final DungeonGenerator dungeonGenerator) {
		Map<BlockPos, IBlockState> stateMap = new HashMap<>();
			this.saggyPath(this.startPos, this.endPos, stateMap);

		return true;
	}

	private void saggyPath(BlockPos currentPos, BlockPos nextPos, Map<BlockPos, IBlockState> stateMap) {
		double dx = nextPos.getX() - currentPos.getX();
		double dy = nextPos.getY() - currentPos.getY();
		double dz = nextPos.getZ() - currentPos.getZ();

		double distHorizontal = dx * dx + dz * dz;
		double distance = Math.sqrt(dy * dy + distHorizontal);

		double phi = Math.atan2(dy, Math.sqrt(distHorizontal));
		double theta = Math.atan2(dz, dx);
		double toTheRight = theta + Math.PI / 2;
		double toTheLeft = theta - Math.PI / 2;

		double lx = (currentPos.getX() + (this.width / 2) * Math.cos(toTheLeft));
		double lz = (currentPos.getZ() + (this.width / 2) * Math.sin(toTheLeft));
		double rx = (currentPos.getX() + (this.width / 2) * Math.cos(toTheRight));
		double rz = (currentPos.getZ() + (this.width / 2) * Math.sin(toTheRight));

		double ddx = rx - lx;
		double ddz = rz - lz;

		double dist = Math.ceil(Math.sqrt(ddx * ddx + ddz * ddz));

		double dthetax = 1, dthetaz = 1;
		
		double i = 0;
		while (i <= dist) {
			dthetax = Math.cos(toTheRight);
			dthetaz = Math.sin(toTheRight);

			double startX = lx + dthetax * (double) i;
			double startZ = lz + dthetaz * (double) i;

			this.drawSaggyArc(this.pathBlock, new BlockPos(startX, currentPos.getY(), startZ), theta, phi, distance, stateMap);

			i += 0.5;
		}
		
		for(int iterY = 1; iterY <= 2; iterY++) {
			this.drawSaggyArc(this.fenceBlock, new BlockPos(lx, currentPos.getY() + iterY, lz), theta, phi, distance, stateMap);
			this.drawSaggyArc(this.fenceBlock, new BlockPos(lx + dthetax * dist, currentPos.getY() + iterY, lz + dthetaz * dist), theta, phi, distance, stateMap);
		}
		for(int iterY = 2; iterY <= 3; iterY++) {
			this.drawSaggyArc(this.railingBlock, new BlockPos(lx, currentPos.getY() + iterY, lz), theta, phi, distance, stateMap);
			this.drawSaggyArc(this.railingBlock, new BlockPos(lx + dthetax * dist, currentPos.getY() + iterY, lz + dthetaz * dist), theta, phi, distance, stateMap);
		}
		
		BlockPos lxPos = new BlockPos(lx, currentPos.getY(), lz);
		
		this.drawLine(this.anchorBlock, lxPos, lxPos.add(0, 2, 0), stateMap);
		this.drawLine(this.anchorBlock, lxPos.add(dthetax * dist, 0, dthetax * dist), lxPos.add(dthetax * dist, 2, dthetax * dist), stateMap);
		this.drawLine(this.anchorBlock, lxPos.add(distance * Math.cos(theta) * Math.cos(phi), distance * Math.sin(phi), distance * Math.sin(theta) * Math.cos(phi)), lxPos.add(distance * Math.cos(theta) * Math.cos(phi), 3 + distance * Math.sin(phi), distance * Math.sin(theta) * Math.cos(phi)), stateMap);
		this.drawLine(this.anchorBlock, lxPos.add(dthetax * dist + distance * Math.cos(theta) * Math.cos(phi), distance * Math.sin(phi), dthetaz * dist + distance * Math.sin(theta) * Math.cos(phi)), lxPos.add(distance * Math.cos(theta) * Math.cos(phi), 3 + distance * Math.sin(phi), distance * Math.sin(theta) * Math.cos(phi)), stateMap);
	}

	private void drawSaggyArc(IBlockState material, BlockPos pos, double theta, double phi, double distance, Map<BlockPos, IBlockState> stateMap) {
		double midPoint = distance / 2;
		double scale = distance / this.tension;

		BlockPos p = new BlockPos(0, 0, 0);
		double iterator = 0;
		while (iterator <= distance) {
			double xx = (iterator - midPoint) / midPoint;
			double ddy = xx * xx * scale;
			BlockPos n = new BlockPos((int) (pos.getX() + iterator * Math.cos(theta) * Math.cos(phi)), (int) (pos.getY() + iterator * Math.sin(phi) + ddy - scale), (int) (pos.getZ() + iterator * Math.sin(theta) * Math.cos(phi)));
			if (p.getDistance(0, 0, 0) != 0.0) {
				this.drawLine(material, p, n , stateMap);
			}

			p = n;
			iterator += 0.5;
		}
	}

	private void drawLine(IBlockState material, BlockPos p, BlockPos n, Map<BlockPos, IBlockState> stateMap) {
		this.drawLineConstrained(material, p, n, stateMap, 0);
	}

	private void drawLineConstrained(IBlockState material, BlockPos p, BlockPos n, Map<BlockPos, IBlockState> stateMap, int maxLength) {
		int dx = n.getX() - p.getX();
		int dy = n.getY() - p.getY();
		int dz = n.getZ() - p.getZ();
		
		double distHoriz = dx * dx + dz * dz;
		double distance = Math.sqrt(dy * dy + distHoriz);
		
		if(distance < maxLength || maxLength < 1) {
			double phi = Math.atan2(dy, Math.sqrt(distHoriz));
			double theta = Math.atan2(dz, dx);
			
			double iterator = 0;
			while(iterator <= distance) {
				stateMap.put(p.add(iterator * Math.cos(theta) * Math.cos(phi), iterator * Math.sin(phi), iterator * Math.sin(theta)*Math.cos(phi)), material);
				iterator += 0.5D;
			}
		}
	}

}
