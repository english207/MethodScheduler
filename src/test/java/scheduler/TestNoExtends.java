package scheduler;

import hzf.scheduler.noextends.work.RegisterCronNoExtends;

/**
 * Created by huangzhenfeng on 2016/3/24.
 *
 */
public class TestNoExtends
{

    public void say()
    {
        System.out.println("say hello");
    }

    public static void main(String[] args)
    {
        RegisterCronNoExtends.regWork(new TestNoExtends(), "say", "0/2 * * * * ? *");
    }
}
