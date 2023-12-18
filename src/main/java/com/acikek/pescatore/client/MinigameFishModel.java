package com.acikek.pescatore.client;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.entity.MinigameFishEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

public class MinigameFishModel extends EntityModel<MinigameFishEntity> {

    public static final EntityModelLayer LAYER = new EntityModelLayer(Pescatore.id("cube"), "main");

    private final ModelPart base;

    public MinigameFishModel(ModelPart modelPart) {
        base = modelPart.getChild(EntityModelPartNames.CUBE);
    }

    public static TexturedModelData getTexturedModelData() {
        var modelData = new ModelData();
        var modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(MinigameFishEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        base.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(LAYER, MinigameFishModel::getTexturedModelData);
    }
}
