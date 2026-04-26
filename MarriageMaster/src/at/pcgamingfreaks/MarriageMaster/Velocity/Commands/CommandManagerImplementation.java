package at.pcgamingfreaks.MarriageMaster.Velocity.Commands;

import at.pcgamingfreaks.MarriageMaster.Velocity.API.CommandManager;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarryCommand;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManagerImplementation implements CommandManager {
    private final MarriageMaster plugin;
    private final List<MarryCommand> commands = new ArrayList<>();

    public CommandManagerImplementation(MarriageMaster plugin) {
        this.plugin = plugin;
    }

    public void init() {}

    public void close() {}

    @Override
    public boolean isOnSwitch(@Nullable String str) {
        return str != null && str.equalsIgnoreCase("on");
    }

    @Override
    public boolean isOffSwitch(@Nullable String str) {
        return str != null && str.equalsIgnoreCase("off");
    }

    @Override
    public boolean isToggleSwitch(@Nullable String str) {
        return str != null && str.equalsIgnoreCase("toggle");
    }

    @Override
    public boolean isAllSwitch(@Nullable String str) {
        return str != null && str.equalsIgnoreCase("all");
    }

    @Override
    public boolean isRemoveSwitch(@Nullable String str) {
        return str != null && str.equalsIgnoreCase("remove");
    }

    @Override
    public @NotNull String getOnSwitchTranslation() {
        return "on";
    }

    @Override
    public @NotNull String getOffSwitchTranslation() {
        return "off";
    }

    @Override
    public @NotNull String getToggleSwitchTranslation() {
        return "toggle";
    }

    @Override
    public @NotNull String getAllSwitchTranslation() {
        return "all";
    }

    @Override
    public @NotNull String getRemoveSwitchTranslation() {
        return "remove";
    }

    @Override
    public @Nullable List<String> getSimpleTabComplete(@NotNull CommandSource sender, @Nullable String... args) {
        return Collections.emptyList();
    }

    @Override
    public void registerSubCommand(@NotNull MarryCommand command) {
        commands.add(command);
    }

    @Override
    public void unRegisterSubCommand(@NotNull MarryCommand command) {
        commands.remove(command);
    }
}
