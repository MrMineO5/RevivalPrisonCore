/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards;

public interface MessageExecutor {
    /**
     * Generate the message using the provided parameters
     *
     * @param name   Name
     * @param amount Amount
     * @return Message
     */
    String run(String name, int amount);
}
