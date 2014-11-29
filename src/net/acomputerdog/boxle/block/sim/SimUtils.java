package net.acomputerdog.boxle.block.sim;


import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.InvalidStackDataException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

import java.util.ArrayList;
import java.util.List;

public class SimUtils {
    public static boolean verifyStack(Stack stack, String... types) {
        if (types.length == 0) {
            return true;
        }
        if (types.length > stack.size()) {
            return false;
        }
        List<StackItem> stacks = new ArrayList<>();
        boolean result = true;
        for (String type : types) {
            StackItem item = stack.pop();
            stacks.add(item);
            if (item != null && type != null) {
                if (!type.equals(item.getType())) {
                    result = false;
                    break;
                }
            }
            if ((item == null) != (type == null)) {
                result = false;
                break;
            }
        }
        for (int index = stacks.size() - 1; index >= 0; index--) {
            stack.push(stacks.get(index));
        }
        return result;
    }

    public static void verifyStack(Sim sim, Atom atom, Stack stack, String... types) throws InvalidStackDataException {
        if (!verifyStack(stack, types)) {
            throw new InvalidStackDataException(sim, atom, "Invalid stack items!");
        }
    }
}
