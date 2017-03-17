package edu.uah.model;

/**
 * Created by Daniel on 1/16/2017.
 */

public class Courses {
    private long id;
    private long major_id;
    private long rooms_id;
    private String crn;
    private String course;
    private String section;
    private String title;
    private String credits;
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
    public void setAll(long major_id, long room_id, String crn, String course, String section, String title,
                       String credits, String days, String start, String end, String instructor) {
        this.major_id = major_id;
        this.rooms_id = room_id;
        this.crn = crn;
        this.course = course;
        this.section = section;
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
    public long getMajor() {
        return major_id;
    }

    /**
     *
     * @param major_id
     */
    public void setMajor(long major_id) {
        this.major_id = major_id;
    }

    /**
     *
     * @return
     */
    public long getRoom() {
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
    public String getCRN() {
        return crn;
    }

    /**
     *
     * @param crn
     */
    public void setCRN(String crn) {
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
    public String getSection() {
        return section;
    }

    /**
     *
     * @param section
     */
    public void setSection(String section) {
        this.section = section;
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
    public String getCredits() {
        return credits;
    }

    /**
     *
     * @param credits
     */
    public void setCredits(String credits) {
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

    public String print() {
        return " ID:" + this.id + " MAJOR_ID:" + this.major_id + " ROOM_ID:" + this.rooms_id + " CRN:" + this.crn +
                " COURSE:" + this.course + " SECTION:" + this.section + " TITLE:" + this.title + " CREDITS:" + this.credits +
                " DAYS:" + this.days + " START:" + this.start + " END:" + this.end + " INSTRUCTOR:" + this.instructor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)id;
        result = prime * result + ((crn == null) ? 0 : crn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Courses other = (Courses) obj;
        if (id != other.id)
            return false;
        if (course == null) {
            if (other.course != null)
                return false;
        } else if (!course.equals(other.course))
            return false;
        return true;
    }
}
