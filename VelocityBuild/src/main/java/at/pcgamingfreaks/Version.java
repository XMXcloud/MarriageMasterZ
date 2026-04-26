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
        if (version == null || o.version == null) return 0;
        return version.compareTo(o.version); 
    }

    public boolean olderThan(String other) {
        return compareTo(new Version(other)) < 0;
    }

    public boolean olderThan(Version other) {
        return compareTo(other) < 0;
    }

    public boolean newerThan(String other) {
        return compareTo(new Version(other)) > 0;
    }

    public boolean newerThan(Version other) {
        return compareTo(other) > 0;
    }
}
