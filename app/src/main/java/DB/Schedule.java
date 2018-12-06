package DB;

/**
 * Created by leesd on 2018-11-28.
 */

public class Schedule {
    private int sch_idx;
    private int exe_idx_fk;
    private String day;
    private String date;
    private String isSuccess;
    private int count_all;
    private int count_now;

    public Schedule(int sch_idx, int exe_idx_fk, String day, String date, String isSuccess, int count_all, int count_now) {
        this.sch_idx = sch_idx;
        this.exe_idx_fk = exe_idx_fk;
        this.day = day;
        this.date = date;
        this.isSuccess = isSuccess;
        this.count_all = count_all;
        this.count_now = count_now;
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

    public void setIsSuccess(String isSuccess) { this.isSuccess = isSuccess; }

    public int getSch_idx() {
        return sch_idx;
    }

    public int getExe_idx_fk() { return exe_idx_fk; }

    public String getDay() {
        return day;
    }

    public String getDate() {return date;}

    public String getIsSuccess() { return isSuccess; }

    public int getCount_all() {
        return count_all;
    }

    public void setCount_all(int count_all) {
        this.count_all = count_all;
    }

    public int getCount_now() {
        return count_now;
    }

    public void setCount_now(int count_now) {
        this.count_now = count_now;
    }

}
