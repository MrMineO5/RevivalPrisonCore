/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;

public class Protocol {
    private static ProtocolManager protocolManager;

    public static void init() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public static void addListener(PacketAdapter adapter) {
        protocolManager.addPacketListener(adapter);
    }
}
