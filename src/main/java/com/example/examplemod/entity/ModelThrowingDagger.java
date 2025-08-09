package com.example.examplemod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// ModelThrowingDagger.java
public class ModelThrowingDagger extends EntityModel<ThrowingDaggerEntity> {
    private final ModelPart dagger;

    public ModelThrowingDagger(ModelPart root) { // 修改构造函数
        this.dagger = root.getChild("dagger");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();

        parts.addOrReplaceChild("dagger",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -8.0F, -0.5F, 1, 16, 1),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(ThrowingDaggerEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        dagger.zRot = (float)Math.toRadians(90);
        // 添加飞行旋转效果（绕Y轴）
        dagger.yRot = ageInTicks * 0.5f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        dagger.render(poseStack, buffer, packedLight, packedOverlay);
    }
}