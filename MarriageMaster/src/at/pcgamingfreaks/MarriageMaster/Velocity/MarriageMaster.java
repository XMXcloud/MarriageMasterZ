package at.pcgamingfreaks.MarriageMaster.Velocity;

import at.pcgamingfreaks.MarriageMaster.Velocity.API.*;
import at.pcgamingfreaks.MarriageMaster.Velocity.Commands.*;
import at.pcgamingfreaks.MarriageMaster.Velocity.Database.Config;
import at.pcgamingfreaks.MarriageMaster.Velocity.Database.Database;
import at.pcgamingfreaks.MarriageMaster.Velocity.Database.Language;
import at.pcgamingfreaks.MarriageMaster.Velocity.Database.MarriagePlayerData;
import at.pcgamingfreaks.MarriageMaster.Velocity.Listener.JoinLeaveInfo;
import at.pcgamingfreaks.MarriageMaster.Velocity.Listener.PluginChannelCommunicator;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Plugin(id = "marriagemaster", name = "MarriageMaster", version = "2.9.2", authors = {"GeorgH93"})
public class MarriageMaster implements MarriageMasterPlugin {
    private static MarriageMaster instance;
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataFolder;

    private Config config;
    private Language language;
    private Database database;
    private CommandManagerImplementation commandManager;

    @Inject
    public MarriageMaster(ProxyServer proxy, Logger logger, @DataDirectory Path dataFolder) {
        instance = this;
        this.proxy = proxy;
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    public static MarriageMaster getInstance() { return instance; }
    public ProxyServer getProxy() { return proxy; }
    public Logger getLogger() { return logger; }
    public Path getDataFolder() { return dataFolder; }
    public Config getConfig() { return config; }
    public Language getLanguage() { return language; }
    public Database getDatabase() { return database; }
    public @NotNull CommandManagerImplementation getCommandManager() { return commandManager; }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing MarriageMaster for Velocity...");

        initializeMessageSystem();

        config = new Config(this, dataFolder);
        if (!config.load()) {
            logger.error("Failed to load config!");
            return;
        }

        language = new Language(this, dataFolder);
        if (!language.load(config)) {
            logger.error("Failed to load language!");
            return;
        }

        database = new Database(this);
        commandManager = new CommandManagerImplementation(this);
        registerCommands();

        proxy.getEventManager().register(this, new JoinLeaveInfo(this));
        
        MinecraftChannelIdentifier channel = MinecraftChannelIdentifier.from("marriagemaster:main");
        proxy.getChannelRegistrar().register(channel);
        proxy.getEventManager().register(this, new PluginChannelCommunicator(this));

        logger.info("MarriageMaster enabled!");
    }

    private void registerCommands() {
        HomeCommand home = new HomeCommand(this);
        TpCommand tp = new TpCommand(this);
        
        commandManager.registerSubCommand(home);
        commandManager.registerSubCommand(tp);
        commandManager.registerSubCommand(new ChatCommand(this));
        commandManager.registerSubCommand(new ReloadCommand(this));
        commandManager.registerSubCommand(new UpdateCommand(this));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (database != null) database.closePublic();
        logger.info("MarriageMaster disabled!");
    }

    @Override
    public @NotNull MarriagePlayerData getPlayerData(@NotNull Player player) {
        return database.getPlayer(player.getUniqueId());
    }

    @Override
    public @NotNull MarriagePlayerData getPlayerData(@NotNull UUID uuid) {
        return database.getPlayer(uuid);
    }

    @Override
    public @NotNull MarriagePlayerData getPlayerData(@NotNull String name) {
        // Simple implementation for proxy
        return null; 
    }

    @Override
    public @NotNull Collection<? extends Marriage> getMarriages() {
        return Collections.emptyList();
    }

    @Override
    public boolean areMultiplePartnersAllowed() {
        return config.areMultiplePartnersAllowed();
    }

    @Override
    public boolean isSelfMarriageAllowed() {
        return config.isSelfMarriageAllowed();
    }

    @Override
    public boolean isSelfDivorceAllowed() {
        return true;
    }

    @Override
    public boolean isSurnamesEnabled() {
        return config.isSurnamesEnabled();
    }

    @Override
    public boolean isSurnamesForced() {
        return config.isSurnamesForced();
    }

    @Override
    public void doDelayableTeleportAction(@NotNull DelayableTeleportAction action) {
        action.run();
    }

    @Override
    public @NotNull CommandManagerImplementation getCommandManager() {
        return commandManager;
    }

    @Override
    public @NotNull Collection<? extends MarriagePlayer> getPriestsOnline() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Collection<? extends MarriagePlayer> getPriests() {
        return Collections.emptyList();
    }

    public String getVersion() {
        return "2.9.2";
    }

    public void reload() {
        config.load();
        language.load(config);
    }

    private void initializeMessageSystem() {
        try {
            // Initialize Message system via reflection
            Class<?> messageClass = Class.forName("at.pcgamingfreaks.Message.Message");
            try {
                java.lang.reflect.Field field = messageClass.getDeclaredField("MESSAGE_BUILDER_CONSTRUCTOR");
                field.setAccessible(true);
                field.set(null, at.pcgamingfreaks.MarriageMaster.Velocity.Database.VelocityMessageBuilder.class.getConstructor());
            } catch (Exception e) {
                logger.warn("Failed to set MESSAGE_BUILDER_CONSTRUCTOR: " + e.getMessage());
            }
        } catch (Throwable e) {
            logger.error("Failed to initialize message system! This might cause issues with messages.", e);
        }
    }
}
