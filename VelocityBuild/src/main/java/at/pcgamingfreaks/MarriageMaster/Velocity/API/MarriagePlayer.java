package at.pcgamingfreaks.MarriageMaster.Velocity.API;

import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MarriagePlayer extends at.pcgamingfreaks.MarriageMaster.API.MarriagePlayer<Marriage, MarriagePlayer>
{
	/**
	 * Gets the Player that is represented by this marriage player data.
	 *
	 * @return The Player represented by this marriage player. null if offline.
	 */
	@Nullable Player getPlayer();

	boolean canSee(Player player);

	/**
	 * Checks if the player is married to a given player.
	 *
	 * @param player The player to be checked.
	 * @return True if they are married. False if not.
	 */
	boolean isPartner(@NotNull Player player);
}
