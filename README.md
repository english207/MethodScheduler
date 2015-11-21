# MethodScheduler
基于Quartz的方法调度器

/**
 *   继承 MethodScheduler 后可以使用 registerWork 对一个无参方法设置调度的时间（用cron表达式，基于Quartz）
 */
public class Test extends MethodScheduler
{
    public void startJob()
    {
        this.registerWork("say", "0/2 * * * * ? *");
    }

    public void say()
    {
        System.out.println("say hello");
    }


    public static void main(String[] args)
    {
        new Test().startJob();
    }

}
