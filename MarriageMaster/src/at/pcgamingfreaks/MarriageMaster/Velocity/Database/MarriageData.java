package at.pcgamingfreaks.MarriageMaster.Velocity.Database;

import at.pcgamingfreaks.MarriageMaster.API.Home;
import at.pcgamingfreaks.MarriageMaster.Database.MarriageDataBase;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.Marriage;
import at.pcgamingfreaks.MarriageMaster.Velocity.API.MarriagePlayer;
import at.pcgamingfreaks.Message.MessageColor;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class MarriageData extends MarriageDataBase<MarriagePlayer, CommandSource, Home> implements Marriage {

    public MarriageData(@NotNull MarriagePlayer player1, @NotNull MarriagePlayer player2, @Nullable MarriagePlayer priest, @NotNull Date weddingDate, @Nullable String surname,
                        boolean pvpEnabled, @Nullable MessageColor color, @Nullable Home home, @Nullable Object databaseKey) {
        super(player1, player2, priest, weddingDate, surname, pvpEnabled, color, home, databaseKey);
    }

    public MarriageData(@NotNull MarriagePlayer player1, @NotNull MarriagePlayer player2, @Nullable MarriagePlayer priest, @NotNull Date weddingDate, @Nullable String surname) {
        super(player1, player2, priest, weddingDate, surname);
    }

    @Override
    public boolean setSurname(String surname) {
        return false;
    }

    @Override
    public void setSurname(String surname, @NotNull CommandSource changer) {
        throw new UnsupportedOperationException("setSurname is currently not available on Velocity");
    }

    @Override
    public void setSurname(String surname, @NotNull MarriagePlayer changer) {
        throw new UnsupportedOperationException("setSurname is currently not available on Velocity");
    }

    @Override
    public void divorce(@NotNull CommandSource divorcedBy) {
        throw new UnsupportedOperationException("divorce is currently not available on Velocity");
    }

    @Override
    public void divorce(@NotNull MarriagePlayer divorcedBy) {
        throw new UnsupportedOperationException("divorce is currently not available on Velocity");
    }
}
