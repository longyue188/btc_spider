package com.ceo.reckless.entity;

public class MACDEntity {
    public long[] timeArray;
    public double[] difArray;
    public double[] deaArray;
    public double[] barArray;
    public int emptyNum;
    public int size;
    /**
     * 记录idx从0到大的顺序也是时间从小到大的顺序
     * 末尾0的个数为emptyNum
     * 注意各个array开始的idx和原始k list的不是对应的
     * 理论上正确的顺序应为
     * 0 0 0 v1 v2 v3 v4 v5
     * 实际顺序为
     * v1 v2 v3 v4 v5 0 0 0
     */
}
