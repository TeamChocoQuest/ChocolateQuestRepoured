package com.teamcqr.chocolatequestrepoured.util.math;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class BoundingBox {

	private final Vec3d[] vertices;
	private final Line[] edges;
	private final Square[] planes;
	private double minX;
	private double minY;
	private double minZ;
	private double maxX;
	private double maxY;
	private double maxZ;
	private final AxisAlignedBB aabb;

	public BoundingBox(Vec3d[] vertices, int[][] edges, int[][] planes) {
		this.vertices = vertices;
		for (Vec3d vec : vertices) {
			this.minX = Math.min(this.minX, vec.x);
			this.minY = Math.min(this.minY, vec.y);
			this.minZ = Math.min(this.minZ, vec.z);
			this.maxX = Math.max(this.maxX, vec.x);
			this.maxY = Math.max(this.maxY, vec.y);
			this.maxZ = Math.max(this.maxZ, vec.z);
		}
		this.aabb = new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
		this.edges = new Line[edges.length];
		for (int i = 0; i < edges.length; i++) {
			this.edges[i] = new Line(vertices[edges[i][0]], vertices[edges[i][1]]);
		}
		this.planes = new Square[planes.length];
		for (int i = 0; i < planes.length; i++) {
			this.planes[i] = new Square(vertices[planes[i][0]], vertices[planes[i][1]], vertices[planes[i][2]], vertices[planes[i][3]]);
		}
	}

	/**
	 * Example for a 1x1x1 cube: { (0,0,0), (1,0,0), (0,0,1), (1,0,1), (0,1,0), (1,1,0), (0,1,1), (1,1,1) }
	 */
	public BoundingBox(Vec3d[] vertices) {
		this(vertices, new int[][] { { 0, 1 }, { 1, 3 }, { 3, 2 }, { 2, 0 }, { 0, 4 }, { 1, 5 }, { 3, 7 }, { 2, 6 }, { 4, 5 }, { 5, 7 }, { 7, 6 }, { 6, 4 } }, new int[][] { { 0, 2, 1, 3 }, { 0, 1, 4, 5 }, { 1, 3, 5, 7 }, { 3, 2, 7, 6 }, { 2, 0, 6, 4 }, { 4, 5, 6, 7 } });
	}

	public BoundingBox(AxisAlignedBB aabb) {
		this(new Vec3d[] {
				new Vec3d(aabb.minX, aabb.minY, aabb.minZ),
				new Vec3d(aabb.maxX, aabb.minY, aabb.minZ),
				new Vec3d(aabb.minX, aabb.minY, aabb.maxZ),
				new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ),
				new Vec3d(aabb.minX, aabb.maxY, aabb.minZ),
				new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ),
				new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ),
				new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ) });
	}

	public boolean isVecInside(Vec3d vec) {
		for (Square plane : this.planes) {
			if (!plane.isVecBehindPlane(vec)) {
				return false;
			}
		}
		return true;
	}

	public Vec3d[] getVertices() {
		Vec3d[] copy = new Vec3d[this.vertices.length];
		System.arraycopy(this.vertices, 0, copy, 0, this.vertices.length);
		return copy;
	}

	public Line[] getEdges() {
		Line[] copy = new Line[this.edges.length];
		System.arraycopy(this.edges, 0, copy, 0, this.edges.length);
		return copy;
	}

	public Square[] getPlanes() {
		Square[] copy = new Square[this.planes.length];
		System.arraycopy(this.planes, 0, copy, 0, this.planes.length);
		return copy;
	}

	public AxisAlignedBB getAabb() {
		return this.aabb;
	}

}
