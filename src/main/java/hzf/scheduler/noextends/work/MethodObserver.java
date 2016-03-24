package hzf.scheduler.noextends.work;

import hzf.scheduler.base.observer.ISubject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhenfeng on 2016/2/1.
 *
 */
public class MethodObserver extends Observer
{
    private Map<Object, List<String>> mapJobList = new HashMap<Object, List<String>>();

    @Override
    public void register(Object observer, String method)
    {
        if (observer != null && method != null)
        {
            if (mapJobList.containsKey(observer))
            {
                List<String> list = mapJobList.get(observer);
                list.add(method);
            }
            else
            {
                List<String> list = new ArrayList<String>();
                list.add(method);
                mapJobList.put(observer, list);
            }
        }
    }

    public void unRegister(Object observer, String method)
    {
        if (observer == null || method == null)
        {
            throw new NullPointerException();
        }

        if (mapJobList.containsKey(observer))
        {
            List<String> list = mapJobList.get(observer);
            list.remove(method);
        }
    }

    @Override
    public void updateByCron(ISubject subject)
    {
        for (Object obj : mapJobList.keySet())
        {
            List<String> excuted_methods = mapJobList.get(obj);
            for (String excuted_method : excuted_methods)
            {
                try
                {
                    /**
                     *      参数长度为0即要保障执行的方法
                     *      所以执行的方法一定是要无参
                     */
                    Method method = obj.getClass().getDeclaredMethod(excuted_method);

                    new Thread(new MethodRunnable(method, obj)).start();
                    break;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
