package dev.covector.menga.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import dev.covector.menga.PPRendererAccessor;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(PlayerEntityModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends BipedEntityModel<T> implements PPRendererAccessor {
    public ModelPart pp;
    public ModelPart ball1;
    public ModelPart ball2;

    public PlayerModelMixin(ModelPart root, boolean thinArms) {
        super(root, RenderLayer::getEntityTranslucent);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/model/ModelPart;Z)V", at = @At("TAIL"))
    public void constructorInject(ModelPart root, boolean thinArms, CallbackInfo ci) {
        pp = root.getChild("pp");
        ball1 = root.getChild("ball1");
        ball2 = root.getChild("ball2");
    }

    @Inject(method = "getTexturedModelData(Lnet/minecraft/client/model/Dilation;Z)Lnet/minecraft/client/model/ModelData;", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void getPPModelData(Dilation dilation, boolean slim, CallbackInfoReturnable cir, ModelData modelData) {
        modelData.getRoot().addChild("pp", ModelPartBuilder.create().uv(32, 59).cuboid(0.0F, 0.0F, -4.0F, 1.0F, 1.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelData.getRoot().addChild("ball1", ModelPartBuilder.create().uv(32, 62).cuboid(-1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelData.getRoot().addChild("ball2", ModelPartBuilder.create().uv(36, 62).cuboid(1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
    }

    @Override
    public void renderPP(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        this.pp.render(matrices, vertices, light, overlay);
     }

    @Override
    public void renderBalls(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        this.ball1.render(matrices, vertices, light, overlay);
        this.ball2.render(matrices, vertices, light, overlay);
     }
}