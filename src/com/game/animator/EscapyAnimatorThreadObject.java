package com.game.animator;

// TODO: Auto-generated Javadoc
/**
 * The Class EscapyAnimatorThreadObject.
 */
public class EscapyAnimatorThreadObject extends EscapyAnimatorSuper implements Runnable {

	/**
	 * Instantiates a new escapy animator thread object.
	 */
	public EscapyAnimatorThreadObject() {
		/** VOID **/
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while (!stopItNow[0]) {
			while (!finished[0]) {
				for (int i = 0; i < animatedList.size(); i++) {
					if (animatedList.get(i) instanceof EscapyAnimatorObject) {
						((EscapyAnimatorObject) animatedList.get(i))
								.InterruptAnimator((EscapyAnimatorObject) animatedList.get(i));
						if (indexOfList.get(i).intValue() == EscapyAnimatorSuper.ACTIVE)
							((EscapyAnimatorObject) animatedList.get(i)).defineAnimation();
					}
				}
				if (stopItNow[0] && !stopItNow[1])
					Thread.currentThread().interrupt();
				try {
					if (!stopItNow[0] && !stopItNow[1]) {
						Thread.sleep(5);
					} else {
						Thread.currentThread().interrupt();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Thread.currentThread().interrupt();
	}

}