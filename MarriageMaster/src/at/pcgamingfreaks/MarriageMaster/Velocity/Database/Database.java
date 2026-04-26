package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.MarriageMaster.API.Home;
import at.pcgamingfreaks.MarriageMaster.Database.BaseDatabase;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.UUID;

public class Database extends BaseDatabase<MarriageMaster, MarriagePlayerData, MarriageData, Home> {

    public Database(MarriageMaster plugin) {
        super(plugin, java.util.logging.Logger.getLogger("MarriageMaster"), new PlatformSpecific(plugin), plugin.getConfig(), "MarriageMaster", plugin.getDataFolder().toFile(), true, false);
        if (available()) {
            // unCacheStrategy = ... (Dummy or simple impl)
            plugin.getProxy().getEventManager().register(plugin, this);
            if (loadRunnable != null) loadRunnable.run();
        }
    }

    public void closePublic() {
        close();
    }

    @Override
    protected void close() {
        plugin.getProxy().getEventManager().unregisterListener(plugin, this);
        super.close();
    }

    @Override
    public MarriagePlayerData getPlayer(UUID uuid) {
        MarriagePlayerData player = cache.getPlayer(uuid);
        if (player == null) {
            java.util.Optional<Player> pPlayer = plugin.getProxy().getPlayer(uuid);
            player = new MarriagePlayerData(uuid, pPlayer.map(Player::getUsername).orElse("Unknown"));
            cache.cache(player);
        }
        load(player);
        return player;
    }

    @Subscribe
    public void onPlayerLogin(PostLoginEvent event) {
        getPlayer(event.getPlayer().getUniqueId());
    }

    @Override
    protected void loadOnlinePlayers() {
        for (Player player : plugin.getProxy().getAllPlayers()) {
            getPlayer(player.getUniqueId());
        }
    }
}
