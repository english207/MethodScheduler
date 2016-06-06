package hzf.scheduler.noextends.work;

import hzf.scheduler.base.observer.IObserver;
import hzf.scheduler.base.observer.ISubject;
import hzf.scheduler.base.work.SchedulerJob;
import hzf.scheduler.noextends.observer.SubjectNoExtends;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangzhenfeng on 2016/2/1.
 *
 *      todo 当前是粗略测试版，需要改进的地方是：
 *      todo 因为是需要无缝接入其他的系统中，故可能是不需要写观察者，
 *      todo 将所有注册进来的object自动成为观察者
 */
public class RegisterCronNoExtends
{
    private static final String group = "subject";
    private static final String Scheduleder = "Scheduleder";
    //  此处Map是记录有多少种Scheduleder
    private static Map<String, SubjectNoExtends> mapSubject2 = new HashMap<String, SubjectNoExtends>();
    private static Map<Object, IObserver> mapObserver = new HashMap<Object, IObserver>();
    private static Map<String, Scheduler> mapScheduler= new HashMap<String, Scheduler>();

    private static SchedulerFactory schedulerfactory = new StdSchedulerFactory();

    public synchronized static Scheduler regWork(Object job, String method, String cron)
    {
        String cronName = "Scheduleder" + cron.trim().replaceAll(" ","").hashCode();
        return registerWork(cronName, job, method, cron);
    }

    private static Scheduler registerWork(String cronName, Object job, String method, String cron)
    {
        SubjectNoExtends subject = mapSubject2.get(cronName);
        MethodObserver observer = (MethodObserver) mapObserver.get(job);

        if (subject != null)
        {
            // 若[ 被监听者-subject ]不为空，则代表已存在对应的cronName的Trigger，
            // 只需将对应的[监听者]获得或者生成后注册到 subject 则可
            if (observer == null)
            {
                observer = new MethodObserver();
                mapObserver.put(job, observer);
                subject.register(observer, "hello world");
            }
            observer.register(job, method);

            return mapScheduler.get(cronName);
        }
        else
        {
            // 若[ 被监听者-subject ]不为空，则需要实例化对应的cronName的Trigger，
            // 且将对应的[监听者]获得或者生成后注册到 subject 则可
            subject = new SubjectNoExtends();
            mapSubject2.put(cronName, subject);

            if (observer == null)
            {
                observer = new MethodObserver();
                mapObserver.put(job, observer);
                subject.register(observer, "hello world");
            }
            observer.register(job, method);

            Scheduler scheduler = createScheduler(cronName, cron, subject);
            mapScheduler.put(cronName, scheduler);

            return scheduler;
        }
    }

    private static Scheduler createScheduler(String cronName, String cron, ISubject subject)
    {
        Scheduler scheduler;
        try
        {
            scheduler = schedulerfactory.getScheduler();

            JobDetail job = JobBuilder.newJob(SchedulerJob.class).withIdentity(cronName, group).build();

            job.getJobDataMap().put(group, subject);

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

            Trigger trigger = TriggerBuilder.newTrigger()
                                .withIdentity(cronName, group)
                                .withSchedule(cronScheduleBuilder)
                                .startNow().build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            mapScheduler.put(cronName, scheduler);
        }
        catch(Exception e){ e.printStackTrace(); }

        return mapScheduler.get(cronName);
    }

    public static Scheduler getScheduler(String cron)
    {
        String cronName = Scheduleder + cron.trim().replaceAll(" ","").hashCode();
        return mapScheduler.get(cronName);
    }

    public static TriggerKey getTriggerKey(String cron)
    {
        String cronName = Scheduleder + cron.trim().replaceAll(" ","").hashCode();
        return TriggerKey.triggerKey(cronName, group);
    }

    public static boolean unRegisterWork(Object job, String method) throws Exception
    {
        if (job == null && method == null)
        {
            throw new NullPointerException();
        }

        try
        {
            MethodObserver observer = (MethodObserver) mapObserver.get(job);
            observer.unRegister(job, method);
            // 此处没有判断被观察者已经没有观察者了，可能会造成内存泄露，但其实不影响
            return true;
        }
        catch (Exception e) { e.printStackTrace(); }

        return false;
    }
}

