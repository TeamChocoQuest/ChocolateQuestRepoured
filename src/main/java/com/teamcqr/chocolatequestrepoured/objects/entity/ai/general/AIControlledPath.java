package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIControlledBase;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.MathHelper;

public class AIControlledPath extends AIControlledBase {
      boolean pathDirection = false;
      int currentPathPoint = -1;

      public AIControlledPath(EntityCQRHumanBase owner) {
            super(owner);
      }

      public boolean shouldExecute() {
            return this.owner.getNavigator().getPath() != null;
      }

      public void updateTask() {
            if (this.owner.getNavigator().getPath() != null) {
                  if (this.currentPathPoint == -1) {
                        this.setNearestPathPoint();
                  } else if (this.currentPathPoint < this.owner.getNavigator().getPath().getCurrentPathLength()) {
                        int x = MathHelper.floor(this.owner.posX);
                        int y = MathHelper.floor(this.owner.posY);
                        int z = MathHelper.floor(this.owner.posZ);
                        x -= this.owner.getNavigator().getPath().getClosedSet()[this.currentPathPoint].x;
                        y -= this.owner.getNavigator().getPath().getClosedSet()[this.currentPathPoint].y;
                        z -= this.owner.getNavigator().getPath().getClosedSet()[this.currentPathPoint].z;
                        double dist = (double)(x * x + y * y + z * z);
                        if (this.owner.getRidingEntity() != null) {
                              dist -= (double)(this.owner.getRidingEntity().height * 2.0F + this.owner.getRidingEntity().width * 2.0F);
                        }

                        if (dist < 4.0D) {
                              this.nextPathPoint();
                        } else {
                              this.tryMoveToXYZ((double)this.owner.getNavigator().getPath().getClosedSet()[this.currentPathPoint].x, (double)this.owner.getNavigator().getPath().getClosedSet()[this.currentPathPoint].y, (double)this.owner.getNavigator().getPath().getClosedSet()[this.currentPathPoint].z, 0.7F);
                        }
                  } else {
                        this.setNearestPathPoint();
                  }
            }

            super.updateTask();
      }

      public PathNavigate getNavigator() {
            return this.owner.getRidingEntity() != null && this.owner.getRidingEntity() instanceof EntityLiving && ((EntityLiving)this.owner.getRidingEntity()).getNavigator() != null ? ((EntityLiving)this.owner.getRidingEntity()).getNavigator() : this.owner.getNavigator();
      }

      public void nextPathPoint() {
            if (this.currentPathPoint >= this.owner.getNavigator().getPath().getCurrentPathLength() - 1) {
                  this.pathDirection = false;
                  this.currentPathPoint = this.owner.getNavigator().getPath().getCurrentPathLength() - 1;
            }

            if (this.currentPathPoint == 0) {
                  this.pathDirection = true;
                  ++this.currentPathPoint;
            } else if (this.pathDirection) {
                  ++this.currentPathPoint;
            } else {
                  --this.currentPathPoint;
            }

      }

      public void setNearestPathPoint() {
            int closestPoint = -1;
            double minDistance = Double.MAX_VALUE;

            for(int i = 0; i < this.owner.getNavigator().getPath().getCurrentPathLength(); ++i) {
                  int x = MathHelper.floor(this.owner.posX);
                  int y = MathHelper.floor(this.owner.posY);
                  int z = MathHelper.floor(this.owner.posZ);
                  x -= this.owner.getNavigator().getPath().getClosedSet()[i].x;
                  y -= this.owner.getNavigator().getPath().getClosedSet()[i].y;
                  z -= this.owner.getNavigator().getPath().getClosedSet()[i].z;
                  double dist = (double)(x * x + y * y + z * z);
                  if (dist < minDistance) {
                        closestPoint = i;
                        minDistance = dist;
                  }
            }

            this.currentPathPoint = closestPoint;
      }
}
