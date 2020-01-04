/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui;

import net.ultradev.prisoncore.utils.text.HiddenStringUtils;

public class InventoryTitleBuilder {
    private String name;
    private boolean noput;

    public InventoryTitleBuilder(String name) {
        this.name = name;
    }

    public InventoryTitleBuilder noPut() {
        this.noput = true;
        return this;
    }

    public String create() {
        return name + (noput ? HiddenStringUtils.encodeString("noPut") : "");
    }
}
