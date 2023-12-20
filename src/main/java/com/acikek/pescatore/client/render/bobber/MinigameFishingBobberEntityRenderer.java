package com.acikek.pescatore.client.render.bobber;

import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

// TODO: This is probably just all placeholder anyway if we decide to do 3D model -- if not, clean this up
// TODO: Custom texture for each rod
// TODO: Line different color
@Environment(EnvType.CLIENT)
public class MinigameFishingBobberEntityRenderer extends EntityRenderer<MinigameFishingBobberEntity> {

    public static final Identifier TEXTURE = new Identifier("textures/entity/fishing_hook.png");
    public static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);

    public MinigameFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(MinigameFishingBobberEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        PlayerEntity player = entity.getPlayerOwner();
        if (player == null) {
            return;
        }
        matrices.push();
        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(dispatcher.getRotation());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 0.0f, 0, 0, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 1.0f, 0, 1, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 1.0f, 1, 1, 0);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 0.0f, 1, 0, 0);
        matrices.pop();
        int xOffset = player.getMainArm() == Arm.RIGHT ? 1 : -1;
        if (!entity.tier.matchesStack(player.getMainHandStack())) {
            xOffset = -xOffset;
        }
        // TODO: I don't need to be cleaning this up rn
        float swingY = MathHelper.sin(MathHelper.sqrt(player.getHandSwingProgress(tickDelta)) * MathHelper.PI);
        float tickYaw = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * MathHelper.RADIANS_PER_DEGREE;
        double yawY = MathHelper.sin(tickYaw);
        double yawX = MathHelper.cos(tickYaw);
        double m = (double)xOffset * 0.35;
        double n = 0.8;
        double o;
        double p;
        double q;
        float r;
        double s;
        if ((this.dispatcher.gameOptions == null || this.dispatcher.gameOptions.getPerspective().isFirstPerson()) && player == MinecraftClient.getInstance().player) {
            s = 960.0 / (double)(Integer)this.dispatcher.gameOptions.getFov().getValue();
            Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)xOffset * 0.525F, -0.1F);
            vec3d = vec3d.multiply(s);
            vec3d = vec3d.rotateY(swingY * 0.5F);
            vec3d = vec3d.rotateX(-swingY * 0.7F);
            o = MathHelper.lerp((double)tickDelta, player.prevX, player.getX()) + vec3d.x;
            p = MathHelper.lerp((double)tickDelta, player.prevY, player.getY()) + vec3d.y;
            q = MathHelper.lerp((double)tickDelta, player.prevZ, player.getZ()) + vec3d.z;
            r = player.getStandingEyeHeight();
        } else {
            o = MathHelper.lerp((double)tickDelta, player.prevX, player.getX()) - yawX * m - yawY * 0.8;
            p = player.prevY + (double)player.getStandingEyeHeight() + (player.getY() - player.prevY) * (double)tickDelta - 0.45;
            q = MathHelper.lerp((double)tickDelta, player.prevZ, player.getZ()) - yawY * m + yawX * 0.8;
            r = player.isInSneakingPose() ? -0.1875F : 0.0F;
        }

        s = MathHelper.lerp((double)tickDelta, entity.prevX, entity.getX());
        double t = MathHelper.lerp((double)tickDelta, entity.prevY, entity.getY()) + 0.25;
        double u = MathHelper.lerp((double)tickDelta, entity.prevZ, entity.getZ());
        float v = (float)(o - s);
        float w = (float)(p - t) + r;
        float x = (float)(q - u);
        VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry entry2 = matrices.peek();

        for(int z = 0; z <= 16; ++z) {
            renderFishingLine(v, w, x, vertexConsumer2, entry2, percentage(z, 16), percentage(z + 1, 16));
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static float percentage(int value, int max) {
        return (float)value / (float)max;
    }

    private static void vertex(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {
        buffer.vertex(matrix, x - 0.5F, (float)y - 0.5F, 0.0F).color(255, 255, 255, 255).texture((float)u, (float)v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    private static void renderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd) {
        float f = x * segmentStart;
        float g = y * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
        float h = z * segmentStart;
        float i = x * segmentEnd - f;
        float j = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - g;
        float k = z * segmentEnd - h;
        float l = MathHelper.sqrt(i * i + j * j + k * k);
        i /= l;
        j /= l;
        k /= l;
        buffer.vertex(matrices.getPositionMatrix(), f, g, h).color(0, 0, 0, 255).normal(matrices.getNormalMatrix(), i, j, k).next();
    }

    @Override
    public Identifier getTexture(MinigameFishingBobberEntity fishingBobberEntity) {
        return TEXTURE;
    }

    public static void register() {
        EntityRendererRegistry.register(MinigameFishingBobberEntity.ENTITY_TYPE, MinigameFishingBobberEntityRenderer::new);
    }
}
