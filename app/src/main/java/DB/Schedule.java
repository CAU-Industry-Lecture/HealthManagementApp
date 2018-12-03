package DB;

/**
 * Created by leesd on 2018-11-28.
 */

public class Schedule {
    private int sch_idx;
    private int exe_idx_fk;
    private String day;
    private String date;
    private String time;

    public Schedule(int sch_idx, int exe_idx_fk, String day, String date, String time) {
        this.sch_idx = sch_idx;
        this.exe_idx_fk = exe_idx_fk;
        this.day = day;
        this.date = date;
        this.time = time;
    }

    public void setSch_idx(int sch_idx) {
        this.sch_idx = sch_idx;
    }

    public void setExe_idx_fk(int exe_idx_fk) { this.exe_idx_fk = exe_idx_fk; }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) { this.time = time; }

    public int getSch_idx() {
        return sch_idx;
    }

    public int getExe_idx_fk() { return exe_idx_fk; }

    public String getDay() {
        return day;
    }

    public String getDate() {return date;}

    public String getTime() { return time; }

}
