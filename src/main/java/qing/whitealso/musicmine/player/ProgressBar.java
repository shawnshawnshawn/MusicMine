package qing.whitealso.musicmine.player;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 *     作者：程序员snx
 *     链接：https://juejin.cn/post/6985082383175254023
 *     来源：稀土掘金
 *     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * @author baiye
 * @since 2022/2/10 11:05 上午
 **/
public class ProgressBar {

    /**
     * 当前进度
     */
    private int index;
    /**
     * 步长
     */
    private int step;
    /**
     * 进度条长度,总进度数值
     */
    private int barLength;

    private int sleepDur;

    /**
     * 是否初始化
     */
    private boolean hasInited = false;
    /**
     * 是否已经结束
     */
    private boolean hasFinished = false;
    /**
     * 进度条title
     */
    private String title;

    private static final char processChar = '█';
    private static final char waitChar = '─';


    private ProgressBar() {
        index = 0;
        step = 1;
        barLength = 100;
        title = "Progress:";
    }

    public static ProgressBar build() {
        return new ProgressBar();
    }

    public static ProgressBar build(int step) {
        ProgressBar progressBar = build();
        progressBar.step = step;
        return progressBar;
    }

    public static ProgressBar build(int index, int step) {
        ProgressBar progressBar = build(step);
        progressBar.index = index;
        return progressBar;
    }

    public static ProgressBar build(int index, int step, String title) {
        ProgressBar progressBar = build(index, step);
        progressBar.title = title;
        return progressBar;
    }

    public static ProgressBar build(int index, int step, String title, int barLength, int sleepDur) {
        ProgressBar progressBar = build(index, step, title);
        progressBar.barLength = barLength;
        progressBar.sleepDur = sleepDur;
        return progressBar;
    }

    private String generate(int num, char ch) {
        if (num == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }

    private String genProcess(int num) {
        return generate(num, processChar);
    }

    private String genWaitProcess(int num) {
        return generate(num, waitChar);
    }

    /**
     * 清空进度条
     */
    private void cleanProcessBar() {
        System.out.print(generate(barLength / step + 6, '\b'));
    }

    /**
     * 进度+1
     */
    public void process() {
        if (hasFinished) return;
        checkInit();
        cleanProcessBar();
        index++;
        drawProgressBar();
        checkFinish();
    }

    /**
     * 进度+指定数值
     *
     * @param process 指定数值
     */
    public void process(int process) {
        checkStatus();
        checkInit();
        cleanProcessBar();
        if (index + process >= barLength)
            index = barLength;
        else
            index += process;
        drawProgressBar();
        checkFinish();
    }

    /**
     * 步进
     */
    public void step() {
        checkStatus();
        checkInit();
        cleanProcessBar();
        if (index + step >= barLength)
            index = barLength;
        else
            index += step;
        drawProgressBar();
        checkFinish();
    }


    /**
     * 绘制进度条
     */
    public void drawProgressBar() {
        System.out.print(
                String.format(
                        "%3d%%├%s%s┤",
                        index,
                        genProcess(index / step),
                        genWaitProcess(barLength / step - index / step)
                )
        );
    }


    /**
     * 检查进度条状态
     * 已完成的进度条不可以继续执行
     */
    private void checkStatus() {
        if (hasFinished) throw new RuntimeException("进度条已经完成");
    }

    /**
     * 检查是否已经初始化
     */
    private void checkInit() {
        if (!hasInited) init();
    }


    /**
     * 检查是否已经完成
     */
    private void checkFinish() {
        if (hasFinished() && !hasFinished) finish();
    }

    /**
     * 是否已经完成进度条
     *
     * @return
     */
    public boolean hasFinished() {
        return index >= barLength;
    }

    /**
     * 初始化进度条
     */
    private void init() {
        checkStatus();
        System.out.print(title);
        System.out.print(String.format("%3d%%[%s%s]", index, genProcess(index / step), genWaitProcess(barLength / step - index / step)));
        hasInited = true;
    }

    /**
     * 结束进度条，由 checkFinish()调用
     */
    private void finish() {
        System.out.println();
        hasFinished = true;
    }

    /**
     * 间隔50ms 自动执行进度条
     */
    @SneakyThrows
    public void printProgress() {
        init();
        do {
            process();
            try {
                TimeUnit.MILLISECONDS.sleep(sleepDur);
            } catch (Exception e) {
            }
        } while (index <= barLength);
        System.out.println();
    }


}
