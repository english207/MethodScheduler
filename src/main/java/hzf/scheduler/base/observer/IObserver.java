package hzf.scheduler.base.observer;

/**
 * Created by huangzhenfeng on 2015/11/3.
 *
 */
public interface IObserver
{
    public void updateByCron(ISubject subject);
}
