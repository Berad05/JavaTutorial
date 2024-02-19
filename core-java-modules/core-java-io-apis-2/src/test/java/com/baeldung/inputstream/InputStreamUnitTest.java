package com.baeldung.inputstream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
public class InputStreamUnitTest {
    @Test //givenAStringWrittenToAFile_whenReadByX_thenY
    public void givenAStringWrittenToFile_whenReadWithFileInputStream_thenItShouldExitsInTheInputStream(@TempDir Path tempDir) throws IOException {
        Path sampleOut = tempDir.resolve("sample-out.txt");
        List<String> lines = Arrays.asList("Hello. This is just a test. Good bye.");
        Files.write(sampleOut, lines);
        File sampleOutFile = sampleOut.toFile();
        try (InputStream inputStream = new FileInputStream(sampleOutFile)) {
            assertThat(readString(inputStream)).contains(lines.get(0));
        }
    }

    @Test
    public void givenAStringWrittenToFile_whenReadWithFileInputStreamWithStringConstructor_thenItShouldExitsInTheInputStream(@TempDir Path tempDir) throws IOException {
        Path sampleOut = tempDir.resolve("sample-out.txt");
        String expectedText = "Hello. Hi.";
        List<String> lines = Arrays.asList(expectedText);
        Files.write(sampleOut, lines);
        String fileAbsolutePath = sampleOut.toFile()
            .getAbsolutePath();
        try (FileInputStream fis = new FileInputStream(fileAbsolutePath)) {
            int content;
            int availableBytes = fis.available();
            Assert.assertTrue(availableBytes > 0);
            StringBuilder actualReadText = new StringBuilder();
            while ((content = fis.read()) != -1) {
                actualReadText.append((char) content);
            }
            assertThat(actualReadText.toString()).contains(expectedText);
        }
    }

    @Test
    public void givenAByteArray_whenReadWithByteArrayInputStream_thenTheStringValueOfTheByteArrayShouldMatchTheExpectedString() throws IOException {
        byte[] byteArray = { 104, 101, 108, 108, 111 };
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray)) {
            assertThat(readString(bais)).isEqualTo("hello");
        }
    }
    @Test
    public void givenKeyValuePairsWrittenInAFile_whenPassedInOutputStreams_thenThePairsShouldExistsInObjectInputStreams(
          @TempDir Path tempDir) throws IOException, ClassNotFoundException {
        Path sampleOut = tempDir.resolve("sample-out.txt");
        File fileText = sampleOut.toFile();
        //Serialize a Hashmap then write it into a file.
        Map<String, String> kv = new HashMap<>();
        try (ObjectOutputStream objectOutStream = new ObjectOutputStream(new FileOutputStream(fileText))) {
                kv.put("baseURL", "baeldung.com");
                kv.put("apiKey", "this_is_a_test_key");
                objectOutStream.writeObject(kv);
        }
        //Deserialize the contents of a file then transform it back into a Hashmap
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileText))) {
                HashMap<String, String> inputKv = (HashMap<String, String>) input.readObject();
                assertThat(kv.get("baseURL")).isEqualTo( inputKv.get("baseURL"));
                assertThat(kv.get("apiKey")).isEqualTo( inputKv.get("apiKey"));
        }
    }

    private static String readString(InputStream inputStream) throws IOException {
        int c;
        StringBuilder sb = new StringBuilder();
        while ((c = inputStream.read()) != -1) {
            sb.append((char) c);
        }
        return sb.toString();
    }

}
