/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#ifndef __SENDERMODULES_H__
#define __SENDERMODULES_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "controloutput_type.h"
#include "data_rate.h"
#include "flightmode_type.h"
#include "gps_type.h"
#include "ivory.h"
#include "mavlink_attitude_msg.h"
#include "mavlink_attitude_quaternion_msg.h"
#include "mavlink_auth_key_msg.h"
#include "mavlink_battery_status_msg.h"
#include "mavlink_change_operator_control_ack_msg.h"
#include "mavlink_change_operator_control_msg.h"
#include "mavlink_command_ack_msg.h"
#include "mavlink_command_long_msg.h"
#include "mavlink_data16_msg.h"
#include "mavlink_data32_msg.h"
#include "mavlink_data64_msg.h"
#include "mavlink_data_stream_msg.h"
#include "mavlink_debug_msg.h"
#include "mavlink_debug_vect_msg.h"
#include "mavlink_global_position_int_msg.h"
#include "mavlink_global_position_setpoint_int_msg.h"
#include "mavlink_global_vision_position_estimate_msg.h"
#include "mavlink_gps_global_origin_msg.h"
#include "mavlink_gps_raw_int_msg.h"
#include "mavlink_heartbeat_msg.h"
#include "mavlink_highres_imu_msg.h"
#include "mavlink_hil_controls_msg.h"
#include "mavlink_hil_rc_inputs_raw_msg.h"
#include "mavlink_hil_state_msg.h"
#include "mavlink_local_position_ned_msg.h"
#include "mavlink_local_position_ned_system_global_offset_msg.h"
#include "mavlink_local_position_setpoint_msg.h"
#include "mavlink_manual_control_msg.h"
#include "mavlink_manual_setpoint_msg.h"
#include "mavlink_memory_vect_msg.h"
#include "mavlink_mission_ack_msg.h"
#include "mavlink_mission_clear_all_msg.h"
#include "mavlink_mission_count_msg.h"
#include "mavlink_mission_current_msg.h"
#include "mavlink_mission_item_msg.h"
#include "mavlink_mission_item_reached_msg.h"
#include "mavlink_mission_request_list_msg.h"
#include "mavlink_mission_request_msg.h"
#include "mavlink_mission_request_partial_list_msg.h"
#include "mavlink_mission_set_current_msg.h"
#include "mavlink_mission_write_partial_list_msg.h"
#include "mavlink_named_value_float_msg.h"
#include "mavlink_named_value_int_msg.h"
#include "mavlink_nav_controller_output_msg.h"
#include "mavlink_omnidirectional_flow_msg.h"
#include "mavlink_optical_flow_msg.h"
#include "mavlink_param_request_list_msg.h"
#include "mavlink_param_request_read_msg.h"
#include "mavlink_param_set_msg.h"
#include "mavlink_param_value_msg.h"
#include "mavlink_ping_msg.h"
#include "mavlink_radio_msg.h"
#include "mavlink_raw_imu_msg.h"
#include "mavlink_raw_pressure_msg.h"
#include "mavlink_rc_channels_override_msg.h"
#include "mavlink_rc_channels_raw_msg.h"
#include "mavlink_rc_channels_scaled_msg.h"
#include "mavlink_request_data_stream_msg.h"
#include "mavlink_roll_pitch_yaw_rates_thrust_setpoint_msg.h"
#include "mavlink_roll_pitch_yaw_speed_thrust_setpoint_msg.h"
#include "mavlink_roll_pitch_yaw_thrust_setpoint_msg.h"
#include "mavlink_safety_allowed_area_msg.h"
#include "mavlink_safety_set_allowed_area_msg.h"
#include "mavlink_scaled_imu_msg.h"
#include "mavlink_scaled_pressure_msg.h"
#include "mavlink_servo_output_raw_msg.h"
#include "mavlink_set_global_position_setpoint_int_msg.h"
#include "mavlink_set_gps_global_origin_msg.h"
#include "mavlink_set_local_position_setpoint_msg.h"
#include "mavlink_set_mode_msg.h"
#include "mavlink_set_quad_motors_setpoint_msg.h"
#include "mavlink_set_quad_swarm_led_roll_pitch_yaw_thrust_msg.h"
#include "mavlink_set_quad_swarm_roll_pitch_yaw_thrust_msg.h"
#include "mavlink_set_roll_pitch_yaw_speed_thrust_msg.h"
#include "mavlink_set_roll_pitch_yaw_thrust_msg.h"
#include "mavlink_setpoint_6dof_msg.h"
#include "mavlink_setpoint_8dof_msg.h"
#include "mavlink_state_correction_msg.h"
#include "mavlink_statustext_msg.h"
#include "mavlink_sys_status_msg.h"
#include "mavlink_system_time_msg.h"
#include "mavlink_vfr_hud_msg.h"
#include "mavlink_vicon_position_estimate_msg.h"
#include "mavlink_vision_position_estimate_msg.h"
#include "mavlink_vision_speed_estimate_msg.h"
#include "motors_type.h"
#include "radio_stat_type.h"
#include "sensors_type.h"
#include "userinput_type.h"
void gcs_transmit_send_data16(struct data_rate_state* n_var0, uint8_t* n_var1,
                              uint8_t n_var2[80U]);
void gcs_transmit_send_heartbeat(struct flightmode* n_var0, bool* n_var1,
                                 uint8_t* n_var2, uint8_t n_var3[80U]);
void gcs_transmit_send_attitude(struct sensors_result* n_var0, uint8_t* n_var1,
                                uint8_t n_var2[80U]);
void gcs_transmit_send_vfrhud(struct position* n_var0,
                              struct controloutput* n_var1,
                              struct sensors_result* n_var2, uint8_t* n_var3,
                              uint8_t n_var4[80U]);
void gcs_transmit_send_servo_output(struct motors* n_var0,
                                    struct controloutput* n_var1,
                                    uint8_t* n_var2, uint8_t n_var3[80U]);
void gcs_transmit_send_gps_raw_int(struct position* n_var0, uint8_t* n_var1,
                                   uint8_t n_var2[80U]);
void gcs_transmit_send_global_position_int(struct position* n_var0,
                                           struct sensors_result* n_var1,
                                           uint32_t n_var2, uint8_t* n_var3,
                                           uint8_t n_var4[80U]);
void gcs_transmit_send_param_value(struct param_value_msg* n_var0,
                                   uint8_t* n_var1, uint8_t n_var2[80U]);
void gcs_transmit_send_radio(struct radio_stat* n_var0, uint8_t* n_var1,
                             uint8_t n_var2[80U]);

#ifdef __cplusplus
}
#endif
#endif /* __SENDERMODULES_H__ */