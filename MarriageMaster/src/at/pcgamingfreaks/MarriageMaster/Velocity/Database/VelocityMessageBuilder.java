package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.Message.MessageBuilder;
import at.pcgamingfreaks.Message.MessageComponent;

import java.util.Collection;

public class VelocityMessageBuilder extends MessageBuilder<VelocityMessageBuilder, VelocityMessage> {
    public VelocityMessageBuilder() {
        super();
    }

    public VelocityMessageBuilder(String text) {
        super(text);
    }

    public VelocityMessageBuilder(MessageComponent component) {
        super(component);
    }

    public VelocityMessageBuilder(Collection<? extends MessageComponent> components) {
        super(components);
    }

    @Override
    public VelocityMessage getMessage() {
        return new VelocityMessage(getClassicMessage());
    }
}
