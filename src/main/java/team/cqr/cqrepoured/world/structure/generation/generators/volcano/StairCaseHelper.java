package team.cqr.cqrepoured.world.structure.generation.generators.volcano;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

import javax.annotation.Nullable;
import java.util.Random;

public class StairCaseHelper {

	// Sky direction here: In which direction is the staircase when looked at from the volcano's center
	public enum EStairSection {
		/**
		 * North: -z East: +x South: +z West: -x
		 * 
		 * NE: z- x+ SE: z+ x+ SW: z+ x- NW: z- x-
		 */

		NORTH("NORTH_EAST"), NORTH_EAST("EAST_SEC"), EAST_SEC("EAST"), EAST("SOUTH_EAST"), SOUTH_EAST("SOUTH_SEC"), SOUTH_SEC("SOUTH"), SOUTH("SOUTH_WEST"), SOUTH_WEST("WEST_SEC"), WEST_SEC("WEST"), WEST("NORTH_WEST"), NORTH_WEST("NORTH_SEC"), NORTH_SEC("NORTH");

		private String successorName;

		EStairSection(String successor) {
			this.successorName = successor;
		}

		public EStairSection getSuccessor() {
			return EStairSection.valueOf(this.successorName);
		}

		@Nullable
		public EStairSection getPredeccessor() {
			for (EStairSection section : EStairSection.values()) {
				if (section.successorName.equalsIgnoreCase(this.name())) {
					return section;
				}
			}
			return null;
		}

		public Direction getAsSkyDirection() {
			switch (this) {
			case EAST:
			case EAST_SEC:
				return Direction.EAST;
			case NORTH:
			case NORTH_SEC:
				return Direction.NORTH;
			case SOUTH:
			case SOUTH_SEC:
				return Direction.SOUTH;
			case WEST:
			case WEST_SEC:
				return Direction.WEST;
			default:
				return this.getPredeccessor().getAsSkyDirection();
			}
		}
	}

	public static EStairSection getRandomStartSection() {
		switch (new Random().nextInt(4)) {
		case 0:
			return EStairSection.NORTH_EAST;
		case 1:
			return EStairSection.SOUTH_EAST;
		case 2:
			return EStairSection.NORTH_WEST;
		case 3:
			return EStairSection.SOUTH_WEST;
		}
		return EStairSection.SOUTH_WEST;
	}

	public static boolean isPillarCenterLocation(int x, int z, int radiusOfCircle) {
		// SOUTH & NORTH
		if (x == 0 && (Math.abs(z) == Math.abs(radiusOfCircle / 2))) {
			return true;
		}
		// EAST & WEST
		if (z == 0 && (Math.abs(x) == Math.abs(radiusOfCircle / 2))) {
			return true;
		}

		return false;
	}

	public static Direction getFacingWithRotation(Direction orig, Rotation rotation) {
		Direction ret = orig;
		switch (orig) {
		case EAST:
			switch (rotation) {
			case CLOCKWISE_180:
				ret = Direction.WEST;
				break;
			case CLOCKWISE_90:
				ret = Direction.SOUTH;
				break;
			case COUNTERCLOCKWISE_90:
				ret = Direction.NORTH;
				break;
			default:
				break;
			}
			break;
		case NORTH:
			switch (rotation) {
			case CLOCKWISE_180:
				ret = Direction.SOUTH;
				break;
			case CLOCKWISE_90:
				ret = Direction.EAST;
				break;
			case COUNTERCLOCKWISE_90:
				ret = Direction.WEST;
				break;
			default:
				break;
			}
			break;
		case SOUTH:
			switch (rotation) {
			case CLOCKWISE_180:
				ret = Direction.NORTH;
				break;
			case CLOCKWISE_90:
				ret = Direction.WEST;
				break;
			case COUNTERCLOCKWISE_90:
				ret = Direction.EAST;
				break;
			default:
				break;
			}
			break;
		case WEST:
			switch (rotation) {
			case CLOCKWISE_180:
				ret = Direction.EAST;
				break;
			case CLOCKWISE_90:
				ret = Direction.NORTH;
				break;
			case COUNTERCLOCKWISE_90:
				ret = Direction.SOUTH;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return ret;
	}

	// X,Z begin at -radius and ends at radius
	public static boolean isLocationFine(EStairSection section, int x, int z, int radiusOfCircle) {
		switch (section) {
		case NORTH:
			if (z < 0) {
				if (0 <= x && x <= (radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case NORTH_SEC:
			if (z < 0) {
				if (-(radiusOfCircle / 3) <= x && x <= 0) {
					return true;
				}
			}
			break;
		case SOUTH:
			if (z > 0) {
				if (-(radiusOfCircle / 3) <= x && x <= 0) {
					return true;
				}
			}
			break;
		case SOUTH_SEC:
			if (z > 0) {
				if (0 <= x && x <= (radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case EAST:
			if (x > 0) {
				if (0 <= z && z <= (radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case EAST_SEC:
			if (x > 0) {
				if (-(radiusOfCircle / 3) <= z && z <= 0) {
					return true;
				}
			}
			break;
		case WEST:
			if (x < 0) {
				if (-(radiusOfCircle / 3) <= z && z <= 0) {
					return true;
				}
			}
			break;
		case WEST_SEC:
			if (x < 0) {
				if (0 <= z && z <= (radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case SOUTH_EAST:
			if (x > 0 && z > 0) {
				if (x >= (radiusOfCircle / 3) && z >= (radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case SOUTH_WEST:
			if (x < 0 && z > 0) {
				if (x <= -(radiusOfCircle / 3) && z >= (radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case NORTH_EAST:
			if (x > 0 && z < 0) {
				if (x >= (radiusOfCircle / 3) && z <= -(radiusOfCircle / 3)) {
					return true;
				}
			}
			break;
		case NORTH_WEST:
			if (x < 0 && z < 0) {
				if (x <= -(radiusOfCircle / 3) && z <= -(radiusOfCircle / 3)) {
					return true;
				}
			}
			break;

		default:
			return false;
		}
		return false;
	}

}
