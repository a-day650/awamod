package com.example.examplemod.client;

import com.example.examplemod.entity.ModEntities;
import com.example.examplemod.entity.ModelThrowingDagger;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    public static final ModelLayerLocation THROWING_DAGGER_LAYER =
            new ModelLayerLocation(new ResourceLocation("awamod", "iron_dagger"), "main");

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(THROWING_DAGGER_LAYER, ModelThrowingDagger::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.THROWING_DAGGER.get(),
                ThrowingDaggerRenderer::new // 现在只需要Context参数
        );
    }
}