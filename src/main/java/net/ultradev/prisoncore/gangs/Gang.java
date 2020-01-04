/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.gangs;

import lombok.Getter;
import net.ultradev.prisoncore.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Gang {
    @Getter
    private String name;
    @Getter
    private String displayname;
    @Getter
    private UUID owner;
    @Getter
    private Map<UUID, GangRank> members = new HashMap<>();
    private List<UUID> invited = new ArrayList<>();

    /**
     * Create a gang
     *
     * @param displayname Gang name
     * @param owner       Gang owner
     */
    public Gang(@NotNull String displayname, UUID owner) {
        this.name = displayname.toLowerCase();
        this.displayname = displayname;
        this.owner = owner;
        members.put(owner, GangRank.OWNER);
        OfflinePlayer op = Bukkit.getPlayer(owner);
        PlayerData.setGang(op, name);
    }

    private Gang(String name, String displayname, UUID owner, Map<UUID, GangRank> members) {
        this.name = name;
        this.displayname = displayname;
        this.owner = owner;
        this.members = members;
    }

    /**
     * Load a gang from the configuration section
     *
     * @param cfg Configuration Section
     * @return Loaded gang
     */
    @NotNull
    @Contract("_ -> new")
    public static Gang loadGang(@NotNull ConfigurationSection cfg) {
        String name = cfg.getString("name");
        String displayname = cfg.getString("displayName");
        UUID owner = UUID.fromString(cfg.getString("owner"));
        Map<UUID, GangRank> members = new HashMap<>();
        for (String str : cfg.getConfigurationSection("members").getKeys(false)) {
            members.put(UUID.fromString(str), GangRank.valueOf(cfg.getString("members." + str)));
        }
        return new Gang(name, displayname, owner, members);
    }

    /**
     * Check whether a player is on the member list
     *
     * @param player Player
     * @return <code>true</code> if the player is on the member list
     */
    public boolean isMember(UUID player) {
        return members.containsKey(player);
    }

    /**
     * Add a player to the member list
     *
     * @param player Player
     * @return <code>true</code> if the player was added, <code>false</code> is the player was already a member
     */
    public boolean addMember(UUID player) {
        if (this.members.containsKey(player)) {
            return false;
        }
        this.members.put(player, GangRank.MEMBER);
        PlayerData.setGang(Bukkit.getOfflinePlayer(player), this.name);
        return true;
    }

    /**
     * Remove a member from the member list
     *
     * @param player Player
     * @return <code>true</code> if the member was removed, <code>false</code> if the player was not a member
     */
    public boolean removeMember(UUID player) {
        if (!this.members.containsKey(player)) {
            return false;
        }
        this.members.remove(player);
        return true;
    }

    /**
     * Check whether a player is on the invite list
     *
     * @param player Player
     * @return <code>true</code> if the player is on the invite list
     */
    public boolean isInvited(UUID player) {
        return this.invited.contains(player);
    }

    /**
     * Add a player to the invite list
     *
     * @param player Player
     */
    public void addInvite(UUID player) {
        if (!this.invited.contains(player)) {
            this.invited.add(player);
        }
    }

    /**
     * Remove an invited player from the invite list
     *
     * @param player Player
     */
    public void removeInvite(UUID player) {
        this.invited.remove(player);
    }

    /**
     * Save the current data into the configuration section
     *
     * @param cfg Configuration Section
     */
    public void save(@NotNull ConfigurationSection cfg) {
        cfg.set("name", this.name);
        cfg.set("displayName", this.displayname);
        cfg.set("owner", this.owner.toString());
        for (Map.Entry<UUID, GangRank> entry : members.entrySet()) {
            cfg.set("members." + entry.getKey().toString(), entry.getValue().name());
        }
    }

    /**
     * Get a player's rank
     *
     * @param id UUID of player
     * @return Rank of player
     */
    public GangRank getRank(UUID id) {
        return members.get(id);
    }
}
