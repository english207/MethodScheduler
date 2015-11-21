package scheduler.base;

import scheduler.observer.IObserver;
import scheduler.observer.ISubject;
import scheduler.observer.Subject;

import java.lang.reflect.Method;
import java.util.List;

//Created by huangzhenfeng on 2015/7/22.
public class MethodScheduler<T>  implements IObserver
{
    public MethodScheduler registerWork(String method, String cron)
    {
        RegisterCron.registerWork(this, method, cron);
        return this;
    }

    public MethodScheduler unRegisterWork(String method)
    {
        RegisterCron.unRegisterWork(this, method);
        return this;
    }

    @Override
    public void updateByCron(ISubject subject)
    {
        Subject subjectString = (Subject) subject;
        List<String> list = subjectString.getMapJobList().get(this);

        for (String type : list)
        {
            try
            {
                Class c = Class.forName(this.getClass().getName());

                Method[] methods = c.getMethods();
                for (Method method : methods)
                {
                    /**
                     *   参数长度为0即要保障执行的方法
                     *      所以执行的方法一定是要无参
                     */
                    if (method.getName().equals(type) && method.getParameterTypes().length == 0)
                    {
                        new Thread(new MethodRunnable(method, this)).start();
                        break;
                    }
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }
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
