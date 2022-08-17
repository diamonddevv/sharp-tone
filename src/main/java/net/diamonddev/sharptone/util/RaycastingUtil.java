package net.diamonddev.sharptone.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class RaycastingUtil {

    public static EntityHitResult performEntityRaycast(Entity source, Vec3d origin, Vec3d target) { // thank you once again apoli 👍 very cool (https://github.com/apace100/apoli/blob/master/src/main/java/io/github/apace100/apoli/power/factory/action/entity/RaycastAction.java)
        Vec3d ray = target.subtract(origin);
        Box box = source.getBoundingBox().stretch(ray).expand(1.0D, 1.0D, 1.0D);
        return ProjectileUtil.raycast(source, origin, target, box, (entityx) -> !entityx.isSpectator(), ray.lengthSquared());
    }

    public static BlockHitResult performBlockRaycast(Entity source, Vec3d origin, Vec3d target, boolean handleFluids) {
        return source.world.raycast(new RaycastContext(origin, target, RaycastContext.ShapeType.OUTLINE, handleFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, source));
    }

    public static Pair<HitResult, Double> performRaycastsInParallel(Entity source, Vec3d origin, Vec3d target, boolean handleFluids) { // thank you once again apoli 👍
        HitResult result;

        // Entity
        result = performEntityRaycast(source, origin, target);

        // Block
        BlockHitResult blockHitResult = performBlockRaycast(source, origin, target, handleFluids);

        // Work out which to use
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            if (result == null || result.getType() == HitResult.Type.MISS) {
                result = blockHitResult;
            } else {
                if (result.getPos().squaredDistanceTo(target) > blockHitResult.getPos().squaredDistanceTo(target)) {
                    result = blockHitResult;
                }
            }
        }

        // create miss catch
        if (result == null) {
            result = new HitResult(target) {
                @Override
                public Type getType() {
                    return Type.MISS;
                }
            };
        }

        // Get raycast length
        Vec3d finishpos = result.getPos();
        double actualRayLength = origin.squaredDistanceTo(finishpos);


        // return
        return new Pair<>(result, actualRayLength);

    }
}
