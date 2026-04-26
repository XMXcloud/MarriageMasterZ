package at.pcgamingfreaks.MarriageMaster.Velocity.SpecialInfoWorker;

import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class UpgradedInfo {
    private final MarriageMaster plugin;

    public UpgradedInfo(MarriageMaster plugin) {
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        if (event.getPlayer().hasPermission("marriagemaster.admin")) {
            event.getPlayer().sendMessage(net.kyori.adventure.text.Component.text("§a[MarriageMaster] Plugin has been upgraded to version " + plugin.getVersion()));
        }
    }
}
