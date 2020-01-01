package com.teamcqr.chocolatequestrepoured.util;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
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
}
