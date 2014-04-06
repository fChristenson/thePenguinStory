package se.fidde.thepenguinstory.util;

import java.io.IOException;

import org.json.JSONException;

import se.fidde.thepenguinstory.domain.penguin.Penguin;
import se.fidde.thepenguinstory.util.factory.PenguinFactory;
import android.test.ActivityTestCase;

public class PenguinFactoryTest extends ActivityTestCase {

    private Penguin penguin;
    private String path;

    @Override
    protected void setUp() throws Exception {
        penguin = new Penguin();
        path = getInstrumentation().getContext().getFilesDir().getPath();
    }

    public void testSavePenguin() throws JSONException, IOException {
        boolean savePenguin = PenguinFactory.savePenguin(penguin, path);
        assertTrue("can save", savePenguin);
    }

    public void testLoadPenguin() {

        Penguin loadPenguin = PenguinFactory.loadPenguin(path);
        assertNotNull("can load penguin", loadPenguin);
    }
}
