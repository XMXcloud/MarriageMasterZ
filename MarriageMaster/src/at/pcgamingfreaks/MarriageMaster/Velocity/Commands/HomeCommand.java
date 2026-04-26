package at.pcgamingfreaks.MarriageMaster.Velocity.Commands;

import at.pcgamingfreaks.MarriageMaster.API.Home;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriagePlayer;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarryCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class HomeCommand extends MarryCommand {
    private final MarriageMaster plugin;

    public HomeCommand(MarriageMaster plugin) {
        super(plugin.getProxy(), "home", "Teleports you to your home", "marriagemaster.home", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        MarriagePlayer mPlayer = plugin.getPlayerData(player);

        if (!mPlayer.isMarried()) {
            player.sendMessage(net.kyori.adventure.text.Component.text("You are not married!"));
            return;
        }

        MarriagePlayer partner = null;
        if (mPlayer.getPartners().size() == 1) {
            partner = mPlayer.getPartners().iterator().next();
        } else if (args.length > 0) {
            for (MarriagePlayer p : mPlayer.getPartners()) {
                if (p.getName().equalsIgnoreCase(args[0])) {
                    partner = p;
                    break;
                }
            }
        }

        if (partner == null) {
            player.sendMessage(net.kyori.adventure.text.Component.text("Please specify a partner!"));
            return;
        }

        at.pcgamingfreaks.MarriageMaster.Velocity.API.Marriage marriage = mPlayer.getMarriageData(partner);
        if (marriage == null || marriage.getHome() == null) {
            player.sendMessage(net.kyori.adventure.text.Component.text("No home set!"));
            return;
        }

        Home home = marriage.getHome();
        String serverName = home.getHomeServer();
        if (serverName != null) {
            plugin.getProxy().getServer(serverName).ifPresent(s -> player.createConnectionRequest(s).fireAndForget());
        }
        
        player.sendMessage(net.kyori.adventure.text.Component.text("Teleporting to home..."));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull MarriageMasterPlugin getMarriagePlugin() {
        return plugin;
    }

    public void sendHome(java.util.UUID requester, java.util.UUID partner) {}
}
