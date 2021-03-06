/**************************************************************************
  Copyright (c) 2013, 2014, 2015 Rockwell Collins and the University of Minnesota.
  Developed with the sponsorship of the Defense Advanced Research Projects Agency (DARPA).

  Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
  including any software or models in source or binary form, as well as any drawings, specifications, 
  and documentation (collectively "the Data"), to deal in the Data without restriction, including 
  without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
  and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
  subject to the following conditions: 

  The above copyright notice and this permission notice shall be included in all copies or
  substantial portions of the Data.

  THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
  LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
  IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
  FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
  ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA. 

 **************************************************************************/


/**************************************************************************

   File: C:\Users\jdbackes\git\smaccm\models\Trusted_Build_Test\test_uart_active\components\sender\src\smaccm_sender.c
   Created on: 2015/04/09 14:16:46
   using Dulcimer AADL system build tool suite 

   ***AUTOGENERATED CODE: DO NOT MODIFY***

  This C file contains the implementations of the AADL primitives
  used by user-level declarations for thread sender.   

  The user code runs in terms of "dispatchers", which cause 
  dispatch user-level handlers to execute.  These handlers can 
  communicate using the standard AADL primitives, which are mapped
  to C functions.

  The send/receive handlers are not thread safe in CAmkES; it is 
  assumed that this is handled by the CAmkES sequentialized access 
  to the dispatch handler.  There is only one dispatch interface 
  for the component containing all of the dispatch points.

  They are thread safe for eChronos.

  The read/write handlers are thread safe because the writer comes 
  through a dispatch interface but the reader is "local" on a dispatch
  interface and so contention may occur.


 **************************************************************************/


#include <string.h>
#include <smaccm_sender.h>
#include <sender.h>

///////////////////////////////////////////////////////////////////////////
//
// Local prototypes for AADL dispatchers
//
///////////////////////////////////////////////////////////////////////////
void 
smaccm_dispatcher_periodic_100_ms(uint64_t * periodic_100_ms); 
 



test_uart__uart_packet_impl * smaccm_tmp_array_Output1; 
uint32_t * smaccm_tmp_used_Output1;
uint32_t smaccm_max_tmp_array_Output1; 	


/************************************************************************
 * periodic_100_ms Declarations
 * 
 ************************************************************************/

bool smaccm_occurred_periodic_100_ms;
uint64_t smaccm_time_periodic_100_ms;

/************************************************************************
 * sender_periodic_100_ms_write_uint64_t
 * Invoked from remote periodic dispatch thread.
 * 
 * This function records the current time and triggers the active thread 
 * dispatch from a periodic event.
 * 
 ************************************************************************/

bool sender_periodic_100_ms_write_uint64_t(const uint64_t * arg) {
	smaccm_occurred_periodic_100_ms = true;
	smaccm_time_periodic_100_ms = *arg;
	smaccm_dispatch_sem_post();

	return true;
}



/************************************************************************
 *  dispatch_dispatch_periodic_100_ms: 
 * Invoked by remote RPC (or, for active thread, local dispatcher).
 * 
 * This is the function invoked by an active thread dispatcher to 
 * call to a user-defined entrypoint function.  It sets up the dispatch
 * context for the user-defined entrypoint, then calls it.
 * 
 ************************************************************************/

void dispatch_dispatch_periodic_100_ms(
const uint64_t * periodic_100_ms ,
  smaccm_test_uart__uart_packet_impl_struct_1 *Output1_data, uint32_t * Output1_index 
) {
	smaccm_max_tmp_array_Output1 = 1;
	smaccm_tmp_used_Output1 = Output1_index;
	smaccm_tmp_array_Output1 = Output1_data->f; 
	*smaccm_tmp_used_Output1 = 0; 

	periodic_ping(periodic_100_ms); 

}	



/************************************************************************
 * ping_Output1 
 * Invoked from local active or passive thread.
 * 
 * This is the comm function invoked by a user-level thread to send a message 
 * to another thread.  It enqueues the request to be sent when the user thread
 * completes execution.
 * 
 ************************************************************************/

bool ping_Output1(const test_uart__uart_packet_impl * Output1) {
	if (smaccm_max_tmp_array_Output1 > 0 && 
		 *smaccm_tmp_used_Output1 < smaccm_max_tmp_array_Output1) {
		memcpy(&smaccm_tmp_array_Output1[*smaccm_tmp_used_Output1], Output1, sizeof(test_uart__uart_packet_impl));

		(*smaccm_tmp_used_Output1)++;
		return true;
	} else {
		return false;
	}
}


/************************************************************************
 * 
smaccm_dispatcher_periodic_100_ms
 * Invoked from local active thread.
 * 
 * This is the dispatcher function invoked to respond to an incoming thread 
 * stimulus: an ISR, a periodic dispatch, or a queued event.
 * 
 ******************************************************************************/
void 
smaccm_dispatcher_periodic_100_ms(uint64_t * periodic_100_ms) {
	smaccm_test_uart__uart_packet_impl_struct_1 Output1_data ;
	uint32_t Output1_index;


	 
	// make the call: 
	dispatch_dispatch_periodic_100_ms(  periodic_100_ms, 
 		&Output1_data,
 		&Output1_index  
	
		); 
	 

	// call the aadl dispatchers for any generated output events.
	// to prevent misuse by malicious clients, we ensure that the number 
	// of dispatches is less than the maximum allowed dispatch count.
	uint32_t smaccm_it; 
	for (smaccm_it = 0; smaccm_it < Output1_index && smaccm_it < 1; smaccm_it++) {
	   // Calling an active dispatch target.
	   sender_Output1_write_test_uart__uart_packet_impl(
	   (&(Output1_data.f[smaccm_it]))
	   );	

	}

}
 
/************************************************************************
 * run()
 * Main active thread function.
 * 
 ************************************************************************/

int run() {
	// port initialization routines (if any)... 

	// thread initialization routines (if any)...

	// register interrupt handlers (if any)...
	 

	// initial lock to await dispatch input.
	smaccm_dispatch_sem_wait();

	for(;;) {
		smaccm_dispatch_sem_wait();


		// drain the queues 
		if (smaccm_occurred_periodic_100_ms) {
			smaccm_occurred_periodic_100_ms = false;

			smaccm_dispatcher_periodic_100_ms(&smaccm_time_periodic_100_ms);
		}


	}
	// won't ever get here, but form must be followed
	return 0;
}



/**************************************************************************
  End of autogenerated file: C:\Users\jdbackes\git\smaccm\models\Trusted_Build_Test\test_uart_active\components\sender\src\smaccm_sender.c
 **************************************************************************/
