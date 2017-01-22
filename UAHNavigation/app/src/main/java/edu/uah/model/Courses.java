package edu.uah.model;

/**
 * Created by Daniel on 1/16/2017.
 */

public class Courses {
    private long id;
    private int major_id;
    private int rooms_id;
    private int crn;
    private String course;
    private String title;
    private int credits;
    private String days;
    private String start;
    private String end;
    private String instructor;

    /**
     *
     * @param major_id
     * @param room_id
     * @param crn
     * @param course
     * @param title
     * @param credits
     * @param days
     * @param start
     * @param end
     * @param instructor
     */
    public void setAll(int major_id, int room_id, int crn, String course, String title,
                       int credits, String days, String start, String end, String instructor) {
        this.major_id = major_id;
        this.rooms_id = room_id;
        this.crn = crn;
        this.course = course;
        this.title = title;
        this.credits = credits;
        this.days = days;
        this.start = start;
        this.end = end;
        this.instructor = instructor;
    }

    /**
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getMajor() {
        return major_id;
    }

    /**
     *
     * @param major_id
     */
    public void setMajor(int major_id) {
        this.major_id = major_id;
    }

    /**
     *
     * @return
     */
    public int getRoom() {
        return rooms_id;
    }

    /**
     *
     * @param rooms_id
     */
    public void setRoom(int rooms_id) {
        this.rooms_id = rooms_id;
    }

    /**
     *
     * @return
     */
    public int getCRN() {
        return crn;
    }

    /**
     *
     * @param title
     */
    public void setCRN(int title) {
        this.crn = crn;
    }

    /**
     *
     * @return
     */
    public String getCourse() {
        return course;
    }

    /**
     *
     * @param course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public int getCredits() {
        return credits;
    }

    /**
     *
     * @param credits
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     *
     * @return
     */
    public String getDays() {
        return days;
    }

    /**
     *
     * @param days
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     *
     * @return
     */
    public String getStart() {
        return start;
    }

    /**
     *
     * @param start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     *
     * @return
     */
    public String getEnd() {
        return end;
    }

    /**
     *
     * @param end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     *
     * @return
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     *
     * @param instructor
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
