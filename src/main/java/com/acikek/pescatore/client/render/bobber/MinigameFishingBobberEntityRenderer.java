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
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

// TODO: Custom texture for each rod
// TODO: Line different color
@Environment(EnvType.CLIENT)
public class MinigameFishingBobberEntityRenderer extends EntityRenderer<MinigameFishingBobberEntity> {

    public MinigameFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void renderBobber(MinigameFishingBobberEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(dispatcher.getRotation());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normalMatrix = entry.getNormalMatrix();
        RenderLayer layer = RenderLayer.getEntityCutout(getTexture(entity));
        VertexConsumer buffer = vertexConsumers.getBuffer(layer);
        vertex(buffer, matrix, normalMatrix, light, 0.0f, 0, 0, 1);
        vertex(buffer, matrix, normalMatrix, light, 1.0f, 0, 1, 1);
        vertex(buffer, matrix, normalMatrix, light, 1.0f, 1, 1, 0);
        vertex(buffer, matrix, normalMatrix, light, 0.0f, 1, 0, 0);
        matrices.pop();
    }

    public static void renderFishingLineSegment(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd, float colorDelta) {
        float vertexX = x * segmentStart;
        float vertexY = y * (segmentStart * segmentStart + segmentStart) * 0.5f + 0.25f;
        float vertexZ = z * segmentStart;
        float normalX = x * segmentEnd - vertexX;
        float normalY = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5f + 0.25f - vertexY;
        float normalZ = z * segmentEnd - vertexZ;
        Vector3f normal = new Vector3f(normalX, normalY, normalZ).normalize();
        int color = colorDelta > 0.0f ? ColorHelper.Argb.lerp(colorDelta, 0x00FF0000, 0x0000FF00) : 0;
        buffer.vertex(matrices.getPositionMatrix(), vertexX, vertexY, vertexZ)
                .color(ColorHelper.Argb.getRed(color), ColorHelper.Argb.getGreen(color), ColorHelper.Argb.getBlue(color), 255)
                .normal(matrices.getNormalMatrix(), normal.x, normal.y, normal.z)
                .next();
    }

    public void renderFishingLine(MinigameFishingBobberEntity entity, PlayerEntity player, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        int xOffset = player.getMainArm() == Arm.RIGHT ? 1 : -1;
        var stackMatch = entity.getMatchingStack();
        if (stackMatch == null) {
            return;
        }
        if (stackMatch.getRight() == Hand.OFF_HAND) {
            xOffset = -xOffset;
        }
        ItemStack stack = stackMatch.getLeft();
        float swingY = MathHelper.sin(MathHelper.sqrt(player.getHandSwingProgress(tickDelta)) * MathHelper.PI);
        float tickYaw = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * MathHelper.RADIANS_PER_DEGREE;
        double yawY = MathHelper.sin(tickYaw);
        double yawX = MathHelper.cos(tickYaw);
        double playerX;
        double playerY;
        double playerZ;
        float eyeHeight;
        if (player == MinecraftClient.getInstance().player && (dispatcher.gameOptions == null || dispatcher.gameOptions.getPerspective().isFirstPerson())) {
            double fovMultiplier = 960.0 / dispatcher.gameOptions.getFov().getValue();
            Vec3d cameraPos = this.dispatcher.camera.getProjection().getPosition(xOffset * 0.525f, -0.1f);
            cameraPos = cameraPos.multiply(fovMultiplier);
            cameraPos = cameraPos.rotateY(swingY * 0.5F);
            cameraPos = cameraPos.rotateX(-swingY * 0.7F);
            playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX()) + cameraPos.x;
            playerY = MathHelper.lerp(tickDelta, player.prevY, player.getY()) + cameraPos.y;
            playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ()) + cameraPos.z;
            eyeHeight = player.getStandingEyeHeight();
        }
        else {
            double handOffset = xOffset * 0.35;
            playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX()) - yawX * handOffset - yawY * 0.8;
            playerY = player.prevY + player.getStandingEyeHeight() + (player.getY() - player.prevY) * tickDelta - 0.45;
            playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ()) - yawY * handOffset + yawX * 0.8;
            eyeHeight = player.isInSneakingPose() ? -0.1875F : 0.0F;
        }
        double entityX = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
        double entityY = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + 0.25;
        double entityZ = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
        float x = (float) (playerX - entityX);
        float y = (float) (playerY - entityY) + eyeHeight;
        float z = (float) (playerZ - entityZ);
        VertexConsumer segmentBuffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry segmentMatrices = matrices.peek();
        float colorDelta = -1.0f;
        if (entity.spawnedFish != null && stack.hasNbt() && stack.getNbt().contains("Reeling")) {
            colorDelta = (float) player.getItemUseTime() / entity.spawnedFish.type().getPerfectHoldTime() * 1.2f;
            if (colorDelta > 1.2f) {
                colorDelta = 2.4f - colorDelta;
            }
            colorDelta = Math.min(1.0f, colorDelta);
        }
        for (int i = 0; i <= 16; i++) {
            renderFishingLineSegment(x, y, z, segmentBuffer, segmentMatrices, i / 16.0f, (i + 1) / 16.0f, colorDelta);
        }
    }

    @Override
    public void render(MinigameFishingBobberEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        PlayerEntity player = entity.getPlayerOwner();
        if (player == null) {
            return;
        }
        matrices.push();
        renderBobber(entity, matrices, vertexConsumers, light);
        renderFishingLine(entity, player, tickDelta, matrices, vertexConsumers);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static void vertex(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {
        buffer.vertex(matrix, x - 0.5f, y - 0.5f, 0.0f)
                .color(255, 255, 255, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normalMatrix, 0.0f, 1.0f, 0.0f)
                .next();
    }

    @Override
    public Identifier getTexture(MinigameFishingBobberEntity entity) {
        return entity.getTier().bobberTexture;
    }

    public static void register() {
        EntityRendererRegistry.register(MinigameFishingBobberEntity.ENTITY_TYPE, MinigameFishingBobberEntityRenderer::new);
    }
}
