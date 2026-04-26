package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.MarriageMaster.Database.DatabaseConfiguration;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import at.pcgamingfreaks.yaml.YAML;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Logger;

public class Config implements DatabaseConfiguration {
    private final MarriageMaster plugin;
    private final Path dataDirectory;
    private Map<String, Object> configMap = new HashMap<>();
    private YAML yaml;

    public Config(MarriageMaster plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
    }

    public boolean load() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            Path configFile = dataDirectory.resolve("config.yml");
            if (!Files.exists(configFile)) {
                try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                    if (in != null) {
                        Files.copy(in, configFile, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        // Fallback if resource not found
                        Files.createFile(configFile);
                    }
                }
            }
            
            yaml = new YAML(configFile.toFile());
            
            Yaml sYaml = new Yaml();
            try (InputStream in = Files.newInputStream(configFile)) {
                configMap = sYaml.load(in);
                if (configMap == null) configMap = new HashMap<>();
            }
            return true;
        } catch (Exception e) {
            plugin.getLogger().error("Error loading config", e);
            return false;
        }
    }

    @Override
    public YAML getConfigE() {
        return yaml;
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("MarriageMaster");
    }

    public void reload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private Object get(String path, Object def) {
        String[] keys = path.split("\\.");
        Object current = configMap;
        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                return def;
            }
        }
        return current != null ? current : def;
    }

    private boolean getBoolean(String path, boolean def) {
        Object val = get(path, def);
        if (val instanceof Boolean) return (Boolean) val;
        if (val instanceof String) return Boolean.parseBoolean((String) val);
        return def;
    }

    private String getString(String path, String def) {
        Object val = get(path, def);
        return val != null ? val.toString() : def;
    }

    @SuppressWarnings("unchecked")
    private List<String> getStringList(String path) {
        Object val = get(path, null);
        if (val instanceof List) return (List<String>) val;
        return Collections.emptyList();
    }

    public boolean areMultiplePartnersAllowed() {
        return getBoolean("Marriage.AllowMultiplePartners", false);
    }

    public boolean isSelfMarriageAllowed() {
        return getBoolean("Marriage.AllowSelfMarriage", false);
    }

    public boolean isSurnamesEnabled() {
        return getBoolean("Marriage.Surnames.Enable", false);
    }

    public boolean isSurnamesForced() {
        return getBoolean("Marriage.Surnames.Force", false);
    }

    @Override
    public boolean useOnlineUUIDs() {
        String type = getString("Database.UUID_Type", "auto").toLowerCase(Locale.ENGLISH);
        if (type.equals("auto")) {
            return plugin.getProxy().getConfiguration().isOnlineMode();
        }
        return type.equals("online");
    }

    public boolean isJoinLeaveInfoEnabled() {
        return getBoolean("InfoOnPartnerJoinLeave.Enable", true);
    }

    public long getJoinInfoDelay() {
        Object val = get("InfoOnPartnerJoinLeave.JoinDelay", 0);
        if (val instanceof Number) return ((Number) val).longValue();
        return 0;
    }

    public String getUpdateChannel() {
        return getString("Misc.AutoUpdate.Channel", "Release");
    }

    public boolean useUpdater() {
        return getBoolean("Misc.AutoUpdate.Enable", true);
    }

    private List<String> toLowerCase(List<String> list) {
        List<String> out = new ArrayList<>();
        for (String s : list) out.add(s.toLowerCase(Locale.ENGLISH));
        return out;
    }
}
