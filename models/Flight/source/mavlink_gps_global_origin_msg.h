/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#ifndef __MAVLINK_GPS_GLOBAL_ORIGIN_MSG_H__
#define __MAVLINK_GPS_GLOBAL_ORIGIN_MSG_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "mavlinkSendModule.h"
#include "mavlink_pack_ivory.h"
struct gps_global_origin_msg {
    int32_t latitude;
    int32_t longitude;
    int32_t altitude;
} __attribute__((__packed__));
void mavlink_gps_global_origin_msg_send(const
                                        struct gps_global_origin_msg* n_var0,
                                        uint8_t* n_var1, uint8_t n_var2[80U]);
void mavlink_gps_global_origin_unpack(struct gps_global_origin_msg* n_var0,
                                      const uint8_t* n_var1);

#ifdef __cplusplus
}
#endif
#endif /* __MAVLINK_GPS_GLOBAL_ORIGIN_MSG_H__ */