package cn.cjp.core.stopWatch;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.cjp.utils.StringUtil;

/**
 * Stop watch utilities for timing of a thread of tasks.
 *
 * <p>
 * Invokes method {@link #start(java.lang.String)} for timing a task, invokes
 * method {@link #end()} for stop timing the corresponding task, the start and
 * end must match exactly.
 * </p>
 *
 * <p>
 * <b>Note</b>: Remember to {@linkplain Stopwatchs#release() release} a
 * stopwatch if finished statistics finally to avoid memory leak.
 * </p>
 *
 * <p>
 * See the following example snippet codes:
 * 
 * <pre>
 * Stopwatchs.start("task 1");
 * // task 1 includes three sub-tasks: task 1.1, task 1.2 and task 1.3
 * // .... task 1 processing ....
 *
 * Stopwatchs.start("task 1.1");
 * // .... task 1.1 processing ....
 * Stopwatchs.end(); // Ends 1.1
 *
 * Stopwatchs.start("task 1.2");
 * // task 1.2 includes two sub-tasks: task 1.2.1 and task 1.2.2
 * // .... task 1.2 processing ....
 *
 * Stopwatchs.start("task 1.2.1");
 * // .... task 1.2.1 processing ....
 * Stopwatchs.end(); // Ends 1.2.1
 *
 * Stopwatchs.start("task 1.2.2");
 * // .... task 1.2.2 processing ....
 * Stopwatchs.end(); // Ends 1.2.2
 *
 * Stopwatchs.end(); // Ends 1.2
 *
 * Stopwatchs.start("task 1.3");
 * // .... task 1.3 processing ....
 * Stopwatchs.end(); // Ends 1.3
 *
 * Stopwatchs.end(); // Ends 1
 *
 * // Shows the timing statistics
 * System.out.println(Stopwatchs.getTimingStat());
 *
 * Stopwatchs.release();
 * </pre>
 *
 * Outputs:
 * 
 * <pre>
 * [100.0]%, [80]ms [task 1]
 *   [62.5]%, [50]ms [task 1.1]
 *   [25.0]%, [20]ms [task 1.2]
 *     [0.0]%, [0]ms [task 1.2.1]
 *     [25.0]%, [20]ms [task 1.2.2]
 *   [12.5]%, [10]ms [task 1.3]
 * </pre>
 * <p>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.3, May 30, 2012
 * @see https://github.com/b3log/latke/blob/master/latke/src/main/java/org/b3log
 *      /latke/util/Stopwatchs.java
 */
public final class Stopwatchs {

    /**
     * Thread-local variable of a stopwatch.
     */
    private static final ThreadLocal<Stopwatch> STOPWATCH = new ThreadLocal<Stopwatch>();

    /**
     * Starts a task timing with the specified task title.
     *
     * @param taskTitle
     *            the specified task title
     */
    public static void start(final Task task) {
        Stopwatch root = STOPWATCH.get();

        if (null == root) {
            root = new Stopwatch(task); // Creates the root stopwatch
            STOPWATCH.set(root);

            return;
        }

        final Stopwatch recent = getRecentRunning(STOPWATCH.get());

        if (null == recent) {
            return;
        }

        recent.addLeaf(new Stopwatch(task)); // Adds sub-stopwatch
    }

    /**
     * Ends the timing of the recent task started by
     * {@link #start(java.lang.String)}.
     */
    public static void end() {
        final Stopwatch root = STOPWATCH.get();

        if (null == root) {
            return; // Donoting....
        }

        final Stopwatch recent = getRecentRunning(root);

        if (null == recent) {
            return;
        }

        recent.setEndTime(System.currentTimeMillis()); // Ends timing
    }

    /**
     * Releases the thread-local stopwatch.
     *
     * <p>
     * Sets the thread-local stopwatch with {@code null}.
     * </p>
     */
    public static void release() {
        Stopwatch stopwatch = getRoot();
        if (stopwatch != null) {
            long period = stopwatch.endTime - stopwatch.startTime;
            if (period > tooLongTime) {
                tooLongTasks.add(stopwatch);
            }
        }
        STOPWATCH.set(null);
    }

    static Long tooLongTime = 5000L;

    private static List<Stopwatch> tooLongTasks = new ArrayList<>();

    /**
     * Gets the current timing statistics.
     *
     * <p>
     * If a task is not ended, the outputs will be minus for percentage and
     * elapsed, the absolute value of the elapsed filed is the start time.
     * </p>
     *
     * @return the current timing statistics, returns {@code "No stopwatch"} if
     *         not stopwatch
     */
    public static String getTimingStat() {
        final Stopwatch root = STOPWATCH.get();

        if (null == root) {
            return "No stopwatch";
        }

        final StringBuilder stringBuilder = new StringBuilder();

        root.appendTimingStat(1, stringBuilder);

        return stringBuilder.toString();
    }

