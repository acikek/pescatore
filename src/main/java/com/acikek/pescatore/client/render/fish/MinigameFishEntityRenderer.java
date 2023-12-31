package com.acikek.pescatore.client.render.fish;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.entity.MinigameFishEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MinigameFishEntityRenderer extends MobEntityRenderer<MinigameFishEntity, MinigameFishModel> {

    public static final Identifier TEXTURE = Pescatore.id("textures/entity/minigame_fish/fish_shadow.png");

    public MinigameFishEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MinigameFishModel(context.getPart(MinigameFishModel.LAYER)), 0.0f);
    }

    @Override
    public Identifier getTexture(MinigameFishEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(MinigameFishEntity entity, MatrixStack matrices, float amount) {
        float scale = entity.type().size().scale();
        matrices.scale(scale, scale, scale);
    }

    public static void register() {
        EntityRendererRegistry.register(MinigameFishEntity.ENTITY_TYPE, MinigameFishEntityRenderer::new);
    }
}
