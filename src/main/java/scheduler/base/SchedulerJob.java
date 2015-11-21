package scheduler.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import scheduler.observer.Subject;

/**
 * Created by huangzhenfeng on 2015/11/21 0021.
 *
 */
public class SchedulerJob  implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        Subject subject = (Subject) jobExecutionContext.getJobDetail().getJobDataMap().get("subject");
        subject.notifyThem();
    }
}