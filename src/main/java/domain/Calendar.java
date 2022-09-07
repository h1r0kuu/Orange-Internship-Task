package domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.ObjectMapperTimeUtil;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

    public class Calendar {

        @JsonProperty("working_hours")
        private Interval workingHours;
        @JsonProperty("planned_meeting")
        private List<Interval> plannedMeeting;

        private static final ObjectMapper mapper = ObjectMapperTimeUtil.mapper();

        private static final Duration meetingDuration = Duration.ofMinutes(30);

        public static Calendar readFromString(String json) throws JsonProcessingException {
            return mapper.readValue(json, Calendar.class);
        }

        public static Calendar readFromFile(String filePath) throws IOException {
            InputStream json = Calendar.class.getResourceAsStream(filePath);
            return mapper.readValue(json, Calendar.class);
        }

        public Interval getWorkingHours() {
            return workingHours;
        }

        public List<Interval> getPlannedMeeting() {
            return plannedMeeting;
        }

        public Interval getLastAvailableInterval() {
            return new Interval(getPlannedMeeting().get(getPlannedMeeting().size() - 1).getEnd(), getWorkingHours().getEnd());
        }

        public List<Interval> getAvailableIntervals() {
            List<Interval> availableHours = new ArrayList<>();
            for(int i = 0; i < plannedMeeting.size() - 1; i++) {
                LocalTime currIntervalEnd = plannedMeeting.get(i).getEnd();
                LocalTime nextIntervalStart = plannedMeeting.get(i + 1).getStart();
                if(!currIntervalEnd.equals(nextIntervalStart)) {
                    Interval interval = new Interval(currIntervalEnd, nextIntervalStart);
                    availableHours.add(interval);
                }
            }
            availableHours.add(getLastAvailableInterval());
            return availableHours;
        }

        public static List<Interval> mergeAvailableIntervals(Calendar first, Calendar second) {
            List<Interval> firstAvailableHours = first.getAvailableIntervals();
            List<Interval> secondAvailableHours = second.getAvailableIntervals();
            List<Interval> intersection = new ArrayList<>();
            for (Interval firstAvailableHour : firstAvailableHours) {
                for (Interval secondAvailableHour : secondAvailableHours) {
                    if (firstAvailableHour.hasIntersection(secondAvailableHour)) {
                        Interval interval = firstAvailableHour.getIntersection(secondAvailableHour);
                        boolean isEnoughTime = ChronoUnit.MINUTES.between(interval.getStart(), interval.getEnd()) >= meetingDuration.toMinutes();
                        if (isEnoughTime) {
                            intersection.add(interval);
                        }
                    }
                }
            }
            return intersection;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Calendar calendar = (Calendar) o;
            return Objects.equals(workingHours, calendar.workingHours) && Objects.equals(plannedMeeting, calendar.plannedMeeting);
        }
    }