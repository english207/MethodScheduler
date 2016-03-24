package hzf.scheduler.simple.observer;

import hzf.scheduler.base.observer.IObserver;
import hzf.scheduler.base.observer.ISubject;

import java.util.*;

/**
 * Created by huangzhenfeng on 2015/11/3.
 *      自定义主题，以观察者为key，value为观察者的方法名称
 */
public class Subject implements ISubject<String>
{
    private Map<IObserver, List<String>> mapJobList = new HashMap<IObserver, List<String>>();

    @Override
    public void register(IObserver observer, String type)
    {
        //mapList.put(observer, type);

        if (observer != null && type != null)
        {
            if (mapJobList.containsKey(observer))
            {
                List<String> list = mapJobList.get(observer);
                list.add(type);
            }
            else
            {
                List<String> list = new ArrayList<String>();
                list.add(type);
                mapJobList.put(observer, list);
            }
        }
    }

    @Override
    public void unRegister(IObserver observer, String type)
    {
        if (observer != null && type != null)
        {
            if (mapJobList.containsKey(observer))
            {
                List<String> list = mapJobList.get(observer);

                for (String method : list)
                {
                    if (method.equals(type))
                    {
                        list.remove(method);
                        return;
                    }
                }

            }
        }

    }

    @Override
    public void notifyThem()
    {
        Set<IObserver> jobKeys = mapJobList.keySet();

        for (IObserver observer : jobKeys)
        {
            observer.updateByCron(this);
        }
    }

    public Map<IObserver, List<String>> getMapJobList() {
        return mapJobList;
    }

    public void setMapJobList(Map<IObserver, List<String>> mapJobList) {
        this.mapJobList = mapJobList;
    }
}
