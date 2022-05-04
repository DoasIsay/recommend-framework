package recommend.framework.util;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import recommend.framework.Factory;
import recommend.framework.functor.FunctorFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FileMonitor extends FileAlterationListenerAdaptor {
    @Override
    public void onFileCreate(File file) {
        fileChangeCallBack.accept(file);
        System.out.println("onFileCreate:" + file.getName());
    }

    @Override
    public void onFileChange(File file) {
        fileChangeCallBack.accept(file);
        System.out.println("onFileChange : " + file.getName());
    }

    Consumer<File> fileChangeCallBack;

    public FileMonitor(String path, Predicate<String> filter, Consumer<File> fileChangeCallBack) {
        this.fileChangeCallBack = fileChangeCallBack;
        try {
            FileAlterationObserver observer = new FileAlterationObserver(path, new FileFilter() {
                @Override
                public boolean accept(File path) {
                    return filter.test(path.getName());
                }
            });

            observer.addListener(this);
            FileAlterationMonitor fileMonitor = new FileAlterationMonitor(3000,
                    new FileAlterationObserver[] { observer });
            fileMonitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}