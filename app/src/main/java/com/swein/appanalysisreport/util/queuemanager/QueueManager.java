package com.swein.appanalysisreport.util.queuemanager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QueueManager {

    private static QueueManager instance = new QueueManager();
    public static QueueManager getInstance() {
        return instance;
    }

    private LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
    private ThreadPoolExecutor threadPoolExecutor;

    private QueueManager() {

        threadPoolExecutor = new ThreadPoolExecutor(1, 10, 15, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                addTask(runnable);
            }
        });

        Runnable processThread = new Runnable() {

            Runnable runnable;

            @Override
            public void run() {

                while (true) {

                    try {
                        runnable = queue.take();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (runnable != null) {
                        threadPoolExecutor.execute(runnable);
                    }
                }
            }
        };

        threadPoolExecutor.execute(processThread);
    }

    public void addTask(Runnable runnable) {

        if(runnable != null) {
            try {
                queue.put(runnable);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void finalize() throws Throwable {
        threadPoolExecutor.shutdown();
        super.finalize();
    }

}
