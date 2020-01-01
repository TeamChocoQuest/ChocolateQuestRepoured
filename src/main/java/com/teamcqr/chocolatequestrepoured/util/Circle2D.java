package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * Copyright (c) 30.06.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 *
 * Description: Definition for a circle in a 2d pixel grid,
 * constructed using the Midpoint algorithm described here:
 * https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
 */

public class Circle2D {
	public int radius;

	public enum CircleRegion {
		OUTSIDE, EDGE, INSIDE
	}

	public class Coord {
		public int x; // center x of the circle
		public int z; // center y of the circle

		private Coord(int x, int z) {
			this.x = x;
			this.z = z;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Coord)) {
				return false;
			}
			Coord c = (Coord) o;
			return this.x == c.x && this.z == c.z;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.x, this.z);
		}
	}

	// The circle is described by two underlying hash sets of unique points, representing either the border circle
	// or the points that lie inside the border circle.
	private HashSet<Coord> fill; // The points that make up the inside of the circle
	private HashSet<Coord> border;

	// Points along the insideEdge of the circle but not quite to the border border. Subset of fill.
	private HashSet<Coord> insideEdge;

	public Circle2D(int centerX, int centerZ, int radius) {
		this.radius = radius;
		int innerRadius = radius - 1;
		int outerRadius = radius + 1;
		this.fill = new HashSet<>();
		this.insideEdge = new HashSet<>();
		this.border = new HashSet<>();

		for (int dX = -outerRadius; dX < outerRadius; dX++) {
			for (int dZ = -outerRadius; dZ < outerRadius; dZ++) {
				int distSq = dX * dX + dZ * dZ;
				if (distSq < radius * radius) {
					this.fill.add(new Coord(centerX + dX, centerZ + dZ));
					if (distSq > innerRadius * innerRadius) {
						this.insideEdge.add(new Coord(centerX + dX, centerZ + dZ));
					}
				} else if (distSq < outerRadius * outerRadius) {
					this.border.add(new Coord(centerX + dX, centerZ + dZ));
				}
			}
		}

	}

	// Get an array of the (x,z) coordinates that make up the inside of this circle
	public ArrayList<Coord> getFloorArray() {
		return new ArrayList<>(this.fill);
	}

	// Get an array of the (x,z) coordinates that make up the perimeter of this circle
	public ArrayList<Coord> getWallArray() {
		return new ArrayList<>(this.border);
	}

	// Get an array of the (x,z) coordinates that fall on the egde of the inside and perimeter
	public ArrayList<Coord> getEdgeArray() {
		return new ArrayList<>(this.insideEdge);
	}

	public boolean isCoordInFill(int x, int y) {
		return this.fill.contains(new Coord(x, y));
	}

	public boolean isCoordOnBorder(int x, int y) {
		return this.border.contains(new Coord(x, y));
	}

	public boolean isCoordOnInsideEdge(int x, int y) {
		return this.insideEdge.contains(new Coord(x, y));
	}

	public boolean isCoordInCircle(int x, int y) {
		Coord c = new Coord(x, y);
		return this.fill.contains(c) || this.border.contains(c);
	}

}
