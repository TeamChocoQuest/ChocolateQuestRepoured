package com.teamcqr.chocolatequestrepoured.structuregen.Generators.Volcano;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

public class StairCaseHelper {
	
	
	public enum EStairSection {
		/**
		 * North: -z
		 * East: +x
		 * South: +z
		 * West: -x
		 * 
		 * NE: z- x+
		 * SE: z+ x+
		 * SW: z+ x-
		 * NW: z- x-
		 */
		
		NORTH("NORTH_EAST"),
		NORTH_EAST("EAST_SEC"),
		EAST_SEC("EAST"),
		EAST("SOUTH_EAST"),
		SOUTH_EAST("SOUTH_SEC"),
		SOUTH_SEC("SOUTH"),
		SOUTH("SOUTH_WEST"),
		SOUTH_WEST("WEST_SEC"),
		WEST_SEC("WEST"),
		WEST("NORTH_WEST"),
		NORTH_WEST("NORTH_SEC"),
		NORTH_SEC("NORTH");

		private String successorName;
		
		private EStairSection(String successor) {
			this.successorName = successor;
		}
		
		public EStairSection getSuccessor() {
			return EStairSection.valueOf(this.successorName);
		}
		
		public EnumFacing getAsSkyDirection() {
			switch(this) {
			case EAST: case EAST_SEC:
				return EnumFacing.EAST;
			case NORTH: case NORTH_SEC:
				return EnumFacing.NORTH;
			case SOUTH: case SOUTH_SEC:
				return EnumFacing.SOUTH;
			case WEST: case WEST_SEC:
				return EnumFacing.WEST;
			default:
				return null;
			}
		}
	}
	
	public static EStairSection getRandomStartSection() {
		switch(new Random().nextInt(3)) {
		case 0:
			return EStairSection.NORTH_EAST;
		case 1:
			return EStairSection.SOUTH_EAST;
		/*case 2:
			return EStairSection.SOUTH_WEST;*/
		case 2:
			return EStairSection.NORTH_WEST;
		}
		return EStairSection.NORTH_EAST;
	}
	
	public static boolean isPillarCenterLocation(int x, int z, int radiusOfCircle) {
		//SOUTH & NORTH
		if(x == 0 && (Math.abs(z) == Math.abs(radiusOfCircle /2) )) {
			return true;
		}
		//EAST & WEST
		if(z == 0 && (Math.abs(x) == Math.abs(radiusOfCircle /2))) {
			return true;
		}
	
		return false;
	}
	
	public static EnumFacing getFacingWithRotation(EnumFacing orig, Rotation rotation) {
		EnumFacing ret = orig;
		switch(orig) {
		case EAST:
			switch(rotation) {
			case CLOCKWISE_180:
				ret = EnumFacing.WEST;
				break;
			case CLOCKWISE_90:
				ret = EnumFacing.SOUTH;
				break;
			case COUNTERCLOCKWISE_90:
				ret = EnumFacing.NORTH;
				break;
			default:
				break;
			}
			break;
		case NORTH:
			switch(rotation) {
			case CLOCKWISE_180:
				ret = EnumFacing.SOUTH;
				break;
			case CLOCKWISE_90:
				ret = EnumFacing.EAST;
				break;
			case COUNTERCLOCKWISE_90:
				ret = EnumFacing.WEST;
				break;
			default:
				break;
			}
			break;
		case SOUTH:
			switch(rotation) {
			case CLOCKWISE_180:
				ret = EnumFacing.NORTH;
				break;
			case CLOCKWISE_90:
				ret = EnumFacing.WEST;
				break;
			case COUNTERCLOCKWISE_90:
				ret = EnumFacing.EAST;
				break;
			default:
				break;
			}
			break;
		case WEST:
			switch(rotation) {
			case CLOCKWISE_180:
				ret = EnumFacing.EAST;
				break;
			case CLOCKWISE_90:
				ret = EnumFacing.NORTH;
				break;
			case COUNTERCLOCKWISE_90:
				ret = EnumFacing.SOUTH;
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
	
	//X,Z begin at -radius and ends at radius
	public static boolean isLocationFine(EStairSection section, int x, int z, int radiusOfCircle) {
		switch (section) {
		case NORTH:
			if(z < 0) {
				if(0 <= x && x <= (radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case NORTH_SEC:
			if(z <0) {
				if(-(radiusOfCircle /3) <= x && x <= 0) {
					return true;
				}
			}
			break;
		case SOUTH:
			if(z > 0) {
				if(-(radiusOfCircle /3) <= x && x <= 0) {
					return true;
				}
			}
			break;
		case SOUTH_SEC:
			if(z > 0) {
				if(0 <= x && x <= (radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case EAST:
			if(x > 0) {
				if(0 <= z && z <=  (radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case EAST_SEC:
			if(x > 0) {
				if(-(radiusOfCircle /3) <= z && z <=  0) {
					return true;
				}
			}
			break;
		case WEST:
			if(x < 0) {
				if(-(radiusOfCircle /3) <= z && z <=  0) {
					return true;
				}
			}
			break;
		case WEST_SEC:
			if(x < 0) {
				if(0 <= z && z <=  (radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case SOUTH_EAST:
			if(x > 0 && z > 0) {
				if(x >= (radiusOfCircle /3) && z >= (radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case SOUTH_WEST:
			if(x < 0 && z > 0) {
				if(x <= -(radiusOfCircle /3) && z >= (radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case NORTH_EAST:
			if(x > 0 && z < 0) {
				if(x >= (radiusOfCircle /3) && z <= -(radiusOfCircle /3)) {
					return true;
				}
			}
			break;
		case NORTH_WEST:
			if(x < 0 && z < 0) {
				if(x <= -(radiusOfCircle /3) && z <= -(radiusOfCircle /3)) {
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
