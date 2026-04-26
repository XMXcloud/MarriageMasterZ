package at.pcgamingfreaks.MarriageMaster.Velocity;

import at.pcgamingfreaks.MarriageMaster.Velocity.Database.Language;
import at.pcgamingfreaks.MarriageMaster.Velocity.Database.VelocityMessage;

public class CommonMessages {
    public static VelocityMessage NO_PERMISSION;
    public static VelocityMessage PLAYER_ONLY;
    public static VelocityMessage NOT_MARRIED;

    public static void load(Language language) {
        NO_PERMISSION = (VelocityMessage) language.getMessage("General.NoPermission");
        PLAYER_ONLY = (VelocityMessage) language.getMessage("General.PlayerOnly");
        NOT_MARRIED = (VelocityMessage) language.getMessage("General.NotMarried");
    }
}
