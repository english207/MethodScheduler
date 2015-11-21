package scheduler.observer;

/**
 * Created by huangzhenfeng on 2015/11/3.
 *
 */
public interface ISubject<T>
{
    public void register(IObserver observer, T type);
    public void unRegister(IObserver observer, T type);
    public void notifyThem();
}
