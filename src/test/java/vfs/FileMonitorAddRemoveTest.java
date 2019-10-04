package vfs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static vfs.Utils.changeFile;
import static vfs.Utils.getFile;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.events.ChangedEvent;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.junit.jupiter.api.Test;

/**
 * If {@link DefaultFileMonitor#addFile(FileObject)} adds a listener to the filesystem, {@link
 * DefaultFileMonitor#removeFile(FileObject)} supposedly should remove it.
 *
 * <p>This is a variant of VFS-299, which states that filesystem listeners are not removed during
 * stop(). In fact, filesystem listeners are never removed, also not during removeFile().</p>
 */
class FileMonitorAddRemoveTest {

  @Test
  void addingAndRemovingFileDuplicatesChangedEvents() throws Exception {
    final var listener = new CountingListener();
    final DefaultFileMonitor monitor = new DefaultFileMonitor(listener);
    monitor.start();

    final FileObject file = getFile("test.txt");
    monitor.addFile(file);
    monitor.removeFile(file);
    monitor.addFile(file);

    changeFile(file);

    Thread.sleep(5000);
    assertEquals(1, listener.count(ChangedEvent.class));
  }
}
