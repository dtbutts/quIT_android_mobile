package Model;

import java.util.Date;

public class Money {
    private Double total, avgWeekly;
    private Long startDate, currentDate;
    private Date startOfWeekDate, startOfMonthDate, endOfWeekDate, endOfMonthDate;

    public Money(Double total, Double avgWeekly, Long startDate, Date startOfWeek, Date endOfWeekDate, Date startOfMonth, Date endOfMonthDate, Long currentDate) {
        this.total = total;
        this.avgWeekly = avgWeekly;
        this.startDate = startDate;
        this.startOfWeekDate = startOfWeek;
        this.endOfWeekDate= endOfWeekDate;
        this.startOfMonthDate = startOfMonth;
        this.endOfMonthDate= endOfMonthDate;
        this.currentDate = currentDate;
    }


    public Money() {
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getAvgWeekly() {
        return avgWeekly;
    }

    public void setAvgWeekly(Double avgWeekly) {
        this.avgWeekly = avgWeekly;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Date getStartOfWeekDate() {
        return startOfWeekDate;
    }

    public void setStartOfWeekDate(Date startOfWeek) {
        this.startOfWeekDate = startOfWeek;
    }

    public Date getStartOfMonthDate() {
        return startOfMonthDate;
    }

    public void setStartOfMonthDate(Date startOfMonth) {
        this.startOfMonthDate = startOfMonth;
    }

    public Long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Long currentDate) {
        this.currentDate = currentDate;
    }

    public Date getEndOfWeekDate() {
        return endOfWeekDate;
    }

    public void setEndOfWeekDate(Date endOfWeekDate) {
        this.endOfWeekDate = endOfWeekDate;
    }

    public Date getEndOfMonthDate() {
        return endOfMonthDate;
    }

    public void setEndOfMonthDate(Date endOfMonthDate) {
        this.endOfMonthDate = endOfMonthDate;
    }
}
