package dev.covector.menga;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface EdgeCallback {
    Event<EdgeCallback> EVENT = EventFactory.createArrayBacked(EdgeCallback.class,
        (listeners) -> (player, edgedCount) -> {
            for (EdgeCallback listener : listeners) {
                ActionResult result = listener.interact(player, edgedCount);
     
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
     
        return ActionResult.PASS;
    });
     
    ActionResult interact(PlayerEntity player, int edgedCount);
    
}
