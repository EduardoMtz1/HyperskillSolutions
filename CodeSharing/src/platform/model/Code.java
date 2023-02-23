package platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Codes")
public class Code {

    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id = UUID.randomUUID().toString();

    String code;
    String date;
    long time;
    int views;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    long startSeconds;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    boolean timeLimit;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    boolean viewLimit;

    public Code() {
    }

    public Code(String code, String date, int time, int views, boolean timeLimit, boolean viewLimit) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
        this.timeLimit = timeLimit;
        this.viewLimit = viewLimit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(boolean timelimit) {
        this.timeLimit = timelimit;
    }

    public boolean isViewLimit() {
        return viewLimit;
    }

    public void setViewLimit(boolean viewlimit) {
        this.viewLimit = viewlimit;
    }

    public long getStartSeconds() {
        return startSeconds;
    }

    public void setStartSeconds(long startSeconds) {
        this.startSeconds = startSeconds;
    }

}