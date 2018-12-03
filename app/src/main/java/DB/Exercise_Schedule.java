package DB;

/**
 * Created by leesd on 2018-12-03.
 */

public class Exercise_Schedule {
    private boolean isSuccess;
    private int sch_idx;
    private int exe_idx;

    public Exercise_Schedule(boolean isSuccess, int sch_idx, int exe_idx) {
        this.isSuccess = isSuccess;
        this.sch_idx = sch_idx;
        this.exe_idx = exe_idx;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getSch_idx() {
        return sch_idx;
    }

    public void setSch_idx(int sch_idx) {
        this.sch_idx = sch_idx;
    }

    public int getExe_idx() {
        return exe_idx;
    }

    public void setExe_idx(int exe_idx) {
        this.exe_idx = exe_idx;
    }
}
