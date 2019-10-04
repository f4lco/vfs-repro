package vfs;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;

final class Utils {

  private static final Random RANDOM = new Random();

  private Utils() {
  }

  static FileObject getFile(final String fileName) throws FileSystemException {
    final URL url = Utils.class.getClassLoader().getResource(fileName);
    requireNonNull(url, () -> fileName + " not found");
    return VFS.getManager().resolveFile(url);
  }

  static void changeFile(final FileObject file) throws IOException {
    try (var out = file.getContent().getOutputStream()) {
      out.write(RANDOM.nextInt());
    }
  }
}
