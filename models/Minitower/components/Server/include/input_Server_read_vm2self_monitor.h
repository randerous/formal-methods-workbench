/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.3
 */
#ifndef __INPUT_SERVER_READ_VM2SELF_MONITOR_H__
#define __INPUT_SERVER_READ_VM2SELF_MONITOR_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "accelerometer_sample_types.h"
#include "alt_control_debug_types.h"
#include "arming_mode_types.h"
#include "arming_status_types.h"
#include "att_control_debug_types.h"
#include "barometer_sample_types.h"
#include "camera_target_types.h"
#include "control_law_types.h"
#include "control_modes_types.h"
#include "control_output_types.h"
#include "control_setpoint_types.h"
#include "control_source_types.h"
#include "from_smaccm_camera_monitor.h"
#include "gps_fix_types.h"
#include "gyroscope_sample_types.h"
#include "heartbeat_types.h"
#include "ivory.h"
#include "magnetometer_sample_types.h"
#include "packed_status_types.h"
#include "pid_config_types.h"
#include "pid_state_types.h"
#include "position_sample_types.h"
#include "px4io_alarms_types.h"
#include "px4io_state_types.h"
#include "px4io_status_types.h"
#include "quadcopter_motors_types.h"
#include "rc_input_types.h"
#include "reboot_magic_types.h"
#include "reboot_req_types.h"
#include "rgb_led_setting_types.h"
#include "sensors_result_types.h"
#include "sequence_num_types.h"
#include "sequence_numbered_accelerometer_sample_types.h"
#include "sequence_numbered_alt_control_debug_types.h"
#include "sequence_numbered_arming_status_types.h"
#include "sequence_numbered_att_control_debug_types.h"
#include "sequence_numbered_barometer_sample_types.h"
#include "sequence_numbered_camera_target_types.h"
#include "sequence_numbered_control_law_types.h"
#include "sequence_numbered_control_modes_types.h"
#include "sequence_numbered_control_output_types.h"
#include "sequence_numbered_control_setpoint_types.h"
#include "sequence_numbered_float_types.h"
#include "sequence_numbered_gyroscope_sample_types.h"
#include "sequence_numbered_magnetometer_sample_types.h"
#include "sequence_numbered_packed_status_types.h"
#include "sequence_numbered_pid_config_types.h"
#include "sequence_numbered_position_sample_types.h"
#include "sequence_numbered_px4io_state_types.h"
#include "sequence_numbered_quadcopter_motors_types.h"
#include "sequence_numbered_rc_input_types.h"
#include "sequence_numbered_reboot_req_types.h"
#include "sequence_numbered_rgb_led_setting_types.h"
#include "sequence_numbered_sensors_result_types.h"
#include "sequence_numbered_stab_config_types.h"
#include "sequence_numbered_throttle_ui_types.h"
#include "sequence_numbered_tristate_types.h"
#include "sequence_numbered_user_input_result_types.h"
#include "sequence_numbered_user_input_types.h"
#include "sequence_numbered_xyz_calibration_types.h"
#include "smaccm_camera_module.h"
#include "stab_config_types.h"
#include "throttle_mode_types.h"
#include "throttle_ui_types.h"
#include "time_micros_types.h"
#include "towerCameraVMDeps.h"
#include "tristate_types.h"
#include "user_input_result_types.h"
#include "user_input_types.h"
#include "xyz_calibration_types.h"
#include "xyz_types.h"
#include "yaw_mode_types.h"
void callback_input_Server_read_vm2self_handler(const struct SMACCM_DATA__Camera_Bounding_Box_i *n_var0);

#ifdef __cplusplus
}
#endif
#endif /* __INPUT_SERVER_READ_VM2SELF_MONITOR_H__ */