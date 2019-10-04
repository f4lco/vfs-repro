package vfs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;

class CountingListener implements FileListener {

  private final Map<Class<? extends FileChangeEvent>, Integer> received = new ConcurrentHashMap<>();

  private void count(final FileChangeEvent event) {
    received.compute(event.getClass(), (key, count) -> count == null ? 1 : ++count);
  }

  int count(final Class<? extends FileChangeEvent> eventClass) {
    return received.getOrDefault(eventClass, 0);
  }

  @Override
  public void fileCreated(final FileChangeEvent event) {
    count(event);
  }

  @Override
  public void fileDeleted(final FileChangeEvent event) {
    count(event);
  }

  @Override
  public void fileChanged(final FileChangeEvent event) {
    count(event);
  }
}
