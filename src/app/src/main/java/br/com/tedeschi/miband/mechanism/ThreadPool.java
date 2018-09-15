package br.com.tedeschi.miband.mechanism;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ThreadPool sInstance;
    private static ExecutorService sExecutorService;

    // private constructor.
    private ThreadPool() {
        // Prevent form the reflection api.
        if (sInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public synchronized static ThreadPool getInstance() {
        if (sInstance == null) {
            //if there is no instance available... create new one
            sInstance = new ThreadPool();
        }

        if (sExecutorService == null) {
            sExecutorService = Executors.newFixedThreadPool(1);
        }

        return sInstance;
    }

    public ExecutorService getExecutorService() {
        return sExecutorService;
    }
}
