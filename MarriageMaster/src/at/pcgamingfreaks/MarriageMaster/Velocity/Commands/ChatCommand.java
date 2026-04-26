package at.pcgamingfreaks.MarriageMaster.Velocity.Commands;

import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriagePlayer;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarryCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatCommand extends MarryCommand {
    private final MarriageMaster plugin;

    public ChatCommand(MarriageMaster plugin) {
        super(plugin.getProxy(), "chat", "Private chat with your partner", "marriagemaster.chat", true, true);
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;
        MarriagePlayer mPlayer = plugin.getPlayerData(player);

        if (!mPlayer.isMarried()) {
            plugin.getLanguage().getMessage("Command.Chat.NotMarried").send(sender);
            return;
        }

        if (args.length == 0) {
            plugin.getLanguage().getMessage("Command.Chat.Usage").send(sender);
            return;
        }

        String msg = String.join(" ", args);
        sendChat(mPlayer, msg);
    }

    private void sendChat(MarriagePlayer sender, String message) {
        for (MarriagePlayer partner : sender.getPartners()) {
            if (partner.isOnline()) {
                partner.sendMessage("§d[MarriageChat] " + sender.getDisplayName() + ": §f" + message);
            }
        }
        sender.sendMessage("§d[MarriageChat] " + sender.getDisplayName() + ": §f" + message);
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    @Override
    public @NotNull MarriageMasterPlugin getMarriagePlugin() {
        return plugin;
    }
}
