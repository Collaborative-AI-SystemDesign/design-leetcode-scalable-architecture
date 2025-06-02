package com.example.demo.common;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.random.RandomGenerator;

public class Snowflake {
	private static final int UNUSED_BITS    = 1;
	private static final int EPOCH_BITS     = 41;
	private static final int NODE_ID_BITS   = 10;
	private static final int SEQUENCE_BITS  = 12;

	private static final long maxNodeId    = (1L << NODE_ID_BITS) - 1;
	private static final long maxSequence  = (1L << SEQUENCE_BITS) - 1;

	private final long nodeId = RandomGenerator.getDefault().nextLong(maxNodeId + 1);
	// UTC = 2024-01-01T00:00:00Z
	private final long startTimeMillis = 1704067200000L;

	private long lastTimeMillis = startTimeMillis;
	private long sequence       = 0L;

	// ReentrantLock 생성 (fairness 옵션은 필요에 따라 true/false)
	private final Lock lock = new ReentrantLock();

	public long nextId() {
		lock.lock();
		try {
			long currentTimeMillis = System.currentTimeMillis();

			if (currentTimeMillis < lastTimeMillis) {
				throw new IllegalStateException("Clock moved backwards. Refusing to generate id");
			}

			if (currentTimeMillis == lastTimeMillis) {
				sequence = (sequence + 1) & maxSequence;
				if (sequence == 0) {
					currentTimeMillis = waitNextMillis(currentTimeMillis);
				}
			} else {
				sequence = 0;
			}

			lastTimeMillis = currentTimeMillis;

			return ((currentTimeMillis - startTimeMillis) << (NODE_ID_BITS + SEQUENCE_BITS))
					| (nodeId << SEQUENCE_BITS)
					| sequence;
		} finally {
			lock.unlock();
		}
	}

	private long waitNextMillis(long currentTimestamp) {
		while (currentTimestamp <= lastTimeMillis) {
			currentTimestamp = System.currentTimeMillis();
		}
		return currentTimestamp;
	}
}
