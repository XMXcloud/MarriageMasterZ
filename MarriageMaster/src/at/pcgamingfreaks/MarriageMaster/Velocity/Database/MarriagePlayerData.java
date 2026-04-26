package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.MarriageMaster.API.Home;
import at.pcgamingfreaks.MarriageMaster.Database.MarriagePlayerDataBase;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.Marriage;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriagePlayer;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MarriagePlayerData extends MarriagePlayerDataBase<MarriagePlayer, CommandSource, Home, Marriage, Player, Player, VelocityMessage> implements MarriagePlayer {
    @Getter private long lastPlayed = 0;

    public MarriagePlayerData(@Nullable UUID uuid, @NotNull String name) {
        super(uuid, name);
    }

    public MarriagePlayerData(@Nullable UUID uuid, @NotNull String name, boolean priest) {
        super(uuid, name, priest);
    }

    public MarriagePlayerData(@Nullable UUID uuid, @NotNull String name, boolean priest, boolean sharesBackpack, @Nullable Object databaseKey) {
        super(uuid, name, priest, sharesBackpack, databaseKey);
    }

    @Override
    public @Nullable String getOnlineName() {
        Player player = getPlayer();
        return player != null ? player.getUsername() : null;
    }

    @Override
    public @Nullable Player getPlayer() {
        return MarriageMaster.getInstance().getProxy().getPlayer(getUUID()).orElse(null);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        Player player = getPlayer();
        return player != null && player.hasPermission(permission);
    }

    @Override
    public boolean isOnline() {
        Player player = getPlayer();
        boolean online = player != null;
        if (online) lastPlayed = System.currentTimeMillis();
        return online;
    }

    @Override
    public boolean canSee(Player player) {
        return true;
    }

    @Override
    public boolean canSee(@NotNull MarriagePlayer player) {
        return true;
    }

    @Override
    public boolean isPartner(@NotNull Player player) {
        return isPartner(MarriageMaster.getInstance().getPlayerData(player));
    }

    @Override
    public void send(@NotNull Object message, @Nullable Object... args) {
        sendMessage(message, args);
    }

    @Override
    public void sendMessage(@NotNull Object message, @Nullable Object... args) {
        Player player = getPlayer();
        if (player == null) return;
        if (message instanceof VelocityMessage) {
            ((VelocityMessage) message).send(player, args);
        } else if (message instanceof String) {
            player.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(((String) message).replace('&', '§')));
        }
    }
}
