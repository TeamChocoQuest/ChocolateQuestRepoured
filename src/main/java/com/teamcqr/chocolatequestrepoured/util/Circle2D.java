package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.util.math.BlockPos;

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

public class Circle2D
{
    public enum CircleRegion
    {
        OUTSIDE,
        EDGE,
        INSIDE
    }

    public class Coord
    {
        public int x;
        public int z;

        private Coord (int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == this) return true;
            if (!(o instanceof Coord)) {
                return false;
            }
            Coord c = (Coord) o;
            return this.x == c.x && this.z == c.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }
    }
    private int radius;
    private HashSet<Coord> perimeter;
    private HashSet<Coord> filled;

    public Circle2D(int centerX, int centerZ, int radius)
    {
        this.radius = radius;
        this.perimeter = new HashSet<>();
        this.filled = new HashSet<>();
        int x = 0;
        int z = radius;

        int P = (5 - radius * 4) / 4;

        do
        {
            this.perimeter.add(new Coord(centerX + x, centerZ + z));
            this.perimeter.add(new Coord(centerX - x, centerZ + z));
            this.perimeter.add(new Coord(centerX + x, centerZ - z));
            this.perimeter.add(new Coord(centerX - x, centerZ - z));
            this.perimeter.add(new Coord(centerX + z, centerZ + x));
            this.perimeter.add(new Coord(centerX - z, centerZ + x));
            this.perimeter.add(new Coord(centerX + z, centerZ - x));
            this.perimeter.add(new Coord(centerX - z, centerZ - x));

            // fill all points in between the left and right edges
            for (int fillX = centerX - x; fillX <= centerX + x; fillX ++)
            {
                this.filled.add(new Coord(fillX, centerZ + z));
                this.filled.add(new Coord(fillX, centerZ - z));
            }

            if (P < 0)
            {
                P += 2 * x + 1;
            }
            else
            {
                P += 2 * (x - z) + 1;
                z--;
            }
            x++;

        } while (x <= z);
    }

    public CircleRegion[][] toArray(int startX, int startZ, int lenX, int lenZ)
    {
        Coord c = new Coord(startX, startZ);
        CircleRegion[][] result = new CircleRegion[lenX][lenZ];
        for (int i = 0; i < lenX; i++)
        {
            int edgesSeen = 0; //num edge blocks we have iterated past so far
            for (int j = 0; j < lenZ; j++)
            {
                c.x = startX + i;
                c.z = startZ + j;
                if (perimeter.contains(c))
                {
                    result[i][j] = CircleRegion.EDGE;
                    edgesSeen++;
                }
                else if (edgesSeen == 1)
                {
                    // if only one edge has been seen we must be within the circle
                    result[i][j] = CircleRegion.INSIDE;
                }
                else
                {
                    // if 0 or 2+ edges have been seen then we are before or after the circle
                    result[i][j] = CircleRegion.OUTSIDE;
                }
            }
        }

        return result;
    }

    // Get an array of the (x,z) coordinates that make up the perimeter of this circle
    public ArrayList<Coord> getEdgeCoords()
    {
        return new ArrayList<>(this.perimeter);
    }

    public ArrayList<Coord> getFillCoords()
    {
        return new ArrayList<>(this.filled);
    }
}
