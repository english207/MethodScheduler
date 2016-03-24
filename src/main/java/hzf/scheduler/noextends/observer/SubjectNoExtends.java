package hzf.scheduler.noextends.observer;

import hzf.scheduler.base.observer.IObserver;
import hzf.scheduler.base.observer.ISubject;

import java.util.*;

/**
 * Created by huangzhenfeng on 2015/11/3.
 *      自定义主题，以观察者为key，value为观察者的方法名称
 */
public class SubjectNoExtends implements ISubject<String>
{
    private Set<IObserver> jobServers = new HashSet<IObserver>();

    @Override
    public void register(IObserver observer, String type)
    {
        if (observer != null)
        {
            jobServers.add(observer);
        }
    }

    @Override
    public void unRegister(IObserver observer, String type)
    {
        if (observer != null)
        {
            jobServers.remove(observer);
        }
    }

    @Override
    public void notifyThem()
    {
        for (IObserver observer : jobServers)
        {
            observer.updateByCron(this);
        }
    }
}
