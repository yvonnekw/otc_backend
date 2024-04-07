package com.otc.backend.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import com.otc.backend.dto.CallDto;
import com.otc.backend.models.Call;

public interface CallService {

    public Call getCallById(Long callId);

    public List<Call> getAllCalls();
    

    public Call updateCall(Long callId, Call updatedCall);

    public void deleteCall(Long callId);


    public Call makeCall(String username, String telephone, CallDto callsDTO);

    public List<Call> getCallsByUsername(String username);

    //public List<Call> getUnpaidCallsByUsername(String username);

   // void endCallsAndGenerateInvoice(List<Call> calls);

   //CompletedCallDto convertToCompletedCallDto(CurrentCall currentCall);
    
   //BigDecimal calculateNetCost(Set<Call> calls);

   BigDecimal calculateTotalAmount(Set<Call> calls);

    //double calculateTotalCost(List<Call> calls);

   // public void endCallsAndGenerateInvoice(List<Call> calls);
  // void endCallsAndGenerateInvoice(List<Call> calls, String username, InvoiceWithCallIdsDTO invoiceWithCallIdsDTO);

}
