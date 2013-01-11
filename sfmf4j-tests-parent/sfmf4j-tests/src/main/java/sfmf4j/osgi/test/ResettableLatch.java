/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 *
 * @author sswor
 */
public class ResettableLatch extends CountDownLatch {

    protected static final class Sync extends AbstractQueuedSynchronizer {

        private final int originalCount;

        public Sync(final int count) {
            this.originalCount = count;
            setState(count);
        }

        int getCount() {
            return getState();
        }

        int getOriginalCount() {
            return originalCount;
        }

        void reset() {
            setState(originalCount);
        }

        @Override
        public int tryAcquireShared(int acquires) {
            int currentCount = getCount();
            if (currentCount==0) {
                return 1;
            } else {
                return -1;
            }
        }

        @Override
        public boolean tryReleaseShared(int releases) {
            boolean released = false;
            boolean zeroOrDecremented = false;
            while (!zeroOrDecremented) {
                int currentCount = getState();
                if (currentCount == 0 || compareAndSetState(currentCount, currentCount-1)) {
                    zeroOrDecremented = true;
                    if (getCount()==0) {
                        released = true;
                    }
                }
            }
            return released;
        }
    }
    private final Sync sync;

    public ResettableLatch(int count) {
        super(count);
        sync = new Sync(count);
    }

    public void reset() {
        sync.reset();
    }

    @Override
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    @Override
    public void countDown() {
        sync.releaseShared(1);
    }

    @Override
    public long getCount() {
        return sync.getCount();
    }

    @Override
    public String toString() {
        return "ResettableLatch(originalCount=" + sync.getOriginalCount() + ", currentCount=" + sync.getCount();
    }
}
