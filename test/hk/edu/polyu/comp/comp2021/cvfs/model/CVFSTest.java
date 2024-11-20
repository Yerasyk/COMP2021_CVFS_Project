package hk.edu.polyu.comp.comp2021.cvfs.model;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class CVFSTest {

    @Test
    public void testCreateDiskAndValidateSpace() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);

        assertEquals("Remaining space should decrease after document creation", 960, cvfs.getDisk().getRemainedSize());
    }

    @Test
    public void testCreateAndDeleteDocument() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);

        cvfs.createDocument("doc", "txt", "This is a test document.");
        assertEquals("Remaining space should decrease after document creation", (1000-(24*2+40)-40), cvfs.getDisk().getRemainedSize());
        assertNotNull("File wasn't found", cvfs.getDisk().getCurrentDirectory().findFile("doc"));

        cvfs.delete("doc");
        assertEquals("Remaining space should increase after document deletion", 960, cvfs.getDisk().getRemainedSize());
        assertNull("File found", cvfs.getDisk().getCurrentDirectory().findFile("doc"));
    }

    @Test
    public void testChangeDirectory() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);

        cvfs.createDirectory("subdir");
        cvfs.changeDir("subdir");
        assertEquals("Working directory should be 'subdir'", "subdir",cvfs.getDisk().getCurrentDirectory().getName());

        cvfs.changeDir("..");
        assertEquals("Working directory should return to root after navigating up", "root",cvfs.getDisk().getCurrentDirectory().getName());
    }

    @Test
    public void testUndoRedoOperations() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);

        cvfs.createDirectory("subdir");
        cvfs.undo();
        assertNull("Undo should remove 'subdir' from the working directory contents", cvfs.getDisk().getCurrentDirectory().findFile("subdir"));

        cvfs.redo();
        assertNotNull("Redo should restore 'subdir' in the working directory contents", cvfs.getDisk().getCurrentDirectory().findFile("subdir"));
    }

    @Test
    public void testRecursiveListingWithCriterion() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);

        cvfs.createDirectory("dir1");
        cvfs.changeDir("dir1");
        cvfs.createDocument("doc1", "txt", "Content of doc1");
        cvfs.createDirectory("dir2");
        cvfs.changeDir("dir2");
        cvfs.createDocument("doc2", "txt", "Content of doc2");
        cvfs.changeDir("..");
        cvfs.changeDir("..");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        cvfs.recursiveList("IsDocument");
        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue("Output should contain doc1.txt", output.contains("doc1.txt"));
        assertTrue("Output should contain doc2.txt", output.contains("doc2.txt"));
    }

    @Test(expected = StateChangeCommandFailed.class)
    public void testCreateOversizedDocument() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(50);

        cvfs.createDocument("largeDoc", "txt", "This content is too large for the disk.");
    }

    @Test
    public void testSaveAndLoad(){
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);

        cvfs.createDirectory("dir1");
        cvfs.changeDir("dir1");
        cvfs.createDocument("doc1", "txt", "This is a test document.");
        cvfs.changeDir("..");
        cvfs.createSimpleCri("aa", "name", "contains", "\"doc\"");

        String filePath = "testDisk.datc";
        cvfs.saveDisk(filePath, true);

        CVFS cvfsLoaded = new CVFS();
        cvfsLoaded.loadDisk(filePath);

        assertEquals("Loaded disk should have the same remaining space as the original",
                cvfs.getDisk().getRemainedSize(), cvfsLoaded.getDisk().getRemainedSize());

        assertNotNull("Loaded disk should contain 'dir1'",
                cvfsLoaded.getDisk().getCurrentDirectory().findFile("dir1"));

        //It doesn't check values of criteriaMaps.
        assertTrue("They should have same criteria", cvfs.getCriterionMap().keySet().equals(cvfsLoaded.getCriterionMap().keySet()));
    }

    @Test
    public void testSimpleCri() {
        SimpleCriterion criterion = new SimpleCriterion("ab", "name", "contains", "\"test\"");
        SimpleCriterion criterion2 = new SimpleCriterion("cd", "type", "equals", "\"txt\"");

        Document doc = new Document("testFile", "txt", "content");
        assertTrue(criterion.matches(doc));
        assertTrue(criterion2.matches(doc));

        Document doc2 = new Document("otherFile2", "txt", "content");
        assertFalse(criterion.matches(doc2));
        assertTrue(criterion2.matches(doc2));

        Document doc3 = new Document("testFile3", "java", "content");
        assertTrue(criterion.matches(doc3));
        assertFalse(criterion2.matches(doc3));
    }

    @Test
    public void testBinaryCri() {
        SimpleCriterion criterion1 = new SimpleCriterion("ab", "name", "contains", "\"test\"");
        SimpleCriterion criterion2 = new SimpleCriterion("cd", "type", "equals", "\"txt\"");
        BinaryCriterion binaryCriterion = new BinaryCriterion("ef", criterion1, "&&", criterion2);

        Document doc = new Document("testFile", "txt", "content");
        assertTrue(binaryCriterion.matches(doc));

        Document doc2 = new Document("otherFile", "txt", "content");
        assertFalse(binaryCriterion.matches(doc2));
    }

    @Test
    public void testNegationCri(){
        SimpleCriterion criterion = new SimpleCriterion("ab", "name", "contains", "\"test\"");
        NegationCriterion negationCriterion = new NegationCriterion( "nb", criterion);
        Document doc = new Document("testFile", "txt", "content");
        assertTrue(criterion.matches(doc));
        assertFalse(negationCriterion.matches(doc));

        Document doc2 = new Document("otherFile2", "txt", "content");
        assertFalse(criterion.matches(doc2));
        assertTrue(negationCriterion.matches(doc2));
    }
}
