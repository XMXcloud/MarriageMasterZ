package at.pcgamingfreaks.MarriageMaster.Velocity.API;

import com.velocitypowered.api.command.CommandSource;

/**
 * The command manager is responsible for managing all the command stuff of the plugin.
 * It provides functions to register/unregister sub-commands, requests that need to be accepted and switches translated in the language file.
 */
@SuppressWarnings("unused")
public interface CommandManager extends at.pcgamingfreaks.MarriageMaster.API.CommandManager<MarryCommand, CommandSource>
{}
