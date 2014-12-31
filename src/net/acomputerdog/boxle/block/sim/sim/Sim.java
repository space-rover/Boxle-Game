package net.acomputerdog.boxle.block.sim.sim;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.program.Program;
import net.acomputerdog.boxle.block.sim.program.instruction.Instruction;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.sim.exec.SimExceptionUnknown;
import net.acomputerdog.boxle.block.sim.sim.state.SimState;
import net.acomputerdog.boxle.block.sim.sim.state.SimStateExec;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.core.logger.CLogger;

public class Sim {
    public static final CLogger LOGGER = new CLogger("Sim", false, true);

    private final Stack stack = new Stack(this);
    private final Program program;
    private SimState programState;

    public Sim(Program program) {
        this.program = program;
        stack.push(StackItem.NULL);
    }

    public SimResult startSim() throws SimException {
        programState = SimState.RUNNING;
        Block block = simulate();
        return new SimResult(stack, programState, block);
    }

    private Block simulate() throws SimException {
        InstructionTree tree = program.getInstructions();
        Block block = new Block(program.getId(), program.getName());
        try {
            //System.out.println(tree.getStartInstruction().toString());
            simBranch(tree.getStartInstruction(), stack, block);
            programState = SimState.FINISHED;
        } catch (SimException e) {
            this.programState = new SimStateExec(e);
            throw e;
        }
        return block;
    }

    private SimState simBranch(InstructionBranch branch, Stack stack, Block block) throws SimException {
        Instruction currIns = null;
        try {
            (currIns = branch.getInstruction()).execute(this, stack, block);
            SimState lastState = SimState.RUNNING;
            for (InstructionBranch subBranch : branch.getOutputs()) {
                lastState = simBranch(subBranch, stack, block);
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
