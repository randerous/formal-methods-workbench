/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "mavlink_set_global_position_setpoint_int_msg.h"

void mavlink_set_global_position_setpoint_int_msg_send(const
                                                       struct set_global_position_setpoint_int_msg* n_var0,
                                                       uint8_t* n_var1,
                                                       uint8_t n_var2[80U])
{
    uint8_t n_local0[15U] = {};
    uint8_t* n_ref1 = n_local0;
    int32_t n_deref2 = n_var0->latitude;
    
    mavlink_pack_int32_t((uint8_t*) n_ref1, 0U, n_deref2);
    
    int32_t n_deref3 = n_var0->longitude;
    
    mavlink_pack_int32_t((uint8_t*) n_ref1, 4U, n_deref3);
    
    int32_t n_deref4 = n_var0->altitude;
    
    mavlink_pack_int32_t((uint8_t*) n_ref1, 8U, n_deref4);
    
    int16_t n_deref5 = n_var0->yaw;
    
    mavlink_pack_int16_t((uint8_t*) n_ref1, 12U, n_deref5);
    
    uint8_t n_deref6 = n_var0->coordinate_frame;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 14U, n_deref6);
    for (int32_t n_ix7 = 0; n_ix7 <= 14; n_ix7++) {
        ASSERTS(n_ix7 > 0 && 2147483647 - n_ix7 >= 6 || n_ix7 <= 0);
        if (n_ix7 + 6 >= 80) { } else {
            uint8_t n_deref8 = n_ref1[n_ix7];
            
            ASSERTS(n_ix7 > 0 && 2147483641 >= n_ix7 || n_ix7 <= 0);
            ASSERTS(0 <= 6 + n_ix7 && 6 + n_ix7 < 80);
            *&n_var2[(6 + n_ix7) % 80] = n_deref8;
        }
    }
    mavlinkSendWithWriter(53U, 33U, 15U, n_var1, n_var2);
    for (int32_t n_ix9 = 0; n_ix9 <= 56; n_ix9++) {
        ASSERTS(n_ix9 > 0 && 2147483647 - n_ix9 >= 23 || n_ix9 <= 0);
        ASSERTS(0 <= n_ix9 + 23 && n_ix9 + 23 < 80);
        *&n_var2[(n_ix9 + 23) % 80] = 0U;
    }
    return;
}

void mavlink_set_global_position_setpoint_int_unpack(struct set_global_position_setpoint_int_msg* n_var0,
                                                     const uint8_t* n_var1)
{
    int32_t n_r0 = mavlink_unpack_int32_t(n_var1, 0U);
    
    *&n_var0->latitude = n_r0;
    
    int32_t n_r1 = mavlink_unpack_int32_t(n_var1, 4U);
    
    *&n_var0->longitude = n_r1;
    
    int32_t n_r2 = mavlink_unpack_int32_t(n_var1, 8U);
    
    *&n_var0->altitude = n_r2;
    
    int16_t n_r3 = mavlink_unpack_int16_t(n_var1, 12U);
    
    *&n_var0->yaw = n_r3;
    
    uint8_t n_r4 = mavlink_unpack_uint8_t(n_var1, 14U);
    
    *&n_var0->coordinate_frame = n_r4;
}