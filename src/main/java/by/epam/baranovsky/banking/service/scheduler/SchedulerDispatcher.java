package by.epam.baranovsky.banking.service.scheduler;

import org.quartz.SchedulerException;

public  class SchedulerDispatcher {

    private static volatile SchedulerDispatcher instance = null;
    private SchedulerRunnable schedulerMaker = new SchedulerRunnable();
    private Thread schedulerThread = new Thread(schedulerMaker);

    private SchedulerDispatcher() {}

    public static SchedulerDispatcher getInstance() {
        if(instance==null){
            synchronized (SchedulerDispatcher.class){
                if(instance==null){
                    instance = new SchedulerDispatcher();
                }
            }
        }
        return instance;
    }

    public void init() throws Exception {
        if(!schedulerThread.isAlive()){
            schedulerThread.setName("Quartz scheduler thread");
            schedulerThread.start();
        }
    }

    public void end() throws SchedulerException {
        if(schedulerMaker != null && schedulerThread.isAlive()){
            while(schedulerMaker.getScheduler() == null){}
            schedulerMaker.end();
        }
    }

}
