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
	 * 存储一些对象信息
	 */
	private Object object;

	/**
	 * 设置运行单位
	 * 
	 * @param runnable
	 */
	public JPThread(Runnable target) {
		this.runnable = target;
	}

	public void run() {
		beginTime = System.currentTimeMillis();
		runnable.run();
		beginTime -= System.currentTimeMillis();
	}

	/**
	 * 获取运行时长
	 * 
	 * @return 运行时长
	 */
	public long getPeriod() {
		synchronized (beginTime) {
			if (beginTime > 0) {
				return (System.currentTimeMillis() - beginTime)/1000;
			}
		}
		return -beginTime/1000;
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

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

}
