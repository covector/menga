package dev.covector.menga;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MengaItem extends Item {
    public MengaItem() {
        super(new Item.Settings()
            .maxCount(1)
            .food(new FoodComponent.Builder().alwaysEdible().build())
        );
    }

    public int getMaxUseTime(ItemStack stack) {
        return 100;
     }

     @Override
     public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
         if (user instanceof PlayerEntity && !world.isClient) {
            PlayerEntity player = (PlayerEntity) ((Object) user);
            PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
            player.addStatusEffect(new StatusEffectInstance(Menga.BONER, 600 + 200 * playerState.totalEdged , playerState.totalEdged));
            playerState.totalEdged = 0;
         }
         return super.finishUsing(stack, world, user);
     }
}

