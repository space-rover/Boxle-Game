package net.acomputerdog.boxle.block.sim.sim.state;

public class SimState {
    public static final SimState STOP_BRANCH_REQUESTED = new SimState("STOP_BRANCH_REQUESTED");
    public static final SimState STOP_SIM_REQUESTED = new SimState("STOP_SIM_REQUESTED");
    public static final SimState RUNNING = new SimState("RUNNING");
    public static final SimState FINISHED = new SimState("FINISHED");

    private final String state;

    protected SimState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimState)) return false;

        SimState simState = (SimState) o;

        return !(state != null ? !state.equals(simState.state) : simState.state != null);
    }

    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }
}
