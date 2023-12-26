// Made with Blockbench 4.9.2
// Exported for Minecraft version 1.17 or later

package com.acikek.pescatore.client.render.fish;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.entity.MinigameFishEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class MinigameFishModel extends SinglePartEntityModel<MinigameFishEntity> {

    public static final EntityModelLayer LAYER = new EntityModelLayer(Pescatore.id("minigame_fish"), "main");

    private final ModelPart root;

    public MinigameFishModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        ModelPartData base = root.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData mid = base.addChild("mid", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        mid.addChild("fish_still", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -8.0F, 4.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData fishMoving = mid.addChild("fish_moving", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        fishMoving.addChild("bone", ModelPartBuilder.create().uv(0, 10).cuboid(-2.0F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, -2.0F, 0.0F));
        fishMoving.addChild("bone2", ModelPartBuilder.create().uv(14, 0).cuboid(-1.5F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, -2.0F, 3.0F));
        fishMoving.addChild("bone3", ModelPartBuilder.create().uv(10, 13).cuboid(-1.0F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.pivot(0.5F, -2.0F, 6.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(MinigameFishEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        root.traverse().forEach(ModelPart::resetTransform);
        // TODO: Speed multiplier based on fleeing state
        float speed = entity.isBiting() /*|| isFleeing */ ? 4.0f : 2.0f;
        updateAnimation(entity.animation, MinigameFishAnimation.SWIMMIE, animationProgress, speed);
    }

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(LAYER, MinigameFishModel::getTexturedModelData);
    }
}