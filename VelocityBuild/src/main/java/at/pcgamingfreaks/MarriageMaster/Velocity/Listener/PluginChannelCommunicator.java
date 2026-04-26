package at.pcgamingfreaks.MarriageMaster.Velocity.Listener;

import at.pcgamingfreaks.MarriageMaster.Velocity.Commands.HomeCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.Commands.TpCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import at.pcgamingfreaks.MarriageMaster.Database.PluginChannelCommunicatorBase;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class PluginChannelCommunicator extends PluginChannelCommunicatorBase {
    private final MarriageMaster plugin;
    private final LegacyChannelIdentifier channelIdentifier = new LegacyChannelIdentifier(CHANNEL_MARRIAGE_MASTER);
    private HomeCommand home = null;
    private TpCommand tp = null;

    public PluginChannelCommunicator(MarriageMaster plugin) {
        super(java.util.logging.Logger.getLogger("MarriageMaster"), plugin.getDatabase());
        this.plugin = plugin;
        plugin.getProxy().getChannelRegistrar().register(channelIdentifier);
        plugin.getProxy().getEventManager().register(plugin, this);

        sendMessage(buildStringMessage("UseUUIDs", "true"), true);
        sendMessage(buildStringMessage("UseUUIDSeparators", plugin.getConfig().useUUIDSeparators() + ""), true);
        sendMessage(buildStringMessage("UUID_Type", plugin.getConfig().useOnlineUUIDs() ? "online" : "offline"), true);

        logger.info("Velocity data sync handler initialized.");
    }

    public void setHomeCommand(HomeCommand home) {
        this.home = home;
    }

    public void setTpCommand(TpCommand tp) {
        this.tp = tp;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(channelIdentifier)) return;
        if (!(event.getSource() instanceof ServerConnection)) return;

        if (receive(event.getIdentifier().getId(), event.getData())) {
            // forward message to all other servers
            RegisteredServer source = ((ServerConnection) event.getSource()).getServer();
            for (RegisteredServer server : plugin.getProxy().getAllServers()) {
                if (server.equals(source)) continue;
                server.sendPluginMessage(channelIdentifier, event.getData());
            }
        }
    }

    @Override
    protected void receiveUnknownChannel(@NotNull String channel, byte[] bytes) {}

    @Override
    protected boolean receiveMarriageMaster(@NotNull String cmd, @NotNull DataInputStream inputStream) throws IOException {
        switch (cmd) {
            case "home":
                if (home != null) home.sendHome(UUID.fromString(inputStream.readUTF()), UUID.fromString(inputStream.readUTF()));
                break;
            case "tp":
                if (tp != null) tp.sendTP(UUID.fromString(inputStream.readUTF()), UUID.fromString(inputStream.readUTF()));
                break;
            default:
                return false;
        }
        return true;
    }

    protected void sendMessage(final @NotNull byte[] data, boolean queue) {
        for (RegisteredServer server : plugin.getProxy().getAllServers()) {
            server.sendPluginMessage(channelIdentifier, data);
        }
    }

    @Override
    protected void sendMessage(final @NotNull byte[] data) {
        sendMessage(data, false);
    }

    public void sendMessage(final @NotNull RegisteredServer server, String... msg) {
        server.sendPluginMessage(channelIdentifier, buildStringMessage(msg));
    }

    @Override
    public void close() {
        super.close();
        plugin.getProxy().getChannelRegistrar().unregister(channelIdentifier);
    }
}
