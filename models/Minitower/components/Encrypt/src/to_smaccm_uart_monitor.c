/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.3
 */
#include "to_smaccm_uart_monitor.h"

void callback_to_smaccm_uart_2(const struct ivory_string_UartPacket *n_var0)
{
    int32_t n_deref0 = n_var0->ivory_string_UartPacket_len;
    struct SMACCM_DATA__UART_Packet_i n_local1 = {.buf_len =n_deref0};
    struct SMACCM_DATA__UART_Packet_i *n_ref2 = &n_local1;
    
    if (n_ref2->buf != n_var0->ivory_string_UartPacket_data) {
        memcpy(n_ref2->buf, n_var0->ivory_string_UartPacket_data, sizeof(uint8_t [255U]));
    } else {
        COMPILER_ASSERTS(false);
    }
    callback_output_Encrypt_write_self2uart_2(n_ref2);
}