package leitura_arquivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleBasicLoader {

    public static ArrayList<TitleBasic> load(String filePath) throws IOException {
        ArrayList<TitleBasic> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null && list.size()!= 1000) {
                lineNumber++;
                String[] cols = parseTSVLine(line);
                if (cols.length < 9) continue;

                String tconst = cols[0];
                String titleType = cols[1];
                String primaryTitle = cols[2];
                String originalTitle = cols[3];
                boolean isAdult = "1".equals(cols[4]);
                Integer startYear = parseIntOrNull(cols[5]);
                Integer endYear = parseIntOrNull(cols[6]);
                Integer runtimeMinutes = parseIntOrNull(cols[7]);
                List<String> genres = parseGenres(cols[8]);

                list.add(new TitleBasic(tconst, titleType, primaryTitle, originalTitle,
                        isAdult, startYear, endYear, runtimeMinutes, genres));

            }
        }

        return list;
    }

    private static String[] parseTSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\t') {
                fields.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        fields.add(sb.toString());
        return fields.toArray(new String[0]);
    }

    private static Integer parseIntOrNull(String value) {
        if (value == null || value.isEmpty() || "\\N".equals(value)) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static List<String> parseGenres(String value) {
        if (value == null || value.isEmpty() || "\\N".equals(value)) return List.of();
        return Arrays.asList(value.split(","));
    }
}
