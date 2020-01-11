package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.util.EnumFacing;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public enum ESkyDirection {

	NORTH, EAST, SOUTH, WEST;

	public ESkyDirection getOpposite() {
		switch (this) {
		case EAST:
			return WEST;
		case NORTH:
			return SOUTH;
		case SOUTH:
			return NORTH;
		case WEST:
			return EAST;
		default:
			return null;
		}
	}

	public static ESkyDirection fromFacing(EnumFacing direction) {
		switch(direction) {
		case EAST:
			return EAST;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case WEST:
			return WEST;
		default:
			return null;
		}
	}
}
