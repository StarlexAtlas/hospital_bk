package online.starlex.hospital.service;

import online.starlex.hospital.utils.ErrorCode;

public interface FinanceService {
    //充值余额
    ErrorCode recharge(long medicalRecordId, int amount);
    //订单结算
    ErrorCode schedule(long orderId);
}
