import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TestDriver {
    private static void usage() {
        System.out.println("usage: java TestDriver <path to project>");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
            return;
        }

        final File me = new File(TestDriver.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File testCode = Paths.get(me.getAbsolutePath(), "test_code").toFile();

        if (!testCode.exists() || !testCode.isDirectory()) {
            System.out.println("Can't find test_code. This folder should be in the same path as this java file.");
            return;
        }

        File project = new File(args[0]);
        if (!project.isFile()) {
            System.out.println(args[0] + " is not a file.");
            return;
        }
        if (!args[0].endsWith(".zip")) {
            System.out.println(args[0] + " is not a zip.");
            return;
        }

        File working_dir = Paths.get(me.getAbsolutePath(), "360C_LAB1_WORKING_DIR__").toFile();
        if (working_dir.exists()) {
            System.out.println(working_dir+" already exists! Please delete or move it and try again.");
            return;
        }
        if (!working_dir.mkdir()) {
            System.out.println("Could not mkdir "+working_dir);
            return;
        }

        final String[] exclude_arr = {
                "Driver.java", "AbstractProgram1.java", "Matching.java", "Permutation.java"
        };
        for (String s : exclude_arr) { exclude.add(s); }

        ArrayList<File> found = null;
        try {
            found = unzip(args[0], working_dir);
        } catch (IOException e) {
            System.out.println("Couldn't unzip file");
            return;
        }

        System.out.println("Found and extracted files:");
        for (File f : found) {
            System.out.println(f.getAbsolutePath());
        }

        System.out.println("\nCopying given files to "+working_dir.getAbsolutePath());
        for (File f : testCode.listFiles()) {
            if (f.isDirectory()) { continue; }

            System.out.println(f.getAbsolutePath());
            Path new_path = Paths.get(working_dir.getAbsolutePath(), f.getName());
            try {
                Files.copy(f.toPath(), new_path, StandardCopyOption.COPY_ATTRIBUTES);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done");

        System.out.println("\nCompiling java files...");
        compileInDir(working_dir);
        System.out.println("Done");

        System.out.println("\nRunning '-b small_inputs/4.in'");
        System.out.println("--------------------------------");
        String test_case1 = Paths.get(testCode.getAbsolutePath(), "small_inputs", "4.in").toString();
        runCode(working_dir, true, test_case1);
        System.out.println("--------------------------------");
        System.out.println("Done");

        System.out.println("\nRunning '-g large_inputs/160.in'");
        System.out.println("--------------------------------");
        String test_case2 = Paths.get(testCode.getAbsolutePath(), "large_inputs", "160.in").toString();
        runCode(working_dir, false, test_case2);
        System.out.println("--------------------------------");
        System.out.println("Done");
    }

    private static ArrayList<File> unzip(String zip_name, File unzip_dir) throws IOException {
        ArrayList<File> found = new ArrayList<File>();

        boolean pdfFound = false;
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zip_name));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            String real_filename = Paths.get(fileName).getFileName().toString();
            File newFile = Paths.get(unzip_dir.getAbsolutePath(), real_filename).toFile();
            if (zipEntry.isDirectory()) { /*newFile.mkdir();*/ }
            else {
                if (fileName.endsWith(".pdf")) { pdfFound = true; }
                else if (!real_filename.startsWith(".") && fileName.endsWith(".java") && !exclude.contains(real_filename)) {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    found.add(newFile);
                }
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        if (!pdfFound) {
            System.out.println("\nWARNING: No pdf found, did you forget to submit your report?");
            System.out.println("If this is fine press enter to continue.");
            System.in.read();
        }

        return found;
    }

    private static ArrayList<String> exclude = new ArrayList<String>();

    private static void compileInDir(File working_dir) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        ArrayList<File> java_files = new ArrayList<File>();
        for (File f: working_dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(".java")) {
                java_files.add(f);
            }
        }

        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjectsFromFiles(java_files);
        compiler.getTask(null, fileManager, null, null, null, compilationUnits1)
                .call();
    }

    private static void runCode(File working_dir, boolean bruteforce, String test_case) {
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[] { working_dir.toURI().toURL() });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Class<?> cls = null;
        try {
            cls = Class.forName("Lab1Tester", true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object instance = null;
        try {
            instance = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Method m = null;
        try {
            m = cls.getDeclaredMethod("runTest", Boolean.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            m.invoke(null, bruteforce, test_case);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
