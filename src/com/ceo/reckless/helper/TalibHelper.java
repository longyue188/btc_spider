package com.ceo.reckless.helper;

import com.ceo.reckless.Env;
import com.ceo.reckless.entity.KEntity;
import com.ceo.reckless.entity.MACDEntity;
import com.ceo.reckless.utils.LogUtils;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.util.List;

public class TalibHelper {

    private static boolean TALIB_DEBUG = false;

    public static MACDEntity genMacd(List<KEntity> keList) {

        if (keList == null || keList.size() == 0) {
            LogUtils.logDebugLine("k line data list null");
            return null;
        }

        Core core = new Core();
        // (
        //  int startIdx, int endIdx, double[] inReal, int optInFastPeriod, int optInSlowPeriod, int optInSignalPeriod,
        //  MInteger outBegIdx, MInteger outNBElement, double[] outMACD, double[] outMACDSignal, double[] outMACDHist
        // )
        int startIdx = 0;
        int endIdx = 0;
        double[] inReal = new double[keList.size()];
        int optInFastPeriod = 0;
        int optInSlowPeriod = 0;
        int optInSignalPeriod = 0;

        MInteger outBegIdx = new MInteger();
        MInteger outNBElement = new MInteger();
        double[] outMACD = new double[keList.size()];
        double[] outMACDSignal = new double[keList.size()];
        double[] outMACDHist = new double[keList.size()];

        // 根据k线数据初始化macd参数
        int i = 0;
        for (KEntity item : keList) {
            inReal[i++] = item.close;
        }
        startIdx = 0;
        endIdx = keList.size() - 1;
        optInFastPeriod = Env.MACD_PERIOD_FAST;
        optInSlowPeriod = Env.MACD_PERIOD_LOW;
        optInSignalPeriod = Env.MACD_PERIOD_SIGNAL;

        RetCode ret = core.macd(startIdx, endIdx, inReal, optInFastPeriod, optInSlowPeriod, optInSignalPeriod,
                outBegIdx, outNBElement, outMACD, outMACDSignal, outMACDHist);
        if (ret != RetCode.Success) {
            LogUtils.logDebugLine("genMacd() error " + ret.name());
            return null;
        }

        if (TALIB_DEBUG) {
            LogUtils.logDebugLine("outBegIdx " + outBegIdx.value + " outNBElement " + outNBElement.value);
        }

        MACDEntity result = new MACDEntity();
        result.timeArray = new long[keList.size()];
        result.difArray= outMACD;
        result.deaArray = outMACDSignal;
        result.barArray = outMACDHist;
        result.emptyNum = outBegIdx.value;
        result.size = outNBElement.value;
        // 处理时间戳和macd值
        int j;
        for (i = outBegIdx.value, j = 0; i < keList.size() && j < result.timeArray.length; i++, j++) {
            result.timeArray[j] = keList.get(i).timestamp;
            result.barArray[j] += result.barArray[j];
        }

        return result;
    }
}
