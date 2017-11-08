package cn.com.fanyu.schedule;

import cn.com.fanyu.dao.FyTaskRepository;
import cn.com.fanyu.domain.FyTask;
import cn.com.fanyu.service.DiamondTaskService;
import cn.com.fanyu.service.TaskService;
import cn.com.fanyu.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class ScheduleJobs {
    public final static long SECOND = 1 * 1000;

    @Autowired
    private TaskService taskService;

    @Value("${task-status}")
    private boolean taskStatus;
    @Autowired
    private DiamondTaskService diamondTaskService;
    @Autowired
    private FyTaskRepository fyTaskRepository;



    /**
     * 固定等待时间 @Scheduled(fixedDelay = 时间间隔 )
     *
     * @throws InterruptedException
     */
//    @Scheduled(fixedDelay = SECOND * 4)
    public void fixedDelayJob() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("[FixedDelayJob Execute]" + DateUtil.format(new Date()));
    }

    /**
     * 固定间隔时间 @Scheduled(fixedRate = 时间间隔 )
     */
//    @Scheduled(fixedRate = SECOND * 4)
    public void fixedRateJob() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        System.out.println("[FixedRateJob Execute]" + DateUtil.format(new Date()));
    }

    /**
     * Corn表达式 @Scheduled(cron = Corn表达式)
     */
//    @Scheduled(cron = "0/4 * * * * ?")
    public void cronJob() {
        System.out.println("[CronJob Execute]" + DateUtil.format(new Date()));
    }

    @Scheduled(fixedDelay = SECOND * 2)
    private void TaskJob() throws InterruptedException {
        if(taskStatus){
            task();
        }
    }

    @Scheduled(fixedDelay = SECOND * 2)
    private void DiamondTaskJob() throws InterruptedException {
        if(false){
            diamondTaskService.diamondTaskJob();
        }
    }


    public void task() {
        //查找任务
        List<FyTask> list = fyTaskRepository.findByStatus(0);
        for (FyTask task : list) {
            try {
                taskService.calculate(task);
            }catch (Exception e){
                e.printStackTrace();
                task.setStatus(1);
                task.setMessage("error");
                fyTaskRepository.saveAndFlush(task);
            }
        }
    }
}
