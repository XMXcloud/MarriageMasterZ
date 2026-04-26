package at.pcgamingfreaks.MarriageMaster.Velocity.SpecialInfoWorker;

import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class NoDatabaseWorker {
    private final MarriageMaster plugin;

    public NoDatabaseWorker(MarriageMaster plugin) {
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        if (event.getPlayer().hasPermission("marriagemaster.admin")) {
            event.getPlayer().sendMessage(net.kyori.adventure.text.Component.text("§c[MarriageMaster] Failed to connect to database! Please check your settings."));
        }
    }
}
