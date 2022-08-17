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
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SonicBoomAttack {

    private static final Logger log = LogManager.getLogger("Sharp Tone: Raycasting");

    public static void create(Entity creator, ServerWorld serverWorld, int distance, float actionMultiplier) { // todo: fix particles, balancing

        Vec3d cameravec = creator.getCameraPosVec(0.0f);
        Vec3d rotationvec = creator.getRotationVec(0.0f); // Get Rotvec
        Vec3d targetvec = cameravec.add(rotationvec.x * distance, rotationvec.y * distance, rotationvec.z * distance);


        Pair<HitResult, Double> raycastResultData = RaycastingUtil.performRaycastsInParallel(creator, cameravec, targetvec, true);
        HitResult result = raycastResultData.getLeft();
        int i = 0;
        long trueLength = Math.round(raycastResultData.getRight());

        boolean shouldCancel = false;

        while (!shouldCancel) {
            i++;

            if (trueLength == 0) {
                shouldCancel = true;
            }

            Vec3d vector = cameravec.add(Math.round(rotationvec.x * i), Math.round(rotationvec.x * i), Math.round(rotationvec.x * i));

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
                ((LivingEntity) creator).takeKnockback(1.8 * actionMultiplier, result.getPos().x - creator.getX(), result.getPos().z - creator.getZ());
            }

            serverWorld.createExplosion(
                    null,
                    SharpToneDamageSource.sonicBoomAtk(creator),
                    null,
                    result.getPos().x, result.getPos().y, result.getPos().z,
                    2.0f * actionMultiplier, false, Explosion.DestructionType.NONE);

        }
    }
}
