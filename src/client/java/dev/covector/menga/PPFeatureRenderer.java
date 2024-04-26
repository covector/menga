package dev.covector.menga;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class PPFeatureRenderer
extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PPFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    // analysis result on CapeFeatureRenderer.render():
    // h: partial ticks for interpolation
    // d: capeX (smoothed, 0 when not moving, go up to a terminal value when moving)
    // e: capeY (smoothed, 0 when not moving, go up to a terminal value when moving)
    // m: capeZ (smoothed, 0 when not moving, go up to a terminal value when moving)
    // n: bodyYaw
    // o: sin(n)
    // p: -cos(n)
    // s: influence of capeX and capeZ on the cape based on dot product with the bodyYaw
    // t: 1.0 when full speed, 0.0 when not moving (like d but in all directions)
    // q: influence of capeY on the cape (clamped) + up down movement from movement speed + higher if sneaking
    // r: like t

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l) {
        if (!abstractClientPlayerEntity.hasStatusEffect(Menga.BONER)) {
            return;
        }

        matrixStack.push();
        matrixStack.translate(-0.025f, 0.7f, abstractClientPlayerEntity.isInSneakingPose() ? 0.1f : -0.15f);

        int remaining = abstractClientPlayerEntity.getStatusEffect(Menga.BONER).getDuration();
        if (!abstractClientPlayerEntity.getStatusEffect(Menga.BONER).isInfinite() && remaining < 600) {
            float scale = MathHelper.lerp(remaining / 600F, 0F, 1F);
            matrixStack.scale(scale, scale, scale);
        }
        
        double d = MathHelper.lerp((double)h, (double)abstractClientPlayerEntity.prevCapeX, (double)abstractClientPlayerEntity.capeX) - MathHelper.lerp((double)h, (double)abstractClientPlayerEntity.prevX, (double)abstractClientPlayerEntity.getX());
        double e = MathHelper.lerp((double)h, (double)abstractClientPlayerEntity.prevCapeY, (double)abstractClientPlayerEntity.capeY) - MathHelper.lerp((double)h, (double)abstractClientPlayerEntity.prevY, (double)abstractClientPlayerEntity.getY());
        double m = MathHelper.lerp((double)h, (double)abstractClientPlayerEntity.prevCapeZ, (double)abstractClientPlayerEntity.capeZ) - MathHelper.lerp((double)h, (double)abstractClientPlayerEntity.prevZ, (double)abstractClientPlayerEntity.getZ());
        float n = MathHelper.lerpAngleDegrees((float)h, (float)abstractClientPlayerEntity.prevBodyYaw, (float)abstractClientPlayerEntity.bodyYaw);
        double o = MathHelper.sin((float)(n * ((float)Math.PI / 180)));
        double p = -MathHelper.cos((float)(n * ((float)Math.PI / 180)));
        float t = MathHelper.lerp((float)h, (float)abstractClientPlayerEntity.prevStrideDistance, (float)abstractClientPlayerEntity.strideDistance);

        float u = MathHelper.sin((float)(MathHelper.lerp((float)h, (float)abstractClientPlayerEntity.prevHorizontalSpeed, (float)abstractClientPlayerEntity.horizontalSpeed) * 7.0f)) * 31.0f * t;
        float v = MathHelper.sin((float)(MathHelper.lerp((float)h, (float)abstractClientPlayerEntity.prevHorizontalSpeed, (float)abstractClientPlayerEntity.horizontalSpeed) * 5.0f)) * 113.0f * t;

        u += MathHelper.clamp((float) (d * p - m * o) * 100.0f, -30.0f, 30.0f);
        v -= MathHelper.clamp((float) (e) * 30.0f, -30.0f, 30.0f);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(abstractClientPlayerEntity.getSkinTexture()));

        if (this.getContextModel() instanceof PPRendererAccessor) {
            ((PPRendererAccessor)this.getContextModel()).renderBalls(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(v));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(u));
        
        if (this.getContextModel() instanceof PPRendererAccessor) {
            ((PPRendererAccessor)this.getContextModel()).renderPP(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        }
        matrixStack.pop();
    }
}