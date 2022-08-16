package net.diamonddev.sharptone.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SonicBoomAttack {

    private static final Logger log = LogManager.getLogger("Sharp Tone: Raycasting");

    public static void create(Entity creator, ServerWorld serverWorld, int distance, float actionMultiplier) { // todo: WHY IS THIS NOT WORKING WAAHH

        Vec3d cameravec = creator.getEyePos();
        Vec3d rotationvec = creator.getRotationVec(1.0f); // Get Rotvec
        Vec3d targetvec = cameravec.add(rotationvec.x * distance, rotationvec.y * distance, rotationvec.z * distance);

        HitResult result = RaycastingUtil.performRaycastsInParallel(
                creator, cameravec, targetvec, true
        );


        boolean shouldCancel = false;
        int i = 0;

        while (!shouldCancel) {
            i++;
            Vec3d vector = cameravec.add(rotationvec.x * i, rotationvec.y * i, rotationvec.z * i);

            serverWorld.spawnParticles(
                    ParticleTypes.SONIC_BOOM,
                    vector.x,
                    vector.y,
                    vector.z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0);


            if (vector == result.getPos()) {
                shouldCancel = true;
            }
        }


        if (result instanceof EntityHitResult || result instanceof BlockHitResult) {

            if (creator instanceof LivingEntity) {
                ((LivingEntity) creator).takeKnockback(1.2 * actionMultiplier, result.getPos().x - creator.getX(), result.getPos().z - creator.getZ());
            }

            log.info("Raycast Success");

            serverWorld.createExplosion(
                    null,
                    SharpToneDamageSource.sonicBoomAtk(creator),
                    null,
                    result.getPos().x, result.getPos().y, result.getPos().z,
                    2.0f * actionMultiplier, false, Explosion.DestructionType.NONE);

        }
    }
}
