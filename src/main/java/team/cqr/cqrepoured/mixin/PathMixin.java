package team.cqr.cqrepoured.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author ZZZank
 */
@Mixin(Path.class)
public abstract class PathMixin {

    @Shadow
    @Final
    private PathPoint[] points;

    /**
     * @author ZZZank
     * @reason Migrated from ASM, see <a href="https://github.com/TeamChocoQuest/ChocolateQuestRepoured/commit/f8f20c05cca006a6e503cb3d4d071aa6b21d52e0">this GitHub commit</a> (Add class transformer to fix pathing of big mobs)
     */
    @Overwrite
    public Vec3d getVectorFromIndex(Entity entity, int index) {
        PathPoint point = this.points[index];
        return new Vec3d(point.x + 0.5, point.y, point.z + 0.5);
    }
}
