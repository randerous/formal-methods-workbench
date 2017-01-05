/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.4
 */
#include "reboot_magic_types.h"

void reboot_magic_t_get_le(const uint8_t *n_var0, uint32_t n_var1, uint32_t *n_var2)
{
    uint32_t n_local0 = (uint32_t) 0U;
    uint32_t *n_ref1 = &n_local0;
    
    ivory_serialize_unpack_uint32_le(n_var0, n_var1, n_ref1);
    
    uint32_t n_deref2 = *n_ref1;
    
    *n_var2 = n_deref2;
}

void reboot_magic_t_get_be(const uint8_t *n_var0, uint32_t n_var1, uint32_t *n_var2)
{
    uint32_t n_local0 = (uint32_t) 0U;
    uint32_t *n_ref1 = &n_local0;
    
    ivory_serialize_unpack_uint32_be(n_var0, n_var1, n_ref1);
    
    uint32_t n_deref2 = *n_ref1;
    
    *n_var2 = n_deref2;
}

void reboot_magic_t_set_le(uint8_t *n_var0, uint32_t n_var1, const uint32_t *n_var2)
{
    uint32_t n_local0 = (uint32_t) 0U;
    uint32_t *n_ref1 = &n_local0;
    uint32_t n_deref2 = *n_var2;
    
    *n_ref1 = n_deref2;
    ivory_serialize_pack_uint32_le(n_var0, n_var1, n_ref1);
}

void reboot_magic_t_set_be(uint8_t *n_var0, uint32_t n_var1, const uint32_t *n_var2)
{
    uint32_t n_local0 = (uint32_t) 0U;
    uint32_t *n_ref1 = &n_local0;
    uint32_t n_deref2 = *n_var2;
    
    *n_ref1 = n_deref2;
    ivory_serialize_pack_uint32_be(n_var0, n_var1, n_ref1);
}
