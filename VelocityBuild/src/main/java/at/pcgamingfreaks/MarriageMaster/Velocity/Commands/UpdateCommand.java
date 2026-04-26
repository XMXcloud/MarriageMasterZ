package at.pcgamingfreaks.MarriageMaster.Velocity.Commands;

import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarryCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class UpdateCommand extends MarryCommand {
    private final MarriageMaster plugin;

    public UpdateCommand(MarriageMaster plugin) {
        super(plugin.getProxy(), "update", "Checks for updates", "marriagemaster.admin");
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        sender.sendMessage(net.kyori.adventure.text.Component.text("Current version: " + plugin.getVersion()));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull MarriageMasterPlugin getMarriagePlugin() {
        return plugin;
    }
}
