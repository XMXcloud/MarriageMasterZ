package at.pcgamingfreaks.MarriageMaster.Velocity.Listener;

import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriagePlayer;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import at.pcgamingfreaks.MarriageMaster.Listener.JoinLeaveInfoBase;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class JoinLeaveInfo extends JoinLeaveInfoBase {
    private final MarriageMaster plugin;
    private final long delay;

    public JoinLeaveInfo(MarriageMaster plugin) {
        super(plugin.getLanguage());
        this.plugin = plugin;
        this.delay = plugin.getConfig().getJoinInfoDelay() + 1;
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        MarriagePlayer player = plugin.getPlayerData(event.getPlayer());
        if (player.isMarried()) {
            onJoin(player);
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        MarriagePlayer player = plugin.getPlayerData(event.getPlayer());
        if (player.isMarried()) {
            onLeave(player);
        }
    }

    @Override
    protected void runTaskLater(@NotNull Runnable task) {
        plugin.getProxy().getScheduler().buildTask(plugin, task)
                .delay(delay, TimeUnit.SECONDS)
                .schedule();
    }
}
