package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.MarriageMaster.MagicValues;
import at.pcgamingfreaks.MarriageMaster.Velocity.MarriageMaster;
import at.pcgamingfreaks.Message.Message;
import at.pcgamingfreaks.yaml.YAML;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Language implements at.pcgamingfreaks.MarriageMaster.Database.ILanguage {
    private final MarriageMaster plugin;
    private final Path dataDirectory;
    private YAML yaml;

    public Language(MarriageMaster plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
    }

    public boolean load(Config config) {
        try {
            Path langDir = dataDirectory.resolve("lang");
            if (!Files.exists(langDir)) Files.createDirectories(langDir);
            
            String langName = "en.yml"; 
            Path langFile = langDir.resolve(langName);
            if (!Files.exists(langFile)) {
                try (InputStream in = getClass().getClassLoader().getResourceAsStream("lang/en.yml")) {
                    if (in != null) {
                        Files.copy(in, langFile, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        Files.createFile(langFile);
                    }
                }
            }
            
            yaml = new YAML(langFile.toFile());
            return true;
        } catch (Exception e) {
            plugin.getLogger().error("Error loading language", e);
            return false;
        }
    }

    @Override
    public @NotNull YAML getLangE() {
        return yaml;
    }

    @Override
    public @NotNull String getTranslated(@NotNull String key) {
        if (yaml == null) return key;
        at.pcgamingfreaks.yaml.YamlValue value = yaml.getValue(key);
        if (value == null || value.getValue() == null) return key;
        String val = value.getValue();
        return val.replace("<heart>", "§c" + MagicValues.SYMBOL_HEART + "§r")
                  .replace("<smallheart>", "§c" + MagicValues.SYMBOL_SMALL_HEART + "§r");
    }

    @Override
    public @NotNull String getTranslatedPlaceholder(@NotNull String key) {
        return getTranslated(key);
    }

    @Override
    public @NotNull String getDialog(@NotNull String key) {
        return getTranslated(key);
    }

    @Override
    public @NotNull Message getMessage(@NotNull String path) {
        return new VelocityMessage(getTranslated(path));
    }
}
