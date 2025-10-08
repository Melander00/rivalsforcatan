package samuel.point;

public record PointAmount(Class<? extends Point> pointClass, int amount) implements IPointAmount {

    @Override
    public Class<? extends Point> getPointType() {
        return pointClass;
    }

    @Override
    public int getAmount() {
        return amount;
    }
}
