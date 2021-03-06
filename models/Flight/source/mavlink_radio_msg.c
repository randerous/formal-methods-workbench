/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "mavlink_radio_msg.h"

void mavlink_radio_msg_send(const struct radio_msg* n_var0, uint8_t* n_var1,
                            uint8_t n_var2[80U])
{
    uint8_t n_local0[9U] = {};
    uint8_t* n_ref1 = n_local0;
    uint16_t n_deref2 = n_var0->rxerrors;
    
    mavlink_pack_uint16_t((uint8_t*) n_ref1, 0U, n_deref2);
    
    uint16_t n_deref3 = n_var0->fixed;
    
    mavlink_pack_uint16_t((uint8_t*) n_ref1, 2U, n_deref3);
    
    uint8_t n_deref4 = n_var0->rssi;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 4U, n_deref4);
    
    uint8_t n_deref5 = n_var0->remrssi;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 5U, n_deref5);
    
    uint8_t n_deref6 = n_var0->txbuf;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 6U, n_deref6);
    
    uint8_t n_deref7 = n_var0->noise;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 7U, n_deref7);
    
    uint8_t n_deref8 = n_var0->remnoise;
    
    mavlink_pack_uint8_t((uint8_t*) n_ref1, 8U, n_deref8);
    for (int32_t n_ix9 = 0; n_ix9 <= 8; n_ix9++) {
        ASSERTS(n_ix9 > 0 && 2147483647 - n_ix9 >= 6 || n_ix9 <= 0);
        if (n_ix9 + 6 >= 80) { } else {
            uint8_t n_deref10 = n_ref1[n_ix9];
            
            ASSERTS(n_ix9 > 0 && 2147483641 >= n_ix9 || n_ix9 <= 0);
            ASSERTS(0 <= 6 + n_ix9 && 6 + n_ix9 < 80);
            *&n_var2[(6 + n_ix9) % 80] = n_deref10;
        }
    }
    mavlinkSendWithWriter(166U, 21U, 9U, n_var1, n_var2);
    for (int32_t n_ix11 = 0; n_ix11 <= 62; n_ix11++) {
        ASSERTS(n_ix11 > 0 && 2147483647 - n_ix11 >= 17 || n_ix11 <= 0);
        ASSERTS(0 <= n_ix11 + 17 && n_ix11 + 17 < 80);
        *&n_var2[(n_ix11 + 17) % 80] = 0U;
    }
    return;
}

void mavlink_radio_unpack(struct radio_msg* n_var0, const uint8_t* n_var1)
{
    uint16_t n_r0 = mavlink_unpack_uint16_t(n_var1, 0U);
    
    *&n_var0->rxerrors = n_r0;
    
    uint16_t n_r1 = mavlink_unpack_uint16_t(n_var1, 2U);
    
    *&n_var0->fixed = n_r1;
    
    uint8_t n_r2 = mavlink_unpack_uint8_t(n_var1, 4U);
    
    *&n_var0->rssi = n_r2;
    
    uint8_t n_r3 = mavlink_unpack_uint8_t(n_var1, 5U);
    
    *&n_var0->remrssi = n_r3;
    
    uint8_t n_r4 = mavlink_unpack_uint8_t(n_var1, 6U);
    
    *&n_var0->txbuf = n_r4;
    
    uint8_t n_r5 = mavlink_unpack_uint8_t(n_var1, 7U);
    
    *&n_var0->noise = n_r5;
    
    uint8_t n_r6 = mavlink_unpack_uint8_t(n_var1, 8U);
    
    *&n_var0->remnoise = n_r6;
}