    /**
     * Gets elapsed time from the specified parent stopwatch with the specified
     * task title.
     *
     * @param taskTitle
     *            the specified task title
     * @return
     *         <ul>
     *         <li>
     *         {@linkplain org.b3log.latke.util.Stopwatchs.Stopwatch#getElapsedTime()
     *         elapsed time} of the found task if it
     *         {@linkplain org.b3log.latke.util.Stopwatchs.Stopwatch#isEnded()
     *         is ended}</li>
     *         <li>{@linkplain System#currentTimeMillis() the current time}
     *         subtracts
     *         {@linkplain org.b3log.latke.util.Stopwatchs.Stopwatch#startTime
     *         the start time} of the found task if it
     *         {@linkplain org.b3log.latke.util.Stopwatchs.Stopwatch#isRunning()
     *         is running}</li>
     *         <li>{@code -1} if not found any stopwatch corresponding to the
     *         specified task title</li>
     *         </ul>
     */
    public static long getElapsed(final Task task) {
        final long currentTimeMillis = System.currentTimeMillis();

        if (task == null) {
            return -1;
        }

        final Stopwatch root = STOPWATCH.get();

        if (null == root) {
            return -1;
        }

        final Stopwatch stopwatch = get(root, task);

        if (null == stopwatch) {
            return -1;
        }

        if (stopwatch.isEnded()) {
            return stopwatch.getElapsedTime();
        }

        return currentTimeMillis - stopwatch.getStartTime();
    }

    /**
     * Gets stopwatch from the specified parent stopwatch with the specified
     * task title.
     *
     * @param parent
     *            the specified parent
     * @param taskTitle
     *            the specified task title
     * @return stopwatch, returns {@code null} if not found
     */
    private static Stopwatch get(final Stopwatch parent, final Task task) {
        if (task.equals(parent.getTask())) {
            return parent;
        }

        for (final Stopwatch leaf : parent.getLeaves()) {
            final Stopwatch ret = get(leaf, task);

            if (null != ret) {
                return ret;
            }
        }

        return null;
    }

    /**
     * 获取根节点
     * 
     * @return
     */
    public static Stopwatch getRoot() {
        final Stopwatch root = STOPWATCH.get();
        return root;
    }

    /**
     * 如果当前task正在运行，则返回正在运行的 Stopwatch，没有正在运行，则返回 null
     * 
     * @return
     */
    public static Stopwatch getCurrent() {
        final Stopwatch root = getRecentRunning(STOPWATCH.get());
        return root;
    }

    /**
     * Gets the recent running stopwatch with the specified parent stopwatch.
     *
     * @param parent
     *            the specified parent stopwatch
     * @return the recent stopwatch, returns {@code null} if not found
     */
    private static Stopwatch getRecentRunning(final Stopwatch parent) {
        /**
         * 每次都从根节点查找，TODO 可以考虑优化
         */
        if (null == parent) {
            return null;
        }

        final List<Stopwatch> leaves = parent.getLeaves();

        if (leaves.isEmpty()) {
            if (parent.isRunning()) {
                return parent;
            } else {
                return null;
            }
        }

        for (int i = leaves.size() - 1; i > -1; i--) {
            final Stopwatch leaf = leaves.get(i);

            if (leaf.isRunning()) {
                return getRecentRunning(leaf);
            } else {
                continue;
            }
        }

        return parent;
    }

    /**
     * Private constructor.
     */
    private Stopwatchs() {
    }

    /**
     * Stopwatch for timing a task.
     *
     * <p>
     * A stopwatch based on a tree-structure for timing sub-tasks, and
     * calculating running time of tasks hierarchically.
     *
     * <pre>
     * 0 -- 0 -- 0 -- 0 -- 0 -- 0 -- 0 -- 0 -- 0 -- 0 -- 0
     *           0      -- 0 -- 0
     *                  -- 0 -- 0 -- 0 -- 0
     *                                 -- 0 -- 0 -- 0
     * </pre>
     * </p>
     *
     * <p>
     * The task for timing is specified by the {@linkplain #taskTitle task
     * title}, if exists two tasks with the same title in a task tree,
     * </p>
     *
     * @author <a href="http://88250.b3log.org">Liang Ding</a>
     * @version 1.0.0.0, Oct 12, 2011
     */
    public static class Stopwatch {

        /**
         * Task.
         */
        private Task task;

        /**
         * Leaf noes.
         */
        private List<Stopwatch> leaves = new ArrayList<Stopwatch>();

        /**
         * Start time.
         */
        private long startTime;

        /**
         * End time.
         */
        private long endTime;

        /**
         * Hundred.
         */
        private static final int HUNDRED = 100;

        /**
         * Math context for formatting percent.
         */
        private static final MathContext MATH_CONTEXT = new MathContext(4, RoundingMode.HALF_UP);

