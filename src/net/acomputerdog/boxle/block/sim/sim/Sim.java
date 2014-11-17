package net.acomputerdog.boxle.block.sim.sim;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.program.Program;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.sim.exec.SimExceptionUnknown;
import net.acomputerdog.boxle.block.sim.sim.state.SimState;
import net.acomputerdog.boxle.block.sim.sim.state.SimStateExec;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

public class Sim {
    private final Stack stack = new Stack(this);
    private final Program program;
    private SimState programState;

    public Sim(Program program) {
        this.program = program;
        stack.push(StackItem.NULL);
    }

    public SimResult startSim() {
        programState = SimState.RUNNING;
        simulate();
        return new SimResult(stack, programState);
    }

    private void simulate() {
        InstructionTree tree = program.getInstructions();
        try {
            simBranch(tree.getStartInstruction(), stack);
            programState = SimState.FINISHED;
        } catch (SimException e) {
            this.programState = new SimStateExec(e);
        }
    }

    private SimState simBranch(InstructionBranch branch, Stack stack) throws SimException {
        Instruction currIns = null;
        try {
            (currIns = branch.getInstruction()).execute(this, stack);
            SimState lastState = SimState.RUNNING;
            for (InstructionBranch subBranch : branch.getOutputs()) {
                Stack branchStack = stack.copy();
                lastState = simBranch(subBranch, branchStack);
                if (!lastState.equals(SimState.RUNNING) || !programState.equals(SimState.RUNNING)) {
                    break;
                }
            }
            return lastState;
        } catch (Exception exec) {
            if (!(exec instanceof SimException)) {
                throw new SimExceptionUnknown(this, currIns, exec);
            } else {
                throw (SimException) exec;
            }
        }
    }

    public void stopCurrBranch() {
        this.programState = SimState.STOP_BRANCH_REQUESTED;
    }

    public void stopProgram() {
        this.programState = SimState.STOP_SIM_REQUESTED;
    }
}
