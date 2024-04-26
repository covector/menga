package dev.covector.menga.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.covector.menga.PPFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerRendererMixin(EntityRendererFactory.Context ctx, boolean slim) {
        super(ctx, new PlayerEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
    }

    
    // @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory;Z)V", at = @At("TAIL"))  // somehow this doesn't work
    @Inject(method = "<init>*", at = @At("TAIL"))
    public void constructorInject(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        // LoggerFactory.getLogger("test-mod").info("Intercepted constructor of renderer");
        this.addFeature(new PPFeatureRenderer(this));
    }

}
