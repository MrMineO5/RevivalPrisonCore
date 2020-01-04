/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.betterspigot;

import net.ultradev.prisoncore.Main;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class EntityMeta {
    public static MetadataValue createMetadataValue(String v) {
        return new MetadataValue() {
            private boolean valid = true;

            @Override
            public Object value() {
                assert valid;
                return v;
            }

            @Override
            public int asInt() {
                assert valid;
                return 0;
            }

            @Override
            public float asFloat() {
                assert valid;
                return 0;
            }

            @Override
            public double asDouble() {
                assert valid;
                return 0;
            }

            @Override
            public long asLong() {
                assert valid;
                return 0;
            }

            @Override
            public short asShort() {
                assert valid;
                return 0;
            }

            @Override
            public byte asByte() {
                assert valid;
                return 0;
            }

            @Override
            public boolean asBoolean() {
                assert valid;
                return false;
            }

            @Override
            public String asString() {
                assert valid;
                return v;
            }

            @Override
            public Plugin getOwningPlugin() {
                return Main.getInstance();
            }

            @Override
            public void invalidate() {
                valid = false;
            }
        };
    }
}
