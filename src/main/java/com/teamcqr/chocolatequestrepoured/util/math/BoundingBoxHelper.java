package com.teamcqr.chocolatequestrepoured.util.math;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BoundingBoxHelper {

	public static <T extends Entity> List<T> getEntitiesInsideBB(World world, @Nullable T toIgnore, Class<? extends T> entityClass, BoundingBox bb1) {
		return world.getEntitiesWithinAABB(entityClass, bb1.getAabb(), input -> {
			if (input == toIgnore) {
				return false;
			}
			BoundingBox bb2 = new BoundingBox(input.getEntityBoundingBox());
			for (Vec3d vertice : bb2.getVertices()) {
				if (bb1.isVecInside(vertice)) {
					return true;
				}
			}
			if (checkIfEdgeHitsPlane(bb1, bb2)) {
				return true;
			}
			return checkIfEdgeHitsPlane(bb2, bb1);
		});
	}

	public static boolean checkIfEdgeHitsPlane(BoundingBox bb1, BoundingBox bb2) {
		for (Line edge : bb1.getEdges()) {
			Vec3d lineDirection = edge.vec2.subtract(edge.vec1);
			Vec3d lineDirectionNormalized = lineDirection.normalize();

			for (Square plane : bb2.getPlanes()) {
				Vec3d planeNormal = plane.vec2.subtract(plane.vec1).crossProduct(plane.vec3.subtract(plane.vec1));
				if (planeNormal.dotProduct(lineDirectionNormalized) == 0.0D) {
					break;
				}

				Vec3d vec = lineDirectionNormalized.scale((planeNormal.dotProduct(plane.vec1) - planeNormal.dotProduct(edge.vec1)) / planeNormal.dotProduct(lineDirectionNormalized));
				if (vec.x < 0 != lineDirection.x < 0 || vec.y < 0 != lineDirection.y < 0 || vec.z < 0 != lineDirection.z < 0 || vec.lengthSquared() > lineDirection.lengthSquared()) {
					break;
				}

				Vec3d intersectionPoint = edge.vec1.add(vec);
				int i1 = (int) (10000 * (getAreaOfTriangle(intersectionPoint, plane.vec1, plane.vec2) + getAreaOfTriangle(intersectionPoint, plane.vec2, plane.vec3) + getAreaOfTriangle(intersectionPoint, plane.vec3, plane.vec4) + getAreaOfTriangle(intersectionPoint, plane.vec4, plane.vec1)));
				int i2 = (int) (10000 * (getAreaOfTriangle(plane.vec1, plane.vec2, plane.vec3) + getAreaOfTriangle(plane.vec2, plane.vec3, plane.vec4)));
				if (i1 <= i2) {
					return true;
				}
			}
		}
		return false;
	}

	public static double getAreaOfTriangle(Vec3d vec1, Vec3d vec2, Vec3d vec3) {
		Vec3d v1 = vec2.subtract(vec1);
		Vec3d v2 = vec3.subtract(vec1);
		return 0.5D * v1.crossProduct(v2).lengthVector();
	}

}
