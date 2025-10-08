package samuel.point;

public interface IPointAmount {
    Class<? extends Point> getPointType();
    int getAmount();

}
