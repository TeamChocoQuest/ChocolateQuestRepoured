package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.Volcano;

import java.util.Random;

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
	}
	//N, S , W (is missing ?!?!): Kinda wrong....
	public static EStairSection getRandomStartSection() {
		switch(new Random().nextInt(4)) {
		case 0:
			return EStairSection.NORTH_EAST;
		case 1:
			return EStairSection.SOUTH_EAST;
		case 2:
			return EStairSection.SOUTH_WEST;
		case 3:
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
