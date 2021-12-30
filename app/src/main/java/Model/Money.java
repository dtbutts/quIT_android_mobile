package Model;

import java.util.Date;

public class Money {
    private Double total, avgWeekly, weekVal, monthVal;
    private Long startDate, currentDate;
    private Date startOfWeekDate, startOfMonthDate, endOfWeekDate, endOfMonthDate;

    public Money(Double total, Double avgWeekly, Long startDate, Double weekVal, Date startOfWeek, Date endOfWeekDate, Double monthVal, Date startOfMonth, Date endOfMonthDate, Long currentDate) {
        this.total = total;
        this.avgWeekly = avgWeekly;
        this.startDate = startDate;
        this.weekVal = weekVal;
        this.startOfWeekDate = startOfWeek;
        this.endOfWeekDate= endOfWeekDate;
        this.monthVal = monthVal;
        this.startOfMonthDate = startOfMonth;
        this.endOfMonthDate= endOfMonthDate;
        this.currentDate = currentDate;
    }


    public Money() {
    }

    public Double getWeekVal() {
        return weekVal;
    }

    public void setWeekVal(Double weekVal) {
        this.weekVal = weekVal;
    }

    public Double getMonthVal() {
        return monthVal;
    }

    public void setMonthVal(Double monthVal) {
        this.monthVal = monthVal;
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
