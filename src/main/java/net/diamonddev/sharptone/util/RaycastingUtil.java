package net.diamonddev.sharptone.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RaycastingUtil {

    public static HitResult performEntityRaycast(Entity source, Vec3d origin, Vec3d target) { // thank you once again origins 👍
        Vec3d ray = target.subtract(origin);
        Box box = source.getBoundingBox().stretch(ray).expand(1.0D, 1.0D, 1.0D);
        return ProjectileUtil.raycast(source, origin, target, box, (entityx) -> !entityx.isSpectator(), ray.lengthSquared());
    }

    public static HitResult performBlockRaycast(Entity source, Vec3d origin, Vec3d target, boolean handleFluids) {
        return source.raycast(origin.distanceTo(target), 1.0f, handleFluids);
    }

    public static HitResult performRaycastsInParallel(Entity source, Vec3d origin, Vec3d target, boolean handleFluids) {
        HitResult result = performEntityRaycast(source, origin, target);
        if (result != null) {
            result = performBlockRaycast(source, origin, target, handleFluids);
        }
        return result;
    }
}
