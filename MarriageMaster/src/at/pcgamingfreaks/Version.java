package at.pcgamingfreaks;

import org.jetbrains.annotations.NotNull;

public class Version implements Comparable<Version> {
    private final String version;

    public Version(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return version;
    }

    @Override
    public int compareTo(@NotNull Version o) {
        return version.compareTo(o.version); // Simple comparison
    }
}
