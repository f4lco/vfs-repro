package vfs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import java.io.File;
import java.io.IOException;

/**
 * Formatted code (and updated imports) from <a href="https://issues.apache.org/jira/browse/VFS-299?focusedCommentId=12832610&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-12832610">VFS-299
 * Comment</a>.
 *
 * <p>
 * I assume there are two possible solutions:
 *   <ol>
 *     <li>
 *       Stopping the file monitor removes filesystem listeners.
 *       This would remove the ability to pause listening for file events and to resume with start().
 *     </li>
 *     <li>
 *       To completely decommission the file monitor, it is the duty of the client code to remove all monitored files.
 *       For this to work, removeFile should remove the filesystem listener, which it currently does not (see {@link FileMonitorAddRemoveTest}).
 *     </li>
 *   </ol>
 * </p>
 */
public class MinimalFileMonitorBug {

  private static final String path = "dummy.txt";

  public static void main(String[] args) throws IOException, InterruptedException {
    MyFileListener listener1 = new MyFileListener();
    DefaultFileMonitor fileMonitor = new DefaultFileMonitor(listener1);
    fileMonitor.addFile(getFileObject());
    fileMonitor.start(); // Normal operation ...
    fileMonitor.stop(); // Deliberately writing on file without getting notified.
    MyFileListener listener2 = new MyFileListener();
    fileMonitor = new DefaultFileMonitor(listener2);
    fileMonitor.addFile(getFileObject());
    fileMonitor.start();
    Thread.sleep(1500);
    FileUtils.writeStringToFile(getFile(), "new content");
    Thread.sleep(1500);
  }

  private static FileObject getFileObject() throws IOException {
    return VFS.getManager().resolveFile(getFile().getAbsolutePath());
  }

  private static File getFile() throws IOException {
    File file = new File(path);
    if (!file.exists()) {
      file.createNewFile();
    }

    return file;
  }

  static class MyFileListener implements FileListener {

    @Override
    public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {
    }

    @Override
    public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception {
    }

    @Override
    public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception {
      System.out.println(String
          .format("File [%s] changed event from [%s]", fileChangeEvent.getFile().getName(), this));
    }

  }
}