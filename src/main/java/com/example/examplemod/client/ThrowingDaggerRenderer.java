package com.example.examplemod.client;

import com.example.examplemod.entity.ThrowingDaggerEntity;
import com.example.examplemod.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;

public class ThrowingDaggerRenderer extends EntityRenderer<ThrowingDaggerEntity> {
    private final ItemRenderer itemRenderer;
    private final ItemStack daggerStack; // 你的匕首物品

    public ThrowingDaggerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
        this.daggerStack = new ItemStack(ModItems.IRON_DAGGER.get()); // 替换成你的匕首物品
    }

    @Override
    public void render(ThrowingDaggerEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 调整旋转，使其飞行方向正确（类似箭的旋转）
        poseStack.mulPose(Axis.YP.rotationDegrees(
                Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())-90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(
                Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 50.0f));


        // 调整位置和大小
        poseStack.scale(1.2F, 1.2F, 1.2F); // 适当放大
        poseStack.translate(0.0F, -0.1F, 0.0F); // 微调位置

        // 渲染物品（类似原版箭的渲染方式）
        BakedModel model = itemRenderer.getModel(daggerStack, entity.level(), null, 0);
        itemRenderer.render(
                daggerStack,
                ItemDisplayContext.GROUND,
                false, // 是否使用左手模式
                poseStack,
                buffer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                model
        );

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowingDaggerEntity entity) {
        // 由于使用ItemRenderer，纹理由物品模型提供，这里返回一个空纹理
        return new ResourceLocation("minecraft", "textures/item/empty.png");
    }
}