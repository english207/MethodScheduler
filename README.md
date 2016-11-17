# MethodScheduler

基于Quartz的方法调度器

继承 MethodScheduler 后可以使用 registerWork

对一个无参方法设置调度的时间（用cron表达式，基于Quartz）

样例（Example）：
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
