/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "mavlink_statustext_msg.h"

void mavlink_statustext_msg_send(const struct statustext_msg* n_var0,
                                 uint8_t* n_var1, uint8_t n_var2[80U])
{
    uint8_t n_local0[51U] = {};
    uint8_t* n_ref1 = n_local0;
    uint8_t n_deref2 = n_var0->severity;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 0U, n_deref2);
    
    const uint8_t* n_let3 = n_var0->text;
    
    for (int32_t n_ix4 = 0; n_ix4 <= 49; n_ix4++) {
        uint8_t n_deref5 = n_let3[n_ix4];
        
        mavlink_pack_uint8_t((uint8_t*) n_ref1, 1U + (uint8_t) n_ix4, n_deref5);
    }
    for (int32_t n_ix6 = 0; n_ix6 <= 50; n_ix6++) {
        ASSERTS(n_ix6 > 0 && 2147483647 - n_ix6 >= 6 || n_ix6 <= 0);
        if (n_ix6 + 6 >= 80) { } else {
            uint8_t n_deref7 = n_ref1[n_ix6];
            
            ASSERTS(n_ix6 > 0 && 2147483641 >= n_ix6 || n_ix6 <= 0);
            ASSERTS(0 <= 6 + n_ix6 && 6 + n_ix6 < 80);
            *&n_var2[(6 + n_ix6) % 80] = n_deref7;
        }
    }
    mavlinkSendWithWriter(253U, 83U, 51U, n_var1, n_var2);
    for (int32_t n_ix8 = 0; n_ix8 <= 20; n_ix8++) {
        ASSERTS(n_ix8 > 0 && 2147483647 - n_ix8 >= 59 || n_ix8 <= 0);
        ASSERTS(0 <= n_ix8 + 59 && n_ix8 + 59 < 80);
        *&n_var2[(n_ix8 + 59) % 80] = 0U;
    }
    return;
}

void mavlink_statustext_unpack(struct statustext_msg* n_var0, const
                               uint8_t* n_var1)
{
    uint8_t n_r0 = mavlink_unpack_uint8_t(n_var1, 0U);
    
    *&n_var0->severity = n_r0;
    for (int32_t n_ix1 = 0; n_ix1 <= 49; n_ix1++) {
        uint8_t n_r2 = mavlink_unpack_uint8_t(n_var1, 1U + (uint8_t) n_ix1);
        
        *&n_var0->text[n_ix1] = n_r2;
    }
}