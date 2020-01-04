/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

public class EnumArgument<T extends Enum<T>> implements Argument<T> {
    private Class<T> clazz;

    EnumArgument(Class<T> arg) {
        this.clazz = arg;
    }

    public boolean isNotValid(String str) {
        try {
            Enum.valueOf(this.clazz, str.toUpperCase());
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public T parse(String str) {
        return Enum.valueOf(this.clazz, str.toUpperCase());
    }
}
