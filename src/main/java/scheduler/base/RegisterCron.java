package scheduler.base;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import scheduler.observer.IObserver;
import scheduler.observer.Subject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhenfeng on 2015/11/4.
 *
 */
public class RegisterCron
{
    //  此处Map是记录有多少种Scheduleder
    private static Map<String, Subject> mapScheduler = new HashMap<String, Subject>();

    public synchronized static boolean registerWork(IObserver observer, String method, String cron)
    {
        Subject subject;
        String cronName = "Scheduleder" + cron.trim().replaceAll(" ","").hashCode();

        SchedulerFactory schedulerfactory = new StdSchedulerFactory();
        Scheduler scheduler;

        subject = mapScheduler.get(cronName);

        if (subject == null)
        {
            subject = new Subject();
            mapScheduler.put(cronName, subject);
        }
        else
        {
            subject.register(observer, method);
            return true;
        }

        try
        {
            scheduler = schedulerfactory.getScheduler();

            String group = "subject";

            JobDetail job = JobBuilder.newJob(SchedulerJob.class).withIdentity(cronName, group).build();

            job.getJobDataMap().put("subject", subject);

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(cronName, group)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .startNow().build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();

            subject.register(observer, method);
            return true;
        }
        catch(Exception e){ e.printStackTrace(); }

        return false;
    }

    public static boolean unRegisterWork(IObserver observer, String method)
    {
        if (observer == null && method == null)
        {
            throw new NullPointerException();
        }
        else
        {
            for (Subject subject : mapScheduler.values())
            {
                List<String> methods = subject.getMapJobList().get(observer);

                for (String method_tmp : methods)
                {
                        if (method_tmp.equals(method))
                        {
                            subject.unRegister(observer, method);
                            return true;
                        }
                }
            }
        }
        return false;
    }

}