        /**
         * Constructs a stopwatch with the specified task title and starts it at
         * once.
         *
         * @param taskTitle
         *            the specified task title
         */
        Stopwatch(final Task task) {
            this.task = task;
            startTime = System.currentTimeMillis();
        }

        /**
         * Determines whether this stopwatch is ended.
         *
         * @return {@code true} if it is ended, returns {@code false} otherwise
         */
        public boolean isEnded() {
            return endTime > 0;
        }

        /**
         * Determines whether this stopwatch is running.
         *
         * @return {@code true} if it is running, return {@code false} otherwise
         */
        public boolean isRunning() {
            return 0 == endTime;
        }

        /**
         * Gets the task title.
         *
         * @return task title
         */
        public Task getTask() {
            return task;
        }

        /**
         * Gets the end time.
         *
         * @return end time
         */
        public long getEndTime() {
            return endTime;
        }

        /**
         * Sets the end time with the specified end time.
         *
         * @param endTime
         *            the specified end time
         */
        public void setEndTime(final long endTime) {
            this.endTime = endTime;
        }

        /**
         * Gets the start time.
         *
         * @return start time
         */
        public long getStartTime() {
            return startTime;
        }

        /**
         * Gets leaf stopwatches.
         *
         * @return leaves
         */
        public List<Stopwatch> getLeaves() {
            return Collections.unmodifiableList(leaves);
        }

        /**
         * Adds the specified leaf stopwatch.
         *
         * @param leaf
         *            the specified leaf stopwatch
         */
        public void addLeaf(final Stopwatch leaf) {
            leaves.add(leaf);
        }

        /**
         * Gets the elapsed time.
         *
         * @return elapsed time, {@linkplain #startTime startTime} -
         *         {@linkplain #endTime endTime}
         */
        public long getElapsedTime() {
            return endTime - startTime;
        }

        /**
         * Gets the elapsed time percent of root.
         *
         * @return percent of root
         */
        public float getPercentOfRoot() {
            final Stopwatch root = STOPWATCH.get();

            if (null == root) {
                return 0;
            }

            final float rootElapsedTime = (float) root.getElapsedTime();

            if (0 == rootElapsedTime) { // Denominator is equals to zero
                return 0;
            }

            return getElapsedTime() / rootElapsedTime * HUNDRED;
        }

        /**
         * Appends the timing statistics with the specified string builder.
         *
         * @param level
         *            the current level of the task tree
         * @param stringBuilder
         *            the specified string builder
         */
        private void appendTimingStat(final int level, final StringBuilder stringBuilder) {
            stringBuilder.append(toString());

            for (int i = 0; i < leaves.size(); i++) {
                final Stopwatch leaf = leaves.get(i);

                stringBuilder.append(getIndentBlanks(level * 2));
                leaf.appendTimingStat(level + 1, stringBuilder);
            }
        }

        /**
         * Gets the specified number of blanks.
         *
         * @param num
         *            the specified number
         * @return the specified number of blanks
         */
        private String getIndentBlanks(final int num) {
            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < num; i++) {
                builder.append(' ');
            }

            return builder.toString();
        }

        /**
         * 输出格式：阶梯型
         */
        @Override
        public String toString() {
            float percentOfRoot = getPercentOfRoot();

            if (0 > percentOfRoot) {
                percentOfRoot = 0F;
            }

            final BigDecimal percenOfRoot = new BigDecimal(percentOfRoot, MATH_CONTEXT);

            final StringBuilder stringBuilder = new StringBuilder("[").append(percenOfRoot).append("]%, [")
                    .append(getElapsedTime()).append("]ms [").append(getTask()).append("]")
                    .append(StringUtil.LINE_SEPARATOR);

            return stringBuilder.toString();
        }

        public String toFullString() {
            float percentOfRoot = getPercentOfRoot();

            if (0 > percentOfRoot) {
                percentOfRoot = 0F;
            }

            final BigDecimal percenOfRoot = new BigDecimal(percentOfRoot, MATH_CONTEXT);

            Task task = getTask();
            final StringBuilder stringBuilder = new StringBuilder("[").append(percenOfRoot).append("]%, [")
                    .append(getElapsedTime()).append("]ms [").append(task).append("[").append(task.getObj()).append("]")
                    .append("]").append(StringUtil.LINE_SEPARATOR);

            return stringBuilder.toString();
        }
    }

    public static class Task {

        private String taskTitle;

        private Object obj;

        public Task(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!Task.class.equals(obj.getClass())) {
                return false;
            }
            Task task = (Task) obj;
            return this.taskTitle.equals(task.getTaskTitle());
        }

        @Override
        public String toString() {
            return this.taskTitle;
        }

        public String getTaskTitle() {
            return taskTitle;
        }

        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

    }

}
