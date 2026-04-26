package at.pcgamingfreaks.MarriageMaster.Velocity.Commands;

import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriagePlayer;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarryCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TpCommand extends MarryCommand {
    private final MarriageMaster plugin;

    public TpCommand(MarriageMaster plugin) {
        super(plugin.getProxy(), "tp", "Teleports you to your partner", "marriagemaster.tp", true);
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
            player.sendMessage(net.kyori.adventure.text.Component.text("Partner not found!"));
            return;
        }

        Player pPartner = partner.getPlayer();
        if (pPartner == null) {
            player.sendMessage(net.kyori.adventure.text.Component.text("Partner is offline!"));
            return;
        }

        pPartner.getCurrentServer().ifPresent(server -> player.createConnectionRequest(server.getServer()).fireAndForget());
        player.sendMessage(net.kyori.adventure.text.Component.text("Teleporting to partner..."));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull MarriageMasterPlugin getMarriagePlugin() {
        return plugin;
    }

    public void sendTP(java.util.UUID requester, java.util.UUID partner) {}
}
