package scheduler;


import hzf.scheduler.simple.work.MethodScheduler;

/**
 * Created by huangzhenfeng on 2015/11/20 0020.
 *
 */
public class TestSimple extends MethodScheduler
{
    int i = 0;
    public void startJob()
    {
        this.registerWork("say", "0/2 * * * * ? *")
            .registerWork("say2", "0/2 * * * * ? *");
    }

    public void say()
    {
        System.out.println("say hello");
        i ++ ;
        if (i > 5)
        {
            this.unRegisterWork("say");
            System.out.println("结束 say");
        }
    }

    public void say2()
    {
        System.out.println("say2 hello");
        i ++;

        if (i > 10)
        {
            this.registerWork("say", "0/2 * * * * ? *");
            System.out.println("开始 say");
            i = 0;
        }
    }

    public static void main(String[] args)
    {
        new TestSimple().startJob();
    }

}
