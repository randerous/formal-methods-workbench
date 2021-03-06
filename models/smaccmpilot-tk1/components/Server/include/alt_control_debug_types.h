/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.5
 */
#ifndef __ALT_CONTROL_DEBUG_TYPES_H__
#define __ALT_CONTROL_DEBUG_TYPES_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "ivory_serialize.h"
#include "pid_state_types.h"
typedef struct alt_control_debug {
            float alt_est;
            float alt_rate_est;
            float r22_gain;
            float ui_setp;
            float ui_rate_setp;
            struct pid_state pos;
            float vz_fb;
            float vz_ff;
            float vz_ff_rot;
            float vz_ctl;
        } alt_control_debug;
void alt_control_debug_get_le(const uint8_t *n_var0, uint32_t n_var1, struct alt_control_debug *n_var2);
void alt_control_debug_get_be(const uint8_t *n_var0, uint32_t n_var1, struct alt_control_debug *n_var2);
void alt_control_debug_set_le(uint8_t *n_var0, uint32_t n_var1, const struct alt_control_debug *n_var2);
void alt_control_debug_set_be(uint8_t *n_var0, uint32_t n_var1, const struct alt_control_debug *n_var2);

#ifdef __cplusplus
}
#endif
#endif /* __ALT_CONTROL_DEBUG_TYPES_H__ */
