/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "mavlink_param_request_read_msg.h"

void mavlink_param_request_read_msg_send(const
                                         struct param_request_read_msg* n_var0,
                                         uint8_t* n_var1, uint8_t n_var2[80U])
{
    uint8_t n_local0[20U] = {};
    uint8_t* n_ref1 = n_local0;
    int16_t n_deref2 = n_var0->param_index;
    
    mavlink_pack_int16_t((uint8_t*) n_ref1, 0U, n_deref2);
    
    uint8_t n_deref3 = n_var0->target_system;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 2U, n_deref3);
    
    uint8_t n_deref4 = n_var0->target_component;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 3U, n_deref4);
    
    const uint8_t* n_let5 = n_var0->param_id;
    
    for (int32_t n_ix6 = 0; n_ix6 <= 15; n_ix6++) {
        uint8_t n_deref7 = n_let5[n_ix6];
        
        mavlink_pack_uint8_t((uint8_t*) n_ref1, 4U + (uint8_t) n_ix6, n_deref7);
    }
    for (int32_t n_ix8 = 0; n_ix8 <= 19; n_ix8++) {
        ASSERTS(n_ix8 > 0 && 2147483647 - n_ix8 >= 6 || n_ix8 <= 0);
        if (n_ix8 + 6 >= 80) { } else {
            uint8_t n_deref9 = n_ref1[n_ix8];
            
            ASSERTS(n_ix8 > 0 && 2147483641 >= n_ix8 || n_ix8 <= 0);
            ASSERTS(0 <= 6 + n_ix8 && 6 + n_ix8 < 80);
            *&n_var2[(6 + n_ix8) % 80] = n_deref9;
        }
    }
    mavlinkSendWithWriter(20U, 214U, 20U, n_var1, n_var2);
    for (int32_t n_ix10 = 0; n_ix10 <= 51; n_ix10++) {
        ASSERTS(n_ix10 > 0 && 2147483647 - n_ix10 >= 28 || n_ix10 <= 0);
        ASSERTS(0 <= n_ix10 + 28 && n_ix10 + 28 < 80);
        *&n_var2[(n_ix10 + 28) % 80] = 0U;
    }
    return;
}

void mavlink_param_request_read_unpack(struct param_request_read_msg* n_var0,
                                       const uint8_t* n_var1)
{
    int16_t n_r0 = mavlink_unpack_int16_t(n_var1, 0U);
    
    *&n_var0->param_index = n_r0;
    
    uint8_t n_r1 = mavlink_unpack_uint8_t(n_var1, 2U);
    
    *&n_var0->target_system = n_r1;
    
    uint8_t n_r2 = mavlink_unpack_uint8_t(n_var1, 3U);
    
    *&n_var0->target_component = n_r2;
    for (int32_t n_ix3 = 0; n_ix3 <= 15; n_ix3++) {
        uint8_t n_r4 = mavlink_unpack_uint8_t(n_var1, 4U + (uint8_t) n_ix3);
        
        *&n_var0->param_id[n_ix3] = n_r4;
    }
}