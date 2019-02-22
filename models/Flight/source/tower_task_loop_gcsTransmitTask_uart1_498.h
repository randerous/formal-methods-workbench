/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#ifndef __TOWER_TASK_LOOP_GCSTRANSMITTASK_UART1_498_H__
#define __TOWER_TASK_LOOP_GCSTRANSMITTASK_UART1_498_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "controloutput_type.h"
#include "data_rate.h"
#include "flightmode_type.h"
#include "gcsstream_timing.h"
#include "gps_type.h"
#include "ivory.h"
#include "mavlink_hil_state_msg.h"
#include "motors_type.h"
#include "optflow_type.h"
#include "radio_info_type.h"
#include "radio_stat_type.h"
#include "sensors_type.h"
#include "tower_primitives.h"
#include "tower_task_usercode_gcsTransmitTask_uart1_498.h"
#include "userinput_type.h"
void read_gcsTransmitTask_uart1_498_dataport10_500(struct flightmode* n_var0);
void read_gcsTransmitTask_uart1_498_dataport1_502(struct sensors_result* n_var0);
void read_gcsTransmitTask_uart1_498_dataport235_504(struct position* n_var0);
void read_gcsTransmitTask_uart1_498_dataport200_506(struct controloutput* n_var0);
void read_gcsTransmitTask_uart1_498_dataport191_508(struct motors* n_var0);
void read_gcsTransmitTask_uart1_498_dataport368_510(struct radio_stat* n_var0);
void read_gcsTransmitTask_uart1_498_dataport12_512(uint8_t* n_var0);
bool emitFromTask_gcsTransmitTask_uart1_498_chan326_514(const
                                                        uint8_t n_var0[80U]);
bool receiveFromTask_gcsTransmitTask_uart1_498_chan330_518(int16_t* n_var0);
void read_gcsTransmitTask_uart1_498_dataport14_520(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport15_522(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport16_524(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport17_526(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport18_528(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport19_530(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport20_532(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport21_534(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport22_536(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport23_538(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport24_540(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport25_542(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport26_544(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport27_546(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport28_548(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport29_550(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport30_552(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport31_554(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport32_556(float* n_var0);
void read_gcsTransmitTask_uart1_498_dataport33_558(float* n_var0);
bool receiveFromTask_gcsTransmitTask_uart1_498_chan328_564(struct gcsstream_timing* n_var0);

#ifdef __cplusplus
}
#endif
#endif /* __TOWER_TASK_LOOP_GCSTRANSMITTASK_UART1_498_H__ */