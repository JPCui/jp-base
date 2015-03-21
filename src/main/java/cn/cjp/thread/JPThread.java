package cn.cjp.thread;

/**
 * 自定义Thread <br>
 * 1.添加运行时间函数 {@link #getPeriod()} <br>
 * 
 * @author Administrator
 * 
 */
public class JPThread extends Thread {

	private Long beginTime = 0L;

	private Runnable runnable;

	/**
	 * 设置运行单位
	 * 
	 * @param runnable
	 */
	public JPThread(Runnable target) {
		this.runnable = target;
	}

	public void run() {
		System.out.println("begin");
		beginTime = System.currentTimeMillis();
		runnable.run();
		// try {
		// new Callable<Boolean>() {
		// public Boolean call() throws Exception {
		// return null;
		// }
		// }.call();
		// } catch (Exception e) {
		// System.err.println("运行失败。。。");
		// e.printStackTrace();
		// }
		beginTime -= System.currentTimeMillis();
		System.out.println("end");
	}

	/**
	 * 获取运行时长
	 * 
	 * @return 运行时长
	 */
	public long getPeriod() {
		System.out.println(beginTime);
		synchronized (beginTime) {
			if (beginTime > 0) {
				return System.currentTimeMillis() - beginTime;
			}
		}
		return -beginTime;
	}

	public static void main(String[] args) throws Exception {
		final JPThread jpThread = new JPThread(new Runnable() {
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(200);
						System.out.println("running body");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		jpThread.start();

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("period: " + i + " : " + jpThread.getPeriod());

		}

		System.out.println("finish");
	}

}
