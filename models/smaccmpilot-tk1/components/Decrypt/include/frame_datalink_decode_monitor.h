/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.5
 */
#ifndef __FRAME_DATALINK_DECODE_MONITOR_H__
#define __FRAME_DATALINK_DECODE_MONITOR_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "smaccm_UART_module.h"
#include "towerUartDeps.h"
#include "hxstream_state_module.h"
#include "ivory_serialize.h"
#include "frameBuffer_monitor.h"
extern struct hxstream_state hx_decoder;
extern uint8_t buf[195U];
extern bool overrun;
void callback_decoder_serial_in(const uint8_t *n_var0);

#ifdef __cplusplus
}
#endif
#endif /* __FRAME_DATALINK_DECODE_MONITOR_H__ */