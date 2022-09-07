package domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class Interval {
    private LocalTime start;
    private LocalTime end;

    public Interval() {}

    public Interval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public static int timeToMin(LocalTime time) {return (time.getHour() * 60) + time.getMinute();}
    public static LocalTime max(LocalTime first, LocalTime second) {return timeToMin(first) > timeToMin(second) ? first : second; }
    public static LocalTime min(LocalTime first, LocalTime second) {return timeToMin(first) < timeToMin(second) ? first : second;}

    public boolean hasIntersection(Interval another) {
        return getStart().isBefore(another.getStart()) && getEnd().isAfter(another.getStart()) ||
               getStart().isBefore(another.getEnd()) && getEnd().isAfter(another.getEnd()) ||
               getStart().isBefore(another.getStart()) && getEnd().isAfter(another.getEnd()) ||
               getStart().isAfter(another.getStart()) && getEnd().isBefore(another.getEnd());
    }

    public Interval getIntersection(Interval another) {
        Interval intersectionInterval = new Interval();

        intersectionInterval.setStart(Interval.max(getStart(), another.getStart()));
        intersectionInterval.setEnd(Interval.min(getEnd(), another.getEnd()));

        return intersectionInterval;
    }

    @Override
    public String toString() {
        return "[\"" + start + "\", \"" + end + "\"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval that = (Interval) o;
        return start.equals(that.start) && end.equals(that.end);
    }
}