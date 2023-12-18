package com.acikek.pescatore.client;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.entity.MinigameFishEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class MinigameFishEntityRenderer extends MobEntityRenderer<MinigameFishEntity, MinigameFishModel> {

    public MinigameFishEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MinigameFishModel(context.getPart(MinigameFishModel.LAYER)), 0.0f);
    }

    @Override
    public Identifier getTexture(MinigameFishEntity entity) {
        return Pescatore.id("textures/entity/minigame_fish/cube.png");
    }

    public static void register() {
        EntityRendererRegistry.register(MinigameFishEntity.ENTITY_TYPE, MinigameFishEntityRenderer::new);
    }
}
