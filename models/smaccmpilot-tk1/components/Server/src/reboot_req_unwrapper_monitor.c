/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.5
 */
#include "reboot_req_unwrapper_monitor.h"

void callback_reboot_vm(const struct sequence_numbered_reboot_req *n_var0)
{
    bool n_local0 = true;
    bool *n_ref1 = &n_local0;
    
    callback_output_tb_self2vm_reboot_enqueue_2(n_ref1);
}