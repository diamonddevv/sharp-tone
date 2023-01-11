package net.diamonddev.sharptone.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SonicBoomAttack {
    public static void create(Entity creator, ServerWorld serverWorld, int distance, float actionMultiplier) {

        distance = (int) (distance * (actionMultiplier / 2));

        Vec3d cameravec = creator.getCameraPosVec(1f);
        Vec3d rotationvec = creator.getRotationVec(1f); // Calculate Vectors
        Vec3d targetvec = cameravec.add(rotationvec.x * distance, rotationvec.y * distance, rotationvec.z * distance);


        Pair<HitResult, Double> raycastResultData = RaycastingUtil.performRaycastsInParallel(creator, cameravec, targetvec, true); // Raycasting

        HitResult result = raycastResultData.getLeft();
        long trueLength = Math.round(raycastResultData.getRight());

        int i = 0;
        boolean shouldCancel = false;

        while (!shouldCancel) {
            i++;

            if (trueLength == 0) {
                shouldCancel = true;
            }

            Vec3d vector = cameravec.add(rotationvec.x * i, rotationvec.y * i, rotationvec.z * i);

            serverWorld.spawnParticles(
                    ParticleTypes.SONIC_BOOM,
                    vector.x,
                    vector.y,
                    vector.z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0);


            if (i >= trueLength) {
                shouldCancel = true;
            }
        }

        if (result instanceof EntityHitResult || result instanceof BlockHitResult) {

            if (creator instanceof LivingEntity) {
                ((LivingEntity) creator).takeKnockback(3.0f * actionMultiplier, result.getPos().x, result.getPos().z);
            }

            serverWorld.createExplosion(
                    null,
                    SharpToneDamageSource.sonicBoomAtk(creator),
                    null,
                    result.getPos().x, result.getPos().y, result.getPos().z,
                    1.8f * actionMultiplier,
                    false, World.ExplosionSourceType.NONE
            );

        }
    }
}
