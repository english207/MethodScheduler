package hzf.scheduler.base.work;

import hzf.scheduler.base.observer.ISubject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by huangzhenfeng on 2015/11/21 0021.
 *
 */
public class SchedulerJob  implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        ISubject subject = (ISubject) jobExecutionContext.getJobDetail().getJobDataMap().get("subject");
        subject.notifyThem();
    }
}