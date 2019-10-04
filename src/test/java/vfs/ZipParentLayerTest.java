package vfs;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.cache.OnCallRefreshFileObject;
import org.junit.jupiter.api.Test;

/**
 * The parent layer is set to null after below call order.
 *
 * Below assertions hold in VFS in 2.1, but not for later versions.
 */
class ZipParentLayerTest {

  @Test
  void zipParentLayerGone() throws Exception {
    final FileObject zippedFile = new OnCallRefreshFileObject(fileInZip());
    assertNotNull(zippedFile.getFileSystem().getParentLayer());

    zippedFile.exists();
    zippedFile.getContent();

    assertNotNull(zippedFile.getFileSystem().getParentLayer());
  }

  private FileObject fileInZip() throws FileSystemException {
    final String zip = getClass().getClassLoader().getResource("test.txt.zip").toExternalForm();
    final String containedFile = zip.replaceFirst("^file", "zip") + "!/test.txt";
    return VFS.getManager().resolveFile(containedFile);
  }
}
