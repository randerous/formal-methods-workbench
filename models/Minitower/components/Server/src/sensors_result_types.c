/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.4
 */
#include "sensors_result_types.h"

void sensors_result_get_le(const uint8_t *n_var0, uint32_t n_var1, struct sensors_result *n_var2)
{
    ibool_get_le(n_var0, n_var1, &n_var2->valid);
    ivory_serialize_unpack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->roll);
    ivory_serialize_unpack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->pitch);
    ivory_serialize_unpack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 9U), &n_var2->yaw);
    xyz_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 13U), &n_var2->omega);
    ivory_serialize_unpack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 25U), &n_var2->baro_alt);
    ivory_serialize_unpack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 29U), &n_var2->lidar_alt);
    xyz_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 33U), &n_var2->accel);
    time_micros_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 45U), &n_var2->ahrs_time);
    time_micros_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 53U), &n_var2->baro_time);
    time_micros_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 61U), &n_var2->lidar_time);
}

void sensors_result_get_be(const uint8_t *n_var0, uint32_t n_var1, struct sensors_result *n_var2)
{
    ibool_get_be(n_var0, n_var1, &n_var2->valid);
    ivory_serialize_unpack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->roll);
    ivory_serialize_unpack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->pitch);
    ivory_serialize_unpack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 9U), &n_var2->yaw);
    xyz_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 13U), &n_var2->omega);
    ivory_serialize_unpack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 25U), &n_var2->baro_alt);
    ivory_serialize_unpack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 29U), &n_var2->lidar_alt);
    xyz_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 33U), &n_var2->accel);
    time_micros_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 45U), &n_var2->ahrs_time);
    time_micros_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 53U), &n_var2->baro_time);
    time_micros_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 61U), &n_var2->lidar_time);
}

void sensors_result_set_le(uint8_t *n_var0, uint32_t n_var1, const struct sensors_result *n_var2)
{
    ibool_set_le(n_var0, n_var1, &n_var2->valid);
    ivory_serialize_pack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->roll);
    ivory_serialize_pack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->pitch);
    ivory_serialize_pack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 9U), &n_var2->yaw);
    xyz_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 13U), &n_var2->omega);
    ivory_serialize_pack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 25U), &n_var2->baro_alt);
    ivory_serialize_pack_float_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 29U), &n_var2->lidar_alt);
    xyz_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 33U), &n_var2->accel);
    time_micros_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 45U), &n_var2->ahrs_time);
    time_micros_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 53U), &n_var2->baro_time);
    time_micros_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 61U), &n_var2->lidar_time);
}

void sensors_result_set_be(uint8_t *n_var0, uint32_t n_var1, const struct sensors_result *n_var2)
{
    ibool_set_be(n_var0, n_var1, &n_var2->valid);
    ivory_serialize_pack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->roll);
    ivory_serialize_pack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->pitch);
    ivory_serialize_pack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 9U), &n_var2->yaw);
    xyz_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 13U), &n_var2->omega);
    ivory_serialize_pack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 25U), &n_var2->baro_alt);
    ivory_serialize_pack_float_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 29U), &n_var2->lidar_alt);
    xyz_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 33U), &n_var2->accel);
    time_micros_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 45U), &n_var2->ahrs_time);
    time_micros_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 53U), &n_var2->baro_time);
    time_micros_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 61U), &n_var2->lidar_time);
}