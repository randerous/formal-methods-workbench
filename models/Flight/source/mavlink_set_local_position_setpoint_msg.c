/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "mavlink_set_local_position_setpoint_msg.h"

void mavlink_set_local_position_setpoint_msg_send(const
                                                  struct set_local_position_setpoint_msg* n_var0,
                                                  uint8_t* n_var1,
                                                  uint8_t n_var2[80U])
{
    uint8_t n_local0[19U] = {};
    uint8_t* n_ref1 = n_local0;
    float n_deref2 = n_var0->x;
    
    mavlink_pack_float((uint8_t*) n_ref1, 0U, n_deref2);
    
    float n_deref3 = n_var0->y;
    
    mavlink_pack_float((uint8_t*) n_ref1, 4U, n_deref3);
    
    float n_deref4 = n_var0->z;
    
    mavlink_pack_float((uint8_t*) n_ref1, 8U, n_deref4);
    
    float n_deref5 = n_var0->yaw;
    
    mavlink_pack_float((uint8_t*) n_ref1, 12U, n_deref5);
    
    uint8_t n_deref6 = n_var0->target_system;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 16U, n_deref6);
    
    uint8_t n_deref7 = n_var0->target_component;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 17U, n_deref7);
    
    uint8_t n_deref8 = n_var0->coordinate_frame;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 18U, n_deref8);
    for (int32_t n_ix9 = 0; n_ix9 <= 18; n_ix9++) {
        ASSERTS(n_ix9 > 0 && 2147483647 - n_ix9 >= 6 || n_ix9 <= 0);
        if (n_ix9 + 6 >= 80) { } else {
            uint8_t n_deref10 = n_ref1[n_ix9];
            
            ASSERTS(n_ix9 > 0 && 2147483641 >= n_ix9 || n_ix9 <= 0);
            ASSERTS(0 <= 6 + n_ix9 && 6 + n_ix9 < 80);
            *&n_var2[(6 + n_ix9) % 80] = n_deref10;
        }
    }
    mavlinkSendWithWriter(50U, 214U, 19U, n_var1, n_var2);
    for (int32_t n_ix11 = 0; n_ix11 <= 52; n_ix11++) {
        ASSERTS(n_ix11 > 0 && 2147483647 - n_ix11 >= 27 || n_ix11 <= 0);
        ASSERTS(0 <= n_ix11 + 27 && n_ix11 + 27 < 80);
        *&n_var2[(n_ix11 + 27) % 80] = 0U;
    }
    return;
}

void mavlink_set_local_position_setpoint_unpack(struct set_local_position_setpoint_msg* n_var0,
                                                const uint8_t* n_var1)
{
    float n_r0 = mavlink_unpack_float(n_var1, 0U);
    
    *&n_var0->x = n_r0;
    
    float n_r1 = mavlink_unpack_float(n_var1, 4U);
    
    *&n_var0->y = n_r1;
    
    float n_r2 = mavlink_unpack_float(n_var1, 8U);
    
    *&n_var0->z = n_r2;
    
    float n_r3 = mavlink_unpack_float(n_var1, 12U);
    
    *&n_var0->yaw = n_r3;
    
    uint8_t n_r4 = mavlink_unpack_uint8_t(n_var1, 16U);
    
    *&n_var0->target_system = n_r4;
    
    uint8_t n_r5 = mavlink_unpack_uint8_t(n_var1, 17U);
    
    *&n_var0->target_component = n_r5;
    
    uint8_t n_r6 = mavlink_unpack_uint8_t(n_var1, 18U);
    
    *&n_var0->coordinate_frame = n_r6;
}