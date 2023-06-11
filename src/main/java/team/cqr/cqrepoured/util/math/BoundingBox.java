package team.cqr.cqrepoured.util.math;

import java.util.List;

import javax.annotation.Nullable;

import org.joml.Vector3d;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class BoundingBox {

	private final Vector3d[] vertices;
	private final Line[] edges;
	private final Square[] planes;
	private double minX;
	private double minY;
	private double minZ;
	private double maxX;
	private double maxY;
	private double maxZ;
	private final AxisAlignedBB aabb;

	public BoundingBox(Vector3d[] vertices, int[][] edges, int[][] planes) {
		this.vertices = vertices;
		this.minX = vertices[0].x;
		this.minY = vertices[0].y;
		this.minZ = vertices[0].z;
		this.maxX = vertices[0].x;
		this.maxY = vertices[0].y;
		this.maxZ = vertices[0].z;
		for (int i = 1; i < vertices.length; i++) {
			Vector3d vec = vertices[i];
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

	public BoundingBox(Vector3d[] vertices) {
		this(vertices, new int[][] { { 0, 1 }, { 1, 3 }, { 3, 2 }, { 2, 0 }, { 0, 4 }, { 1, 5 }, { 3, 7 }, { 2, 6 }, { 4, 5 }, { 5, 7 }, { 7, 6 }, { 6, 4 } }, new int[][] { { 2, 3, 0, 1 }, { 0, 1, 4, 5 }, { 1, 3, 5, 7 }, { 3, 2, 7, 6 }, { 2, 0, 6, 4 }, { 4, 5, 6, 7 } });
	}

	/**
	 * x = right<br>
	 * y = up<br>
	 * z = forward
	 */
	public BoundingBox(Vector3d start, Vector3d end, double yaw, double pitch, Vector3d origin) {
		this(new Vector3d[] {
				rotatePitchYaw(start, (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(new Vector3d(end.x, start.y, start.z), (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(new Vector3d(start.x, start.y, end.z), (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(new Vector3d(end.x, start.y, end.z), (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(new Vector3d(start.x, end.y, start.z), (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(new Vector3d(end.x, end.y, start.z), (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(new Vector3d(start.x, end.y, end.z), (float) -pitch, (float) -yaw).add(origin),
				rotatePitchYaw(end, (float) -pitch, (float) -yaw).add(origin) });
	}

	public BoundingBox(AxisAlignedBB aabb, double yaw, double pitch, Vector3d origin) {
		this(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ), yaw, pitch, origin);
	}

	public boolean isVecInside(Vector3d vec) {
		for (Square plane : this.planes) {
			if (!plane.isVecBehindPlane(vec)) {
				return false;
			}
		}
		return true;
	}

	public Vector3d[] getVertices() {
		Vector3d[] copy = new Vector3d[this.vertices.length];
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

	private static Vector3d rotatePitchYaw(Vector3d vec, float pitch, float yaw) {
		return rotateYaw(rotatePitch(vec, pitch), yaw);
	}

	private static Vector3d rotatePitch(Vector3d vec, float pitch) {
		//TODO: Is this x or z axis?
		return Math.abs(pitch) > 1.0E-4 ? vec.zRot(pitch) : vec;
	}

	private static Vector3d rotateYaw(Vector3d vec, float yaw) {
		return Math.abs(yaw) > 1.0E-4 ? vec.yRot(yaw) : vec;
	}

	public static <T extends Entity> List<T> getEntitiesInsideBB(World world, @Nullable T toIgnore, Class<T> entityClass, BoundingBox bb1) {
		return world.getEntitiesOfClass(entityClass, bb1.getAabb(), input -> {
			if (input == toIgnore) {
				return false;
			}
			BoundingBox bb2 = new BoundingBox(input.getBoundingBox(), 0.0D, 0.0D, Vector3d.ZERO);
			for (Vector3d vertice : bb2.vertices) {
				if (bb1.isVecInside(vertice)) {
					return true;
				}
			}
			for (Vector3d vertice : bb1.vertices) {
				if (bb2.isVecInside(vertice)) {
					return true;
				}
			}
			if (checkIfEdgeHitsPlane(bb1, bb2)) {
				return true;
			}
			// return checkIfEdgeHitsPlane(bb2, bb1);
			return false;
		});
	}

	private static boolean checkIfEdgeHitsPlane(BoundingBox bb1, BoundingBox bb2) {
		for (Line edge : bb1.edges) {
			Vector3d lineDirection = edge.vec2.subtract(edge.vec1);
			Vector3d lineDirectionNormalized = lineDirection.normalize();

			for (Square plane : bb2.planes) {
				Vector3d planeNormal = plane.vec2.subtract(plane.vec1).cross(plane.vec3.subtract(plane.vec1));
				if (planeNormal.dot(lineDirectionNormalized) == 0.0D) {
					continue;
				}

				Vector3d vec = lineDirectionNormalized.scale((planeNormal.dot(plane.vec1) - planeNormal.dot(edge.vec1)) / planeNormal.dot(lineDirectionNormalized));
				if (vec.x < 0 != lineDirection.x < 0 || vec.y < 0 != lineDirection.y < 0 || vec.z < 0 != lineDirection.z < 0 || vec.lengthSqr() > lineDirection.lengthSqr()) {
					continue;
				}

				Vector3d intersectionPoint = edge.vec1.add(vec);
				int i1 = (int) (1000 * getAreaOfTriangle(intersectionPoint, plane.vec1, plane.vec2)) + (int) (1000 * getAreaOfTriangle(intersectionPoint, plane.vec2, plane.vec4)) + (int) (1000 * getAreaOfTriangle(intersectionPoint, plane.vec4, plane.vec3))
						+ (int) (1000 * getAreaOfTriangle(intersectionPoint, plane.vec3, plane.vec1));
				int i2 = (int) (1000 * getAreaOfTriangle(plane.vec1, plane.vec2, plane.vec3)) + (int) (1000 * getAreaOfTriangle(plane.vec2, plane.vec3, plane.vec4));
				if (i1 <= i2 + 100) {
					return true;
				}
			}
		}
		return false;
	}

	private static double getAreaOfTriangle(Vector3d vec1, Vector3d vec2, Vector3d vec3) {
		Vector3d v1 = vec2.subtract(vec1);
		Vector3d v2 = vec3.subtract(vec1);
		return 0.5D * v1.cross(v2).length();
	}

}
