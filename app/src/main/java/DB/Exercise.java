package DB;

public class Exercise {
    private int exe_idx;
    private String exe_name;
    private String exe_cate;
    private String exe_cal;
    private String interest;
    private int count_all;
    private int count_now;

    public int getExe_idx() { return exe_idx; }
    public String getExe_name() {
        return exe_name;
    }
    public String getExe_cate() {
        return exe_cate;
    }
    public String getExe_cal() {
        return exe_cal;
    }
    public String getInterest() {
        return interest;
    }
    public int getCount_all() {
        return count_all;
    }
    public int getCount_now() {
        return count_now;
    }

    public void setExe_idx(int exe_idx) { this.exe_idx = exe_idx;}
    public void setExe_name(String exe_name) {
        this.exe_name = exe_name;
    }
    public void setExe_cate(String exe_cate) {
        this.exe_cate = exe_cate;
    }
    public void setExe_cal(String exe_cal) {
        this.exe_cal = exe_cal;
    }
    public void setInterest(String interest) {
        this.interest = interest;
    }
    public void setCount_all(int count_all) {
        this.count_all = count_all;
    }
    public void setCount_now(int count_now) {
        this.count_now = count_now;
    }
}
