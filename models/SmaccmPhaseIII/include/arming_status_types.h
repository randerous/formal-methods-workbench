/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.2
 */
#ifndef __ARMING_STATUS_TYPES_H__
#define __ARMING_STATUS_TYPES_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "ivory_serialize.h"
#include "tristate_types.h"
typedef struct arming_status { uint8_t rcinput;
                               uint8_t telem;
                               uint8_t px4io;
                               uint8_t gyro_cal;
                               uint8_t accel_cal;
                               uint8_t mag_cal;
                               uint8_t sens_cal;
} arming_status;
void arming_status_get_le (const uint8_t * n_var0, uint32_t n_var1, struct arming_status * n_var2);
void arming_status_get_be (const uint8_t * n_var0, uint32_t n_var1, struct arming_status * n_var2);
void arming_status_set_le (uint8_t * n_var0, uint32_t n_var1, const struct arming_status * n_var2);
void arming_status_set_be (uint8_t * n_var0, uint32_t n_var1, const struct arming_status * n_var2);

#ifdef __cplusplus
}
#endif
#endif /* __ARMING_STATUS_TYPES_H__ */