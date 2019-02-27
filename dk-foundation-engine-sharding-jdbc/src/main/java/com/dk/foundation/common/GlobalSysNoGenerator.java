package com.dk.foundation.common;

import java.util.HashSet;
import java.util.Set;

public class GlobalSysNoGenerator {

    /**
     * 回推5年
     */
    private final static long EPOCH = 5 * 365 * 24 * 3600 * 1000L;

    private static volatile GlobalSysNoGenerator instance = new GlobalSysNoGenerator();

    private long sequence = 0L;

    /**
     * workId，当前根据服务器ip计算得出
     */
    private long workerId;

    /**
     * 工作机器位数，默认为10位，支持1024台机器
     */
    private int workerIdBits = 10;

    /**
     * 递增序列位数，默认为12位，每毫秒支持4096序列
     */
    private int sequenceBits = 12;

    /**
     * 保留位，未启用
     */
    private int keepBits = 0;

    private int workerIdShift = sequenceBits + keepBits;

    /**
     * timestamp 41位,大概可用69年
     */
    private int timestampLeftShift = workerIdBits + workerIdShift;

    private long lastTimestamp = -1L;

    /**
     * 最大workId
     */
    private long maxWorkerId = ~(-1 << workerIdBits);

    private int sequenceMask =  ~(-1 << sequenceBits);
    /**
     * 回拨时间最大差值等待时间（10ms，等待20毫秒）
     */
    private final static int MAX_OFFSET_WAIT = 10;

    private GlobalSysNoGenerator(){}

    public GlobalSysNoGenerator(int workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;
    }


    public static GlobalSysNoGenerator getInstance(){
        long serverIp = IPUtils.ipToLong(IPUtils.getLocalIp());
        instance.setWorkerId(Math.abs((serverIp % instance.getMaxWorkerId())));
        return instance;
    }

    public static GlobalSysNoGenerator getInstance(int workerId){
        instance.setWorkerId(workerId);
        return instance;
    }

    public GlobalSysNoGenerator workIdBits(int workIdBits) {
        this.setWorkerIdBits(workIdBits);
        return this;
    }

    public GlobalSysNoGenerator sequenceBits(int sequenceBits) {
        this.setSequenceBits(sequenceBits);
        return this;
    }

    /**
     * 64bit 整型数
     * 组成       符号     时间戳(固定41位，69年) workid(默认10，支持 1024台机器) 序列号(默认12位，4096序列/ms)
     * ----      ------           ------               -----                     -----
     * 位数       1               41                     10                        12
     * @return long型整数
     */
    public synchronized long nextSysno() {
        long timestamp = System.currentTimeMillis();
        /**
         * 出现时间回拨
         */
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= MAX_OFFSET_WAIT) {
                try {
                    //时间偏差大小小于10ms，则等待两倍时间
                    wait(offset << 1);
                    timestamp = System.currentTimeMillis();
                    if (timestamp < lastTimestamp) {
                        //还是小于，抛异常并上报
                        throw new RuntimeException("时间产生回拨，id生成器不可用" + timestamp);
                    }
                } catch (InterruptedException e) {
                    throw  new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("时间产生回拨，id生成器不可用" + timestamp);
            }
        }

        /**
         * 时间戳相等，递增序列号
         */
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L) & sequenceMask;
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(timestamp);
            }
        } else {
            this.sequence = 0L;
        }
        this.lastTimestamp = timestamp;
        return ((timestamp - EPOCH) << timestampLeftShift)
                | (this.workerId << (sequenceBits + keepBits))
                | (this.sequence << keepBits);
    }

    /**
     * 等待到下一个时间戳
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public long getMaxWorkerId() {
        return maxWorkerId;
    }

    public void setMaxWorkerId(long maxWorkerId) {
        this.maxWorkerId = maxWorkerId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public int getWorkerIdBits() {
        return workerIdBits;
    }

    public void setWorkerIdBits(int workerIdBits) {
        this.workerIdBits = workerIdBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public void setSequenceBits(int sequenceBits) {
        this.sequenceBits = sequenceBits;
    }

    public int getKeepBits() {
        return keepBits;
    }

    public void setKeepBits(int keepBits) {
        this.keepBits = keepBits;
    }

    public void setWorkerId(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;
    }

    public static void main(String[] args) {
        GlobalSysNoGenerator worker = GlobalSysNoGenerator.getInstance();
        int length = 100;
        long st = System.currentTimeMillis();
        Set<Long> set = new HashSet<>();
        for (int i = 0; i < length; i++) {
            long sysno = worker.nextSysno();
            set.add(sysno);
            System.out.println(sysno);
            System.out.println(Long.toBinaryString(sysno)+" \t "+Long.toBinaryString(sysno).length());
        }
        long elapsed = System.currentTimeMillis() - st;
        System.out.println(elapsed + "毫秒");
    }
}
