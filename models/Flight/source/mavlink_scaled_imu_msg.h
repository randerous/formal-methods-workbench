/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#ifndef __MAVLINK_SCALED_IMU_MSG_H__
#define __MAVLINK_SCALED_IMU_MSG_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "mavlinkSendModule.h"
#include "mavlink_pack_ivory.h"
struct scaled_imu_msg {
    uint32_t time_boot_ms;
    int16_t xacc;
    int16_t yacc;
    int16_t zacc;
    int16_t xgyro;
    int16_t ygyro;
    int16_t zgyro;
    int16_t xmag;
    int16_t ymag;
    int16_t zmag;
} __attribute__((__packed__));
void mavlink_scaled_imu_msg_send(const struct scaled_imu_msg* n_var0,
                                 uint8_t* n_var1, uint8_t n_var2[80U]);
void mavlink_scaled_imu_unpack(struct scaled_imu_msg* n_var0, const
                               uint8_t* n_var1);

#ifdef __cplusplus
}
#endif
#endif /* __MAVLINK_SCALED_IMU_MSG_H__ */