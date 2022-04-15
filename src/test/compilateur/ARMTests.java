package compilateur;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


import compilateur.grammar.*;
import compilateur.utils.Os;


public class ARMTests {

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStreamInJava8() {

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

    public boolean testFile(String testFile) throws IOException {
        if(! System.getProperty("os.arch").contains("aarch64")){
            // Si on n'est pas sur un ARM64, on ne test pas
            return true;
        }
        //chargement du fichier et construction du parser
        CharStream input = CharStreams.fromFileName(testFile);
        
        //Creation d'un fichier équivalent en C
        String cFile = testFile.replace("ARM_", "C/C_");
        //Replace all instances of print with printf in the file 
        String content = new String(java.nio.file.Files.readAllBytes(new File(testFile).toPath()));
        // add include <stdio.h> at the start of the file
        content = "#include <stdio.h>\n" + content;
        content = content.replace("print(", "printf(\"%d\\n\",");

        //Write the file
        java.nio.file.Files.write(new File(cFile).toPath(), content.getBytes());

        // Get name 
        String name = testFile.split("/")[2].split("\\.")[0].split("_")[1];

        // Compilation du fichier C avec Runtime.exec()
        String[] cmd0 = {"clang", "-o", "./bin/ARMc/" + name, cFile};
        try{
            Process p0 = Runtime.getRuntime().exec(cmd0);
            p0.waitFor();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return false;
        }

        // Compilation du fichier ARM avec notre makefile
        String os= "";
        if(Os.getOS() == Os.systeme.MACOS){
            os = "macos";
        } else {
            os = "linux";
        } 
        String[] cmd02 = {"make", os + "Named","--","NAME=" + name,"ARGS="+ testFile};
        try{
            Process p02 = Runtime.getRuntime().exec(cmd02);
            p02.waitFor();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return false;
        }


        //Execution du fichier c et ARM
        String[] cmd = {"./bin/ARMc/" + name};
        ProcessBuilder pr = new ProcessBuilder(cmd);

        String[] cmd2 = {"./bin/ARM/" + name};
        ProcessBuilder pr2 = new ProcessBuilder(cmd2);

        pr.redirectErrorStream(true);
        pr2.redirectErrorStream(true);

        Process p = pr.start();
        Process p2 = pr2.start();

        // Vérification que les sorties du stdout sont les mêmes
        Boolean bool = true;
        Scanner s = new Scanner(p.getInputStream(),StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        String str = s.hasNext() ? s.next() : "";
        s.close();

        Scanner s2 = new Scanner(p2.getInputStream(),StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        String str2 = s2.hasNext() ? s2.next() : "";
        s2.close();

        System.out.println("testFile : " + testFile);
        System.out.println("str1 : " + str);
        System.out.println("str2 : " + str2);

        if(!str.equals(str2)) {
            bool = false;
        }
        return bool ;
    }
}
