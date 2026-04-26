package at.pcgamingfreaks.MarriageMaster.Velocity.SpecialInfoWorker;

import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class DbErrorLoadingDataInfo {
    private final MarriageMaster plugin;
    private final String error;

    public DbErrorLoadingDataInfo(MarriageMaster plugin, String error) {
        this.plugin = plugin;
        this.error = error;
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        if (event.getPlayer().hasPermission("marriagemaster.admin")) {
            event.getPlayer().sendMessage(net.kyori.adventure.text.Component.text("§c[MarriageMaster] Error loading data: " + error));
        }
    }
}
