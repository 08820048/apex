package com.xuyi.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 访问统计DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class VisitStatsDTO {
    
    private Long totalVisits;
    private Long todayVisits;
    private Long uniqueVisitors;
    private Long totalArticles;
    private List<DailyVisitDTO> recentVisits;
    private List<PopularPageDTO> popularPages;

    public VisitStatsDTO() {}

    @Getter
    @Setter
    public static class DailyVisitDTO {
        private LocalDate date;
        private Long visits;

        public DailyVisitDTO() {}

        public DailyVisitDTO(LocalDate date, Long visits) {
            this.date = date;
            this.visits = visits;
        }
    }

    @Getter
    @Setter
    public static class PopularPageDTO {
        private String uri;
        private Long visits;

        public PopularPageDTO() {}

        public PopularPageDTO(String uri, Long visits) {
            this.uri = uri;
            this.visits = visits;
        }
    }
}
