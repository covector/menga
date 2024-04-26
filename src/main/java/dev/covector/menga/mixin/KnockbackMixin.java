package dev.covector.menga.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.covector.menga.Menga;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;

@Mixin(EnchantmentHelper.class)
public abstract class KnockbackMixin {
    @Inject(method = "getKnockback(Lnet/minecraft/entity/LivingEntity;)I", at = @At("HEAD"), cancellable = true)
    private static void getModifiedKnockback(LivingEntity entity, CallbackInfoReturnable cir) {
        int add = 0;
        if (entity.hasStatusEffect(Menga.BONER)) {
            add = entity.getStatusEffect(Menga.BONER).getAmplifier() + 1;
        }
        cir.setReturnValue(EnchantmentHelper.getEquipmentLevel(Enchantments.KNOCKBACK, entity) + add);
     }
}
