/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

public class Validator {
    public static boolean validate(String[] in, Class<?>... args) {
        if (in.length != args.length) {
            return false;
        }
        for (int i = 0; i < in.length; i++) {
            Argument<?> arg = Arguments.getArgument(args[i]);
            if (arg.isNotValid(in[i])) {
                return false;
            }
        }
        return true;
    }

    public static String getError(String[] in, Class<?>... args) {
        if (in.length != args.length) {
            return "Not enough arguments";
        }
        for (int i = 0; i < in.length; i++) {
            Argument<?> arg = Arguments.getArgument(args[i]);
            if (arg.isNotValid(in[i])) {
                return "Invalid " + args[i].getSimpleName();
            }
        }
        return "Valid";
    }

    public static Object[] getValues(String[] in, Class<?>... args) {
        if (!validate(in, args)) {
            throw new IllegalArgumentException();
        }
        Object[] out = new Object[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = Arguments.getArgument(args[i]).parse(in[i]);
        }
        return out;
    }
}