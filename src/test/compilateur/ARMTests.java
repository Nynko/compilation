package compilateur;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;


import static org.junit.jupiter.api.Assertions.assertEquals;


import compilateur.utils.Os;


public class ARMTests {

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStreamInJava8() {
        if(! System.getProperty("os.arch").contains("aarch64")){
            // Si on n'est pas sur un ARM64, on ne test pas
            return Stream.empty();
        }

        List<String> testFiles = new ArrayList<String>();
        File[] files = new File("./examples/").listFiles();

        Pattern pattern = Pattern.compile("ARM\\_.*\\.c$");
        for (File file : files) {
            if (file.isFile()) {
                Matcher m = pattern.matcher(file.getName());
                if(m.matches()) {
                    testFiles.add(file.getName());
                }
            }
        }
            
        return testFiles.stream()
        .map((file) -> {
            String[] parts = file.split("\\.");
            System.out.println(parts.length);
            return DynamicTest.dynamicTest(parts[0].toString() + parts[1].toString() + "Test()", 
                () -> {
                    assertEquals(true, testFile("./examples/" + file));
                });
        });
    }

    public String runProgram(String programName) throws IOException {
        String[] cmd = {programName};
        ProcessBuilder pr = new ProcessBuilder(cmd);

        pr.redirectErrorStream(true);

        Process p = pr.start();

        try (Scanner s = new Scanner(p.getInputStream(),StandardCharsets.UTF_8.name()).useDelimiter("\\A")) {
            String output = s.hasNext() ? s.next() : "";
            s.close();

            return output;
        }
    }

    public boolean testFile(String testFile) throws IOException {     
        //Creation d'un fichier équivalent en C
        String cFile = testFile.replace("ARM_", "C/C_");
        //Replace all instances of print with printf in the file 
        String content = new String(java.nio.file.Files.readAllBytes(new File(testFile).toPath()));
        // add include <stdio.h> at the start of the file
        content = "#include <stdio.h>\n" + content;
        content = "#include <stdlib.h>\n" + content;
        content = content.replace("print(", "printf(\"%d\\n\",");

        // Write the file
        java.nio.file.Files.write(new File(cFile).toPath(), content.getBytes());

        // Get name 
        String name = testFile.split("/")[2].split("\\.")[0].split("_")[1];

        // Compilation du fichier C avec Runtime.exec()
        String[] clangCompilCmd = {"clang", "-o", "./bin/ARMc/" + name, cFile};
        try{
            Runtime.getRuntime().exec(clangCompilCmd).waitFor();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return false;
        }

        // Compilation du fichier ARM avec notre makefile
        String os = Os.getOS() == Os.systeme.MACOS ? "macos" : "linux";

        String[] compilCmd = {"make", os + "Named","--","NAME=" + name,"ARGS="+ testFile};
        try{
            Runtime.getRuntime().exec(compilCmd).waitFor();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return false;
        }


        //Execution des fichiers c et ARM
        String output1 = runProgram("./bin/ARMc/" + name);
        String output2 = runProgram("./bin/ARM/" + name);
        

        // Vérification du fait que les sorties du stdout soient les mêmes
        System.out.println("testFile : " + testFile);
        System.out.println("str1 : " + output1);
        System.out.println("str2 : " + output2);


        return output1.equals(output2);
    }
}
