package dev.covector.menga;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
 
public class StateSaverAndLoader extends PersistentState {
 
    public HashMap<UUID, PlayerData> players = new HashMap<>();
 
    public static PlayerData getPlayerState(LivingEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());
        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
        return playerState;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
 
            playerNbt.putInt("totalEdged", playerData.totalEdged);
 
            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);
 
        return nbt;
    }
 
    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        NbtCompound playersNbt = tag.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
 
            playerData.totalEdged = playersNbt.getCompound(key).getInt("totalEdged");
 
            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });
 
        return state;
    }

 
    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateSaverAndLoader state = persistentStateManager.getOrCreate(StateSaverAndLoader::createFromNbt, StateSaverAndLoader::new, Menga.MODID);
        state.markDirty();
        return state;
    }
}
