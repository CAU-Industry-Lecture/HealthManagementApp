package DB;

public class Exercise {
    private int exe_idx;
    private String exe_name;
    private String exe_cate;
    private String exe_cal;
    private String interest;

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
}
