package net.diamonddev.sharptone.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;

public class SonicBoomAttack {

    public static void create(Entity creator, ServerWorld serverWorld, int distance, float actionMultiplier) {

        Vec3d cameravec = creator.getCameraPosVec(0.0f);
        Vec3d rotationvec = creator.getRotationVec(0.0f); // Get Rotvec
        HitResult result = creator.raycast(distance, 0.0f, true); // Raycast

        boolean shouldCancel = false;

        for(int i = 1; i < MathHelper.floor(distance); ++i) {

            if (!shouldCancel) {
                Vec3d vector = cameravec.add(rotationvec.x * i, rotationvec.y * i, rotationvec.z * i);

                serverWorld.spawnParticles(
                        ParticleTypes.SONIC_BOOM,
                        vector.x,
                        vector.y,                  // Sonic Boom Particles
                        vector.z,
                        1,
                        0.0, 0.0, 0.0,
                        0.0);

                if (result.getType() == HitResult.Type.BLOCK) {
                    if (result.getPos() == vector) {
                        shouldCancel = true;
                    }
                }
            }
        }

        if (result.getType() != HitResult.Type.MISS) {

            if (creator instanceof LivingEntity) {
                ((LivingEntity) creator).takeKnockback(1.0, result.getPos().x - creator.getX(), result.getPos().z - creator.getZ());
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
