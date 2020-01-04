/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

public interface Argument<T> {
    boolean isNotValid(String str);

    T parse(String str);
}
