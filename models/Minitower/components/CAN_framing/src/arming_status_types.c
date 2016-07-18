/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.3
 */
#include "arming_status_types.h"

void arming_status_get_le(const uint8_t *n_var0, uint32_t n_var1, struct arming_status *n_var2)
{
    tristate_t_get_le(n_var0, n_var1, &n_var2->rcinput);
    tristate_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->telem);
    tristate_t_get_le(n_var0, (uint32_t) ((uint32_t) 2U + n_var1), &n_var2->px4io);
    tristate_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 3U), &n_var2->gyro_cal);
    tristate_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 4U), &n_var2->accel_cal);
    tristate_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->mag_cal);
    tristate_t_get_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 6U), &n_var2->sens_cal);
}

void arming_status_get_be(const uint8_t *n_var0, uint32_t n_var1, struct arming_status *n_var2)
{
    tristate_t_get_be(n_var0, n_var1, &n_var2->rcinput);
    tristate_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->telem);
    tristate_t_get_be(n_var0, (uint32_t) ((uint32_t) 2U + n_var1), &n_var2->px4io);
    tristate_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 3U), &n_var2->gyro_cal);
    tristate_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 4U), &n_var2->accel_cal);
    tristate_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->mag_cal);
    tristate_t_get_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 6U), &n_var2->sens_cal);
}

void arming_status_set_le(uint8_t *n_var0, uint32_t n_var1, const struct arming_status *n_var2)
{
    tristate_t_set_le(n_var0, n_var1, &n_var2->rcinput);
    tristate_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->telem);
    tristate_t_set_le(n_var0, (uint32_t) ((uint32_t) 2U + n_var1), &n_var2->px4io);
    tristate_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 3U), &n_var2->gyro_cal);
    tristate_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 4U), &n_var2->accel_cal);
    tristate_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->mag_cal);
    tristate_t_set_le(n_var0, (uint32_t) (n_var1 + (uint32_t) 6U), &n_var2->sens_cal);
}

void arming_status_set_be(uint8_t *n_var0, uint32_t n_var1, const struct arming_status *n_var2)
{
    tristate_t_set_be(n_var0, n_var1, &n_var2->rcinput);
    tristate_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 1U), &n_var2->telem);
    tristate_t_set_be(n_var0, (uint32_t) ((uint32_t) 2U + n_var1), &n_var2->px4io);
    tristate_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 3U), &n_var2->gyro_cal);
    tristate_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 4U), &n_var2->accel_cal);
    tristate_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 5U), &n_var2->mag_cal);
    tristate_t_set_be(n_var0, (uint32_t) (n_var1 + (uint32_t) 6U), &n_var2->sens_cal);
}