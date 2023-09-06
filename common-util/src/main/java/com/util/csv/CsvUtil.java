package com.util.csv;

import java.io.FileWriter;
import java.io.IOException;

public class CsvUtil {

    private static FileWriter csvFileWriter;

    private static FileWriter getWriter(final String csvPath) throws IOException {
        if (null == csvFileWriter) {
            csvFileWriter = new FileWriter(csvPath, true);
        }
        return csvFileWriter;
    }

    public static void writeRecord(final String csvPath, final String... record ) throws IOException {
        final StringBuffer sb = new StringBuffer();
        for (String column : record) {
            sb.append(column).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(",")).append("\n");
        getWriter(csvPath).write(sb.toString());
        getWriter(csvPath).flush();
    }
}
