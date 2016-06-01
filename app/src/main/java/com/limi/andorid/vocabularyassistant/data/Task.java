package com.limi.andorid.vocabularyassistant.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by limi on 16/5/10.
 */
public class Task {

    public static ArrayList<Task> tasks = new ArrayList<>();
    private static int lastTaskID = 0;
    private int startList;
    private int endList;
    private int startUnit;
    private int endUnit;
    private int bookId;
    private int currentID;
    private int taskID;
    private Date date;

    public Task(int startList, int endList, int startUnit, int endUnit, int bookId) {
        this.startList = startList;
        this.endList = endList;
        this.startUnit = startUnit;
        this.endUnit = endUnit;
        this.bookId = bookId;
        taskID = lastTaskID++;
        date = new Date();
        tasks.add(this);
    }

    public static int getLastTaskID() {
        return lastTaskID;
    }

    public static void setLastTaskID(int lastTaskID) {
        Task.lastTaskID = lastTaskID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStartList() {
        return startList;
    }

    public void setStartList(int startList) {
        this.startList = startList;
    }

    public int getEndList() {
        return endList;
    }

    public void setEndList(int endList) {
        this.endList = endList;
    }

    public int getStartUnit() {
        return startUnit;
    }

    public void setStartUnit(int startUnit) {
        this.startUnit = startUnit;
    }

    public int getEndUnit() {
        return endUnit;
    }

    public void setEndUnit(int endUnit) {
        this.endUnit = endUnit;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCurrentID() {
        return currentID;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }
}
