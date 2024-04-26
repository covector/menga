package dev.covector.menga.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.covector.menga.EdgeCallback;
import dev.covector.menga.Menga;
import dev.covector.menga.MengaItem;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import dev.covector.menga.PlayerData;
import dev.covector.menga.StateSaverAndLoader;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin() {
        super(null, null);
    }

    @Shadow
    public abstract int getItemUseTimeLeft();

    @Shadow
    protected ItemStack activeItemStack;

	@Inject(method = "spawnConsumptionEffects(Lnet/minecraft/item/ItemStack;I)V", at = @At("HEAD"), cancellable = true)
	private void mengaParticle(ItemStack stack, int particleCount, CallbackInfo cbi) {

		if (stack.getItem() instanceof MengaItem) {
            this.spawnMangeParticles(stack, particleCount);
            this.playSound(SoundEvents.ENTITY_GUARDIAN_HURT_LAND, 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            cbi.cancel();
        }
	}

    private void spawnMangeParticles(ItemStack stack, int count) {
        var livingEntity = (LivingEntity) (Object) this;
        for(int i = 0; i < count; ++i) {
           Vec3d vec3d = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
           vec3d = vec3d.rotateX(-livingEntity.getPitch() * 0.017453292F);
           vec3d = vec3d.rotateY(-livingEntity.getYaw() * 0.017453292F);
           double d = (double)(-this.random.nextFloat()) * 0.6 - 0.3;
           Vec3d vec3d2 = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
           vec3d2 = vec3d2.rotateX(-livingEntity.getPitch() * 0.017453292F);
           vec3d2 = vec3d2.rotateY(-livingEntity.getYaw() * 0.017453292F);
           vec3d2 = vec3d2.add(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
           livingEntity.getWorld().addParticle(ParticleTypes.ITEM_SNOWBALL, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
        }
  
    }

    @Inject(method = "stopUsingItem()V", at = @At("HEAD"))
    public void stoppedMidway(CallbackInfo cbi) {
        if (this.getWorld().isClient) return;
        if (!(this.activeItemStack.getItem() instanceof MengaItem)) return;
        MengaItem mengaItem = (MengaItem) this.activeItemStack.getItem();
        Menga.LOGGER.info("Remaining Use Time" + String.valueOf(this.getItemUseTimeLeft()));
        if (this.getItemUseTimeLeft() < mengaItem.getMaxUseTime(activeItemStack) * 0.2f) {
            Menga.LOGGER.info("EDGED!");
            if ((Object)this instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) ((Object) this);
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                playerState.totalEdged += 1;
                Menga.LOGGER.info("You edged " + String.valueOf(playerState.totalEdged) + " times! congrats");
                EdgeCallback.EVENT.invoker().interact(player, playerState.totalEdged);
            }
        }
    }

    // @Inject(method = "consumeItem()V", at = @At("HEAD"))
    // public void wentAllTheWay(CallbackInfo cbi) {
    //     if (this.getWorld().isClient) return;
    //     if (!(this.activeItemStack.getItem() instanceof MengaItem)) return;
    //     TestMod.LOGGER.info("Lost the edge!");
    //     if ((Object)this instanceof PlayerEntity) {
    //         PlayerEntity player = (PlayerEntity) ((Object) this);
    //         PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
    //         playerState.totalEdged = 0;
    //     }
    // }
}