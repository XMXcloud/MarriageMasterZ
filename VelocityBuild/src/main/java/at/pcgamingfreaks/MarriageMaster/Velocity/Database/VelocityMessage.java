package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.Message.Message;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class VelocityMessage extends Message<VelocityMessage, Player, CommandSource> {
    private String text;

    public VelocityMessage(String text) {
        super(text);
        this.text = text;
    }

    @Override
    public void send(CommandSource target, Object... args) {
        String msg = prepareMessage(false, args);
        Component component = LegacyComponentSerializer.legacySection().deserialize(msg.replace('&', '§'));
        target.sendMessage(component);
    }

    @Override
    public void send(Collection<? extends Player> players, Object... args) {
        String msg = prepareMessage(false, args);
        Component component = LegacyComponentSerializer.legacySection().deserialize(msg.replace('&', '§'));
        for (Player p : players) {
            p.sendMessage(component);
        }
    }

    @Override
    public void broadcast(Object... args) {
        // Implementation for broadcast if needed
    }
}
