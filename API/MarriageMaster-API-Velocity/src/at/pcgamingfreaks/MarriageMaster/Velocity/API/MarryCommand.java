package at.pcgamingfreaks.MarriageMaster.Velocity.API;

import at.pcgamingfreaks.Command.HelpData;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class MarryCommand implements at.pcgamingfreaks.MarriageMaster.API.MarryCommand<MarriageMasterPlugin, CommandSource>
{
	protected final ProxyServer server;
	private final String name;
	private final String description;
	private final String permission;
	private final List<String> aliases;
	
	protected boolean playerOnly = false;
	protected boolean mustBeMarried = false;
	protected boolean partnerSelectorInHelp = false;

	protected MarryCommand(ProxyServer server, String name, String description, String permission, String... aliases)
	{
		this.server = server;
		this.name = name;
		this.description = description;
		this.permission = permission;
		this.aliases = aliases != null ? Arrays.asList(aliases) : Collections.emptyList();
	}

	protected MarryCommand(ProxyServer server, String name, String description, String permission, boolean playerOnly, String... aliases)
	{
		this(server, name, description, permission, aliases);
		this.playerOnly = playerOnly;
	}

	protected MarryCommand(ProxyServer server, String name, String description, String permission, boolean mustBeMarried, boolean partnerSelectorInHelp, String... aliases)
	{
		this(server, name, description, permission, aliases);
		this.playerOnly = true;
		this.mustBeMarried = mustBeMarried;
		this.partnerSelectorInHelp = partnerSelectorInHelp;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}

	@Override
	public @NotNull String getTranslatedName()
	{
		return name; // Should be overridden if translation is needed
	}

	@Override
	public @NotNull String getDescription()
	{
		return description;
	}

	@Override
	public @Nullable String getPermission()
	{
		return permission;
	}

	@Override
	public @NotNull List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public void registerSubCommands() {}

	@Override
	public void unRegisterSubCommands() {}

	@Override
	public void close() {}

	@Override
	public boolean canUse(@NotNull CommandSource sender)
	{
		if (playerOnly && !(sender instanceof Player)) return false;
		if (permission != null && !sender.hasPermission(permission)) return false;
		if (mustBeMarried && sender instanceof Player)
		{
			return getMarriagePlugin().getPlayerData((Player) sender).isMarried();
		}
		return true;
	}

	@Override
	public @Nullable List<HelpData> doGetHelp(@NotNull CommandSource requester)
	{
		if (!canUse(requester)) return null;
		return getHelp(requester);
	}

	@Override
	public @Nullable List<HelpData> getHelp(@NotNull CommandSource requester)
	{
		return Collections.singletonList(new HelpData(name, description, permission));
	}

	@Override
	public void showHelp(@NotNull CommandSource sendTo, @NotNull String usedMarryAlias)
	{
		// TODO: Implement help display
	}

	@Override
	public abstract void execute(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args);

	@Override
	public abstract List<String> tabComplete(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args);

	@Override
	public void doExecute(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args)
	{
		if (playerOnly && !(sender instanceof Player))
		{
			// send error msg
			return;
		}
		if (permission != null && !sender.hasPermission(permission))
		{
			// send error msg
			return;
		}
		if (mustBeMarried && sender instanceof Player && !getMarriagePlugin().getPlayerData((Player) sender).isMarried())
		{
			// send error msg
			return;
		}
		execute(sender, mainCommandAlias, alias, args);
	}

	@Override
	public List<String> doTabComplete(@NotNull CommandSource sender, @NotNull String mainCommandAlias, @NotNull String alias, @NotNull String[] args)
	{
		if (!canUse(sender)) return null;
		return tabComplete(sender, mainCommandAlias, alias, args);
	}
}
