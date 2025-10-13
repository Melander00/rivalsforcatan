package samuel.player.action;

public record PlayerAction(PlayerActionEnum action, Object data) {

    public PlayerActionEnum getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }

}
