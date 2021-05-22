package co.stag.proj.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    static String downloadLocation = System.getProperty("user.dir") + File.separator + "downloads" + File.separator;

    public static void main(String[] args) {
        // System.out.println("VD: "+getVersionDifference(fetchFloatFromAgivenString("Panel BRIDGE_NPD_v0.5_20160617 version: 0.5_20160617 (Version 0.216"),fetchFloatFromAgivenString("Panel BRIDGE_NPD_v0.5_20160617 version: 0.5_20160617 (Version 0.217")));
    }

    public static String fetchNumberFromAgivenString(String InputString) {
        String numFound = "";
        //System.out.println("input string to be searched for is = "+ InputString);
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(InputString);
        if (m.find()) {
            numFound = m.group(0);
        }
        //System.out.println("Fetched digit from the input string = "+ numFound);
        return numFound;
    }

    public static String fetchFloatFromAgivenString(String InputString) {
        if (InputString != null && InputString.indexOf("(") != -1) {
            InputString = InputString.substring(InputString.indexOf("("), InputString.length());
        }
        String numFound = "";
        // System.out.println("input string to be searched for is = "+ InputString);
        //Pattern1  - 1.1
        Pattern p = Pattern.compile("[0-9]+.+[0-9]");
        Matcher m = p.matcher(InputString);
        if (m.find()) {
            numFound = m.group(0);
        }
        if (!numFound.isEmpty()) {
            return numFound;
        }
        //Pattern2 - 1.11
        Pattern p2 = Pattern.compile("[0-9]+.+[0-9][0-9]");
        Matcher m2 = p2.matcher(InputString);
        if (m2.find()) {
            numFound = m2.group(0);
        }
        if (!numFound.isEmpty()) {
            return numFound;
        }
        //Pattern3 - 10.1
        Pattern p3 = Pattern.compile("[0-9][0-9]+.+[0-9]");
        Matcher m3 = p3.matcher(InputString);
        if (m3.find()) {
            numFound = m3.group(0);
        }
        if (!numFound.isEmpty()) {
            return numFound;
        }
        //Pattern4 - 10.11
        Pattern p4 = Pattern.compile("[0-9][0-9]+.+[0-9][0-9]");
        Matcher m4 = p4.matcher(InputString);
        if (m4.find()) {
            numFound = m4.group(0);
        }
        if (!numFound.isEmpty()) {
            return numFound;
        }
        return numFound;
    }

    public static int getVersionDifference(String version1, String version2, float increment) {
        try {
            BigDecimal numn1 = new BigDecimal(version1);
            BigDecimal num2 = new BigDecimal(version2);
            //Debugger.println("V1: "+String.valueOf(version1)+" and V2: "+version2);

            BigDecimal diff = num2.subtract(numn1).abs();
            //Debugger.println("Version Diff: "+diff);
            String fValue = diff.toString();
            if (increment == 0) {
                Debugger.println("Expected NO Increment.");
                if (fValue.equals("0") || fValue.equals("0.00") || fValue.equals("0.0")) {
                    return 1;
                } else {
                    Debugger.println("Expected no increment in panel version. Before:" + version1 + ",After:" + version2 + ",Diff:" + fValue);
                    return 0;
                }
            }
            //Debugger.println("DIFF Float Value: "+fValue);
            if (fValue.equals("0.1") || fValue.equals("0.01") || fValue.equals("0.001") || fValue.equals("0.0001") || fValue.equals("0.80")) {
                // Debugger.println("TRUE...........");//0.8 --->0.10-0.9 - version change from 0.9 to 0.10
                return 1;
            }
            return 0;
        } catch (Exception exp) {
            Debugger.println("Version 1: " + version1 + ",Version2: " + version2);
            return 0;
        }
    }

    public static long getTimeDifferenceInSeconds(String previousTime, String currentTime) {
        try {
            // Debugger.println("previousTime: "+previousTime);
            // Debugger.println("currentTime  : "+currentTime);
            Date prev_date = getDateFromString(previousTime);
            Date current_date = getDateFromString(currentTime);
            if (prev_date == null) {
                Debugger.println("PreviousTime in unexpected format.");
                return -1;
            }
            if (current_date == null) {
                Debugger.println("CurrentTime in unexpected format.");
                return -1;
            }
            long diff_time = prev_date.getTime() - current_date.getTime();
            //Debugger.println("Diff in Seconds: "+diff_time);
            return diff_time;
        } catch (Exception exp) {
            return -1;
        }
    }

    public static Date getDateFromString(String date_string) {
        String[] date_formats = {"d MMM yyyy, hh:mm",
                "dd MMM yyyy"};
        SimpleDateFormat sdf = null;
        Date date = null;
        for (int i = 0; i < date_formats.length; i++) {
            sdf = new SimpleDateFormat(date_formats[i]);
            try {
                date = sdf.parse(date_string);
            } catch (Exception exp) {

            }
            if (date != null) {
                return date;
            }
        }//for
        return date;
    }

    public static String verifyFilePresenceInDefaultLocation(String fileName) {
        File location = new File(downloadLocation);
        File[] files = location.listFiles();
        boolean isFilePresent = false;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().startsWith(fileName)) {
                Debugger.println("File: " + files[i].getName());
                if (files[i].getName().endsWith(".tsv")) {
                    isFilePresent = true;
                    break;
                }
            } else {
                continue;
            }
        }
        if (isFilePresent) {
            return "Success";
        }
        //Wait for maximum 5 minutes with an interval of 30 seconds to check the file presence
        Debugger.println("File name starts with " + fileName + " not present in downloads location, waiting for 5 minutes with an interval of 60 seconds check." + isFilePresent);
        int timeWait = 0;
        while (!isFilePresent) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith(fileName)) {
                    Debugger.println("File: " + files[i].getName());
                    if (files[i].getName().endsWith(".tsv")) {
                        isFilePresent = true;
                        break;
                    }
                } else {
                    continue;
                }
            }
            try {
                if (timeWait == 10) {
                    Debugger.println("Waited for 5 minutes, File name starts with " + fileName + " could not found in downloads location, Failing test and exiting.");
                    isFilePresent = true;
                    timeWait++;
                } else {
                    Debugger.println("waiting for 30 seconds to check again..." + new Date());
                    Thread.sleep(30000);//30 seconds
                    timeWait++;
                }
            } catch (Exception exp) {

            }
        }//while
        if (timeWait == 6) {
            return "File name starts with " + fileName + " not downloaded...";
        }
        return "Success";
    }

    public static void deleteIfFilePresent(String fileType) {
        String prefix = "";
        if (fileType.equalsIgnoreCase("Download all genes")) {
            prefix = "All_genes";
        } else if (fileType.equalsIgnoreCase("Download all STRs")) {
            prefix = "All_strs";
        } else if (fileType.equalsIgnoreCase("Download all Regions")) {
            prefix = "All_regions";
        }
        File location = new File(downloadLocation);
        File[] files = location.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().startsWith(prefix)) {
                files[i].delete();
                break;
            }
        }
    }

    public static void deleteAllFilesIfPresent(String fileType) {

        File location = new File(downloadLocation);
        File[] files = location.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
            Debugger.println("delete file:" + files[i]);
            break;
        }
    }

    public static String verifyEntityFileContent(String prefix, String contents) {

        File location = new File(downloadLocation);
        File[] files = location.listFiles();
        File actFile = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().startsWith(prefix)) {
                actFile = files[i];
                break;
            }
        }
        if (actFile == null) {
            return "File with name starts with " + prefix + " Not present in download location.";
        }
        String fileTitle = readCSVFileTitle(actFile);
        Debugger.println("Title from downloaded file: " + prefix + "\n" + fileTitle);
        String[] expTitles = contents.split(",");
        for (int i = 0; i < expTitles.length; i++) {
            if (!fileTitle.contains(expTitles[i])) {
                return "Expected Column " + expTitles[i] + " not present in the downloaded file.";
            }
        }
        return "Success";
    }

    static String readCSVFileTitle(File csvfile) {
        //MISDebugger.println("Reading content from the File: "+download_file_location+File.separator+download_file);
        String fileContent = "";
        try {
            Scanner file_scanner = new Scanner(csvfile);
            int count = 0;
            while (file_scanner.hasNextLine()) {
                fileContent = file_scanner.nextLine();
                count++;
                if (count == 1) {//Only title is reading.
                    break;
                }
            }
            file_scanner.close();
            //System.out.println("FULL CONTENT..\n"+fileContent);
            return fileContent;
        } catch (Exception exp) {
            Debugger.println("EXCEPTION \n readCSVFileTitle from :" + csvfile.toString() + "\n" + exp);
            return fileContent;
        }
    }

    static void extractJarFile(String location, String filename) {
        try {
            JarFile jar = new JarFile(new File(location + File.separator + filename));
            // fist get all directories,
            // then make those directory on the destination Path
            for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
                JarEntry entry = (JarEntry) enums.nextElement();

                String fileName = entry.getName();
                if (fileName.startsWith("META-INF")) {
                    continue;
                }
                Debugger.println("File NAme: " + fileName);
                File extractDir = new File(location + File.separator + "JarExtracted");

                if (!extractDir.exists()) {
                    extractDir.mkdirs();
                }

            }

            //now create all files
            for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
                JarEntry entry = (JarEntry) enums.nextElement();

                String fileName = entry.getName();
                if (fileName.startsWith("META-INF")) {
                    continue;
                }
                File f = new File(location + File.separator + "JarExtracted" + File.separator + fileName);

                if (!fileName.endsWith("/")) {
                    InputStream is = jar.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(f);

                    // write contents of 'is' to 'fos'
                    while (is.available() > 0) {
                        fos.write(is.read());
                    }

                    fos.close();
                    is.close();
                }
            }
        } catch (Exception exp) {
            Debugger.println("Exception from Jar Extraction: " + exp);
        }
    }

    public static String getPanelName(String panelNameWithVersion) {
        String panelNameFound = "";
        //System.out.println("input string to be searched for is = "+ InputString);
        Pattern p = Pattern.compile("\\([^()]*\\)");
        Matcher m = p.matcher(panelNameWithVersion);
        if (m.find()) {
            panelNameFound = panelNameWithVersion.replace(m.group(0), "");
        }
        //System.out.println("Fetched digit from the input string = "+ numFound);
        return panelNameFound.trim();
    }
}//end
