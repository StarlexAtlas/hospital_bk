package online.starlex.hospital.utils;

public enum ErrorCode {
    OK(0, "成功"),
    MEDICAL_NOT_EXISTS(-1, "病历本编号不存在"),
    PATIENT_HAS_EXISTS(-2, "该病人已存在，无法重复开户"),
    PATIENT_CAN_NOT_TRANSFER(-3, "该病人尚未入院，无法转移病房"),
    BED_NOT_ENOUGH(-4, "该病房病床不足，请换病房重试"),
    PATIENT_HAS_ADMISSION(-5, "病人已经在院"),
    PATIENT_HAS_DISCHARGE(-6, "病人已经离院"),
    ORDER_NOT_EXISTS(-7, "订单不存在"),
    RESOURCES_NOT_ENOUGH(-8, "非法订单（资源超额或无法退换）"),
    DRUG_NOT_EXISTS(-9, "药品不存在"),
    DRUG_CAN_NOT_REMOVE(-10, "存在未完成的订单，无法下架"),
    EQUIPMENT_NOT_EXISTS(-11, "设备不存在"),
    EQUIPMENT_CAN_NOT_REMOVE(-12, "存在未完成的订单，无法移除"),
    DRUG_NOT_ENOUGH(-13, "药品余量不足"),
    MEDICAL_HAS_REGISTERED(-14, "病历本编号已经被注册过，无法注册"),
    STAFF_LOGIN_ERROR(-15, "职员不存在或密码错误"),
    ORDER_CAN_NOT_COMPLETE(-16, "该订单顾客不存在，订单无法完成"),
    STAFF_NOT_EXISTS(-17, "职员不存在"),
    SICKBED_NOT_EXISTS(-18, "病床不存在"),
    SICKBED_CAN_NOT_REMOVE(-19, "该病床正在使用，无法移除")
    ;

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
