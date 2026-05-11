package domain.model;

public class TurnState {
    private int turnsToTake;
    private boolean skipDraw;
    private boolean isAttacking;
    private Card pendingAction;
    private int nopeCount;

    public void enableSkipDraw() {}
    public void startAttack(int turns) {}
    public void setPendingAction(Card card) {}
    public void clearPendingAction() {}
    public void incrementNopeCount() {}
    public void decrementTurns() {}
    public boolean shouldSkipDraw() { return false; }
    public boolean isAttacking() { return false; }
    public int nopeCount() { return 0; }
    public int turnsRemaining() { return 0; }
    public void reset() {}
}