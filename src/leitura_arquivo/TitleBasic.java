package leitura_arquivo;

import java.util.Collections;
import java.util.List;

public class TitleBasic {
    private final String tconst;
    private final String titleType;
    private final String primaryTitle;
    private final String originalTitle;
    private final boolean isAdult;
    private final Integer startYear;
    private final Integer endYear;
    private final Integer runtimeMinutes;
    private final List<String> genres;

    public TitleBasic(String tconst, String titleType, String primaryTitle,
                      String originalTitle, boolean isAdult, Integer startYear,
                      Integer endYear, Integer runtimeMinutes, List<String> genres) {
        this.tconst = tconst;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres != null ? Collections.unmodifiableList(genres) : Collections.emptyList();
    }

    public String getTconst() { return tconst; }
    public int getTconstId() { return Integer.parseInt(tconst.substring(2)); }
    public String getTitleType() { return titleType; }
    public String getPrimaryTitle() { return primaryTitle; }
    public String getOriginalTitle() { return originalTitle; }
    public boolean isAdult() { return isAdult; }
    public Integer getStartYear() { return startYear; }
    public Integer getEndYear() { return endYear; }
    public Integer getRuntimeMinutes() { return runtimeMinutes; }
    public List<String> getGenres() { return genres; }

    @Override
    public String toString() {
        return "leitura_arquivo.TitleBasic{" +
                "tconst='" + tconst + '\'' +
                ", titleType='" + titleType + '\'' +
                ", primaryTitle='" + primaryTitle + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", isAdult=" + isAdult +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", runtimeMinutes=" + runtimeMinutes +
                ", genres=" + genres +
                '}';
    }
}
