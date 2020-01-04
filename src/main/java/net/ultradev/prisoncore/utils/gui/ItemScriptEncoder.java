/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ItemScriptEncoder {
    static String START_FUNCTION_BLOCK = "[F-Block]";
    static String START_ARG_BLOCK = "[arg]";
    static String END_ARG_BLOCK = "[/arg]";
    static String IN_FUNCTION_SEPERATOR = "[e]";
    static String END_FUNCTION_BLOCK = "[/F-Block]";
    private static String FUNCTION_SEPERATOR = "[end]";

    @Nullable
    private static Function decodeInstruction(@NotNull String inst) {
        if (!(inst.startsWith(START_FUNCTION_BLOCK) && inst.endsWith(END_FUNCTION_BLOCK))) {
            return null;
        }
        inst = inst.substring(START_FUNCTION_BLOCK.length(), inst.length() - END_FUNCTION_BLOCK.length());
        String[] spl = inst.split(Pattern.quote(IN_FUNCTION_SEPERATOR));
        String function = spl[0];
        if (spl.length > 1) {
            String[] args = new String[spl.length - 1];
            for (int i = 1; i < spl.length; i++) {
                String arg = spl[i];
                if (!arg.startsWith(START_ARG_BLOCK) || !arg.endsWith(END_ARG_BLOCK)) {
                    continue;
                }
                args[i - 1] = arg.substring(START_ARG_BLOCK.length(), arg.length() - END_ARG_BLOCK.length());
            }
            return new Function(function, args);
        }
        return new Function(function);
    }

    @NotNull
    static Function[] decodeInstructions(@NotNull String inst) {
        String[] insts = inst.split(Pattern.quote(FUNCTION_SEPERATOR));
        Function[] funcs = new Function[insts.length];
        for (int i = 0; i < insts.length; i++) {
            funcs[i] = decodeInstruction(insts[i]);
        }
        return funcs;
    }

    static String encodeInstructions(String[][] funcs) {
        return Arrays.stream(funcs)
                .map((func) -> Function.parse(func).encodeInstruction())
                .collect(Collectors.joining(FUNCTION_SEPERATOR));
    }

	/*
	private static String encodeInstructions(Function[] funcs) {
		return Arrays.stream(funcs)
				.map(Function::encodeInstruction)
				.collect(Collectors.joining(FUNCTION_SEPERATOR));
	}
	 */
}
