package sfmf4j.nio2;

import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import sfmf4j.api.FileMonitorServiceFactory;

public class Activator implements BundleActivator {
    
    private ExecutorService executorService = null;
    private WatchService watchService = null;
    private ServiceRegistration registration = null;

    @Override
    public void start(BundleContext context) throws Exception {
        watchService = FileSystems.getDefault().newWatchService();
        executorService = Executors.newCachedThreadPool(new ThreadFactory(){

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"SFMF4JWatchService");
            }
        });
        WatchServiceFileMonitorServiceFactory factory = new WatchServiceFileMonitorServiceFactory();
        factory.setExecutorService(executorService);
        factory.setWatchService(watchService);
        registration = context.registerService(FileMonitorServiceFactory.class.getName(), factory, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (registration!=null) {
            registration.unregister();
        }
        if (executorService!=null) {
            executorService.shutdownNow();
        }
        if (watchService != null) {
            watchService.close();
        }        
    }

    
}
