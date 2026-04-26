package at.pcgamingfreaks.MarriageMaster.API;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Home {
    private final String worldName;
    private final String homeServer;
    private final double x, y, z;
    private final float yaw, pitch;

    public Home(@NotNull String worldName, @Nullable String homeServer, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.homeServer = homeServer;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public @NotNull String getWorldName() { return worldName; }
    public @Nullable String getHomeServer() { return homeServer; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
}