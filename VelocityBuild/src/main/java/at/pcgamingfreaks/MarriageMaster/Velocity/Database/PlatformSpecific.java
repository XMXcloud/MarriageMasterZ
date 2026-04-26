package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.Database.ConnectionProvider.ConnectionProvider;
import at.pcgamingfreaks.MarriageMaster.API.Home;
import at.pcgamingfreaks.MarriageMaster.Database.IPlatformSpecific;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import at.pcgamingfreaks.MarriageMaster.Velocity.SpecialInfoWorker.DbErrorLoadingDataInfo;
import at.pcgamingfreaks.Message.MessageColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlatformSpecific implements IPlatformSpecific<MarriagePlayerData, MarriageData, Home> {
    private final MarriageMaster plugin;

    public PlatformSpecific(MarriageMaster plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull MarriagePlayerData producePlayer(@Nullable UUID uuid, @NotNull String name, boolean priest, boolean sharesBackpack, @Nullable Object databaseKey) {
        return new MarriagePlayerData(uuid, name, priest, sharesBackpack, databaseKey);
    }

    @Override
    public @NotNull MarriageData produceMarriage(@NotNull MarriagePlayerData player1, @NotNull MarriagePlayerData player2, @Nullable MarriagePlayerData priest, @NotNull Date weddingDate, @Nullable String surname, boolean pvpEnabled, @Nullable MessageColor color, @Nullable Home home, @Nullable Object databaseKey) {
        return new MarriageData(player1, player2, priest, weddingDate, surname, pvpEnabled, color, home, databaseKey);
    }

    @Override
    public @NotNull Home produceHome(@NotNull String world, @Nullable String server, double x, double y, double z, float yaw, float pitch) {
        return new Home(world, server, x, y, z, yaw, pitch);
    }

    @Override
    public void runAsync(@NotNull Runnable runnable, long delay) {
        if (delay > 0) {
            plugin.getProxy().getScheduler().buildTask(plugin, runnable)
                    .delay(delay, TimeUnit.MILLISECONDS)
                    .schedule();
        } else {
            plugin.getProxy().getScheduler().buildTask(plugin, runnable).schedule();
        }
    }

    @Override
    public @Nullable ConnectionProvider getExternalConnectionProvider(@NotNull String dbType, @NotNull Logger logger) throws SQLException {
        // Shared connection provider from PCGF_PluginLib is not available on Velocity yet
        return null;
    }

    @Override
    public @NotNull String getPluginVersion() {
        return plugin.getProxy().getPluginManager().getPlugin("marriagemaster").get().getDescription().getVersion().get();
    }

    @Override
    public void spawnDatabaseLoadingErrorMessage(String failedToLoad) {
        new DbErrorLoadingDataInfo(plugin, failedToLoad);
    }
}
