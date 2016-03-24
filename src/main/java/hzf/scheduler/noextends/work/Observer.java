package hzf.scheduler.noextends.work;


import hzf.scheduler.base.observer.IObserver;
import hzf.scheduler.base.observer.ISubject;

import java.lang.reflect.Method;

/**
 * Created by huangzhenfeng on 2016/2/1.
 *
 */
public abstract class Observer implements IObserver
{
    public abstract void register(Object object, String method);

    @Override
    public void updateByCron(ISubject subject)
    {

    }

    class MethodRunnable implements Runnable
    {
        private Method method;
        private Object observer;
        public MethodRunnable(Method method, Object observer)
        {
            this.method = method;
            this.observer = observer;
        }

        @Override
        public void run()
        {
            try
            {
                method.invoke(observer);
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }
}
