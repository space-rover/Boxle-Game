package net.acomputerdog.boxle.block.util;


import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

import java.util.ArrayList;
import java.util.List;

public class SimUtils {
    public static boolean verifyStack(Stack stack, String... types) {
        if (types.length == 0) {
            return true;
        }
        List<StackItem> stacks = new ArrayList<>();
        boolean result = true;
        for (String str : types) {
            StackItem item = stack.pop();
            stacks.add(item);
            if (item != null && str != null) {
                if (!str.equals(item.getType())) {
                    result = false;
                    break;
                }
            }
            if ((item == null) != (str == null)) {
                result = false;
                break;
            }
        }
        for (int index = stacks.size() - 1; index >= 0; index--) {
            stack.push(stacks.get(index));
        }
        return result;
    }
}
