package com.otc.backend.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otc.backend.dto.CallDto;
import com.otc.backend.exception.CallCreationException;
import com.otc.backend.exception.CallReceiverNotFoundException;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Call;
import com.otc.backend.models.CallReceiver;

import com.otc.backend.models.Invoice;
import com.otc.backend.models.Users;
import com.otc.backend.publisher.RabbitMQJsonProducer;
import com.otc.backend.publisher.RabbitMQProducer;
import com.otc.backend.models.Role;
import com.otc.backend.repository.CallReceiverRepository;
import com.otc.backend.repository.CallRepository;
//import opticaltelephonecompany.otc.repository.CurrentCallRepository;
import com.otc.backend.repository.InvoiceRepository;
import com.otc.backend.repository.UserRepository;

@Service
public class CallServiceImpl implements CallService {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);
    BigDecimal netCost;
    private final CallRepository callRepository;
    private final UserRepository userRepository;
    private final CallReceiverRepository callReceiverRepository;
   // private final InvoiceService invoiceService;
    //private final CurrentCallRepository currentCallRepository;
    private final RabbitMQJsonProducer rabbitMQJsonProducer;
     //private final InvoiceRepository invoiceRepository;

    public CallServiceImpl(CallRepository callsRepository, UserRepository userRepository,
            CallReceiverRepository callReceiverRepository, 
             RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.callRepository = callsRepository;
        this.userRepository = userRepository;
        this.callReceiverRepository = callReceiverRepository;
       // this.invoiceService = invoiceService;
       // this.currentCallRepository = currentCallRepository;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
       // this.invoiceRepository = invoiceRepository;
    }

    public Call getCallById(Long callId) {
        return callRepository.findById(callId).orElseThrow(UserDoesNotExistException::new);
    }

    public List<Call> getAllCalls() {
        return callRepository.findAll();
    }

    public Call updateCall(Long callId, Call updatedCall) {

        Call call = callRepository.findById(callId).orElseThrow(
                () -> new ResourceNotFoundException("Call not found with the given Id : " + callId));
        call.setStartTime(updatedCall.getStartTime());
        call.setEndTime(updatedCall.getEndTime());

        Call updatedCallObj = callRepository.save(call);
        return updatedCallObj;
    }

    public void deleteCall(Long callId) {
        Users user = userRepository.findById(callId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with the given Id : " + callId));

        callRepository.deleteById(callId);
    }

    public Call makeCall(String username, String telephone, CallDto callsDTO) {
        try {
            Call call = new Call();
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserDoesNotExistException());

            CallReceiver callReceiver = callReceiverRepository.findByTelephone(telephone)
                    .orElseThrow(() -> new CallReceiverNotFoundException());

            // Parse start and end times into LocalTime objects
            LocalTime startTime = LocalTime.parse(callsDTO.getStartTime());
            LocalTime endTime = LocalTime.parse(callsDTO.getEndTime());

            // Calculate the duration between start and end times
            // long durationSeconds = Duration.between(startTime, endTime).getSeconds();
            long durationSeconds = call.calculateDurationInSeconds(startTime, endTime);

            System.out.println("duration in seconds " + durationSeconds);

            String discount = callsDTO.getDiscountForCalls();

            // Convert costPerSecond to BigDecimal
           // BigDecimal costPerSecondDecimal = new BigDecimal(call.getCostPerSecond());

            //System.out.println("cost per seconds " + costPerSecondDecimal);

            // Calculate the net cost of the call
           // BigDecimal netCost = costPerSecondDecimal
                   // .multiply(BigDecimal.valueOf(durationSeconds))
                   // .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
            netCost = call.calculateNetCost(durationSeconds);

            // Apply discount if available
            BigDecimal netCostAfterDiscount = call.applyDiscount(netCost, discount);

            // Calculate VAT amount
            BigDecimal vatAmount = call.calculateVATAmount(netCostAfterDiscount);

            System.out.println("vat amount " + vatAmount);

            // Calculate gross cost
            BigDecimal grossCost = call.calculateGrossCost(netCostAfterDiscount, vatAmount);

            String callDate = LocalDateTime.now().toString();

            String costPerSecond = call.getCostPerSecond();

            System.out.println("costPerSecond from makecall " + costPerSecond);

            // Create the Call entity and set its properties
            Call enterCall = new Call(startTime.toString(), endTime.toString(), String.valueOf(durationSeconds),
                    costPerSecond, callsDTO.getDiscountForCalls(), vatAmount.toString(),
                    netCostAfterDiscount.toString(), grossCost.toString(), 
                    callDate, "Pending Invoice",
                    user, callReceiver);

            // Save the call entity
            return callRepository.save(enterCall);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            throw new CallCreationException();
        }
    }

    /* 
    public Call makeCall(String username, String telephone, CallDto callsDTO) {
        try {
            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserDoesNotExistException());

            CallReceiver callReceiver = callReceiverRepository.findByTelephone(telephone)
                    .orElseThrow(() -> new CallReceiverNotFoundException());

            // Parse start and end times into LocalTime objects
            LocalTime startTime = LocalTime.parse(callsDTO.getStartTime());
            LocalTime endTime = LocalTime.parse(callsDTO.getEndTime());

            // Calculate the duration between start and end times
           // Duration duration = Duration.between(startTime, endTime);

            // Convert the duration to the desired format, e.g., minutes
           // long durationMinutes = duration.toMinutes();

          //  CurrentCall currentCall = new CurrentCall();
           // currentCall.setStartTime(callsDTO.getStartTime());
           // currentCall.setEndTime(callsDTO.getEndTime());
           // currentCall.setUser(user);

            // Save the current call to get the currentCallId
            //currentCall = currentCallRepository.save(currentCall);

          

            Call call = new Call();

            call.setStartTime(callsDTO.getStartTime());
            call.setEndTime(callsDTO.getEndTime());
           // call.setDuration(String.valueOf(durationMinutes)); // Set the duration in minutes
            //call.setCostPerMinute(callsDTO.getCostPerMinute());
            call.setDiscountForCalls(callsDTO.getDiscountForCalls());
            call.setCallDate(LocalDateTime.now().toString());
           // call.setVat(callsDTO.getVat());
          //  call.setNetCost(callsDTO.getNetCost());
          //  call.setGrossCost(callsDTO.getGrossCost());
           // call.setTotalCost(callsDTO.getTotalCost());
            call.setUser(user);
            call.setStatus("Pending Invoice");
            call.setReceiver(callReceiver);
            // call.setCurrentCall(currentCall); // Associate the Call with the CurrentCall
           
            System.out.println("call data to set: in call service " + call);

            //call.calculateCallCost();

            // Save the call to set the currentCallId
            call = callRepository.save(call);

            System.out.println("Call details: " + call);
            System.out.println("User details: " + user);

            return call;
        } catch (UserDoesNotExistException | CallReceiverNotFoundException e) {
            throw e; // Rethrow the exception to be handled at a higher level
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            throw new CallCreationException();
        }
    }                                               
    
    */
    public List<Call> getCallsByUsername(String username) {
        return callRepository.findByUser_Username(username);
    }

    public BigDecimal calculateTotalAmount(Set<Call> calls) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Call call : calls) {
            //BigDecimal netCostString = netCost;
            if (netCost != null) {
                //BigDecimal netCost = new BigDecimal(netCostString);
                totalAmount = totalAmount.add(netCost);
            } else {
                // Handle null net cost gracefully, e.g., log a warning
                logger.warn("Null net cost encountered for call: {}", call);
            }
        }
        logger.info("Total amount: {}", totalAmount);
        return totalAmount;
    }

    
/* 
@Override
public List<Call> getUnpaidCallsByUsername(String username) {
return callRepository.findUnpaidCallsByUsername(username);
}
*/


    /* 
    public BigDecimal calculateTotalAmount(Set<Call> calls) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Call call : calls) {
            // Add the netCost of each call to the total amount
            BigDecimal netCost = new BigDecimal(call.getNetCost());
            totalAmount = totalAmount.add(netCost);
        }
        logger.info("totalAmount {}" + totalAmount);
        return totalAmount;
  
    
   }*/

   // public void processPaymentForCall(Call call) {
        // Logic to retrieve the invoice associated with the call
       // Invoice invoice = call.getInvoice(); // Assuming each call has an associated invoice

        // Process payment for the invoice
       // invoiceService.processPaymentForInvoice(invoice);
   // }
/* 
      // Helper method to calculate the total amount from a set of calls
    private BigDecimal calculateTotalAmount(Set<Call> calls) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Call call : calls) {
            // Add the netCost of each call to the total amount
            totalAmount = totalAmount.add(new BigDecimal(call.getNetCost()));
        }
        return totalAmount;
    }
*/
    /* 
    @Override
    public void endCallsAndGenerateInvoice(List<Call> calls) {
        for (Call call : calls) {
            // Validate the end time of the call
            LocalDateTime startDateTime = LocalDateTime.parse(call.getStartTime());
            LocalDateTime endDateTime = LocalDateTime.parse(call.getEndTime());
            long durationMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
            if (durationMinutes < 0) {
                throw new IllegalStateException("End time is before start time for call: " + call.getCallId());
            }
    
            // Update the duration for the call
            call.setDuration(String.valueOf(durationMinutes));
    
            // Update the end time for the current call's associated CurrentCall
            //if (call.getCurrentCall() != null) {
                call.getCurrentCall().setEndTime(call.getEndTime());
            }
        }
    
        // Save the updated calls to the database
        callRepository.saveAll(calls);
    
        // Trigger invoice generation event
        InvoiceWithCallIdsDTO invoiceWithCallIdsDTO = new InvoiceWithCallIdsDTO();
        InvoiceGenerationEvent invoiceEvent = new InvoiceGenerationEvent();
        invoiceEvent.setUsername(calls.get(0).getUser().getUsername()); // Assuming all calls belong to the same user
        invoiceEvent.setInvoiceWithCallIdsDTO(invoiceWithCallIdsDTO);
        rabbitMQJsonProducer.sendJsonMessage(invoiceEvent);
    }
    
    */
    
    /* 
    public CompletedCallDto convertToCompletedCallDto(CurrentCall currentCall) {
        CompletedCallDto completedCallDto = new CompletedCallDto();
        completedCallDto.setCurrentCallId(currentCall.getCurrentCallId());
        completedCallDto.setStartTime(currentCall.getStartTime());
        completedCallDto.setEndTime(currentCall.getEndTime());
        // Set other properties as needed, such as user information

        return completedCallDto;
    }

    */
    


    // Helper method to calculate the net cost for the calls associated with the
    // current call

    /* 
@Override
public void endCallsAndGenerateInvoice(List<Call> calls) {
    // Get the current time as the end time for all calls
    LocalDateTime endTime = LocalDateTime.now();

    for (Call call : calls) {
        // Parse the start time of the call
        LocalDateTime startDateTime = LocalDateTime.parse(call.getStartTime());

        // Calculate the duration of the call in minutes
        long durationMinutes = Duration.between(startDateTime, endTime).toMinutes();

        // Validate the call duration
        if (durationMinutes < 0) {
            throw new IllegalStateException("End time is before start time for call: " + call.getCallId());
        }

        // Update the end time and duration for the call
        call.setEndTime(endTime.toString());
        call.setDuration(String.valueOf(durationMinutes));

        // Update the end time for the current call if it exists
        if (call.getCurrentCall() != null) {
            call.getCurrentCall().setEndTime(endTime.toString());
        }
    }

    // Save the updated calls to the database
    callRepository.saveAll(calls);

    // Trigger invoice generation event
    InvoiceWithCallIdsDTO invoiceWithCallIdsDTO = new InvoiceWithCallIdsDTO();
    // Set invoice details as needed
    InvoiceGenerationEvent invoiceEvent = new InvoiceGenerationEvent();
    invoiceEvent.setUsername(calls.get(0).getUser().getUsername()); // Assuming all calls belong to the same user
    invoiceEvent.setInvoiceWithCallIdsDTO(invoiceWithCallIdsDTO);
    rabbitMQJsonProducer.sendJsonMessage(invoiceEvent);
}*/
    

    /* 
     public List<Call> getPaidCalls() {
        return callRepository.findByInvoiceIsPaidTrue();
    }
    
    public List<Call> getUnpaidCalls() {
        return callRepository.findByInvoiceIsPaidFalse();
    }
    */
    
    /* 
    public List<Call> getUnpaidCallsByUsername(String username) {
        return callRepository.findByUserUsernameAndIsPaidFalse(username);
    }
    
    public List<Call> findPaidCallsByUsername(String username) {
        return callRepository.  findByInvoiceIsPaidTrue(username);
    }*/

    /* 
    public List<Call> getCallsForReceiver(String username) {
        return callRepository.findByReceiverUsername(username);
    }*/
    /* 
    public List<CallReceiver> getCallReceiversForUser(String username) {
        return callReceiverRepository.findByUsername(username);
    }*/
    /* 
    public List<Call> getAllCallsForUser(CallUser callUser) {
        return callRepository.findByCallUser(callUser);
    }*/

    /* 
    public List<Call> getCallReceiversForUser(String username) {
         return callRepository.findAllCallReceiverByUserName(username);
        //return null;
    }*/

    /* 
    public List<Call> getCallReceiversForUser(String username) {
    return callRepository.findByCallUserUsername(username);
    }*/
    /*  
    public List<CallReceiver> getCallReceiversForUser(String username) {
    return callRepository.findByCallUsersUsername(username);
    }*/
    /* 
    public List<CallReceiver> getCallReceiversForUser(String username) {
        // Assuming you have a method in your repository to fetch call receivers by
        // username
        CallUser callUser = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        return callReceiverRepository.findByCallUser(callUser);//.findByCallUser(username);
    }*/

    /*
     * public Call makeCall(String username, String telephone, CallDto callsDTO) {
     * 
     * try {
     * Users user = userRepository.findByUsername(username)
     * .orElseThrow(() -> new UserDoesNotExistException());
     * 
     * CallReceiver callReceiver = callReceiverRepository.findByTelephone(telephone)
     * .orElseThrow(() -> new CallReceiverNotFoundException());
     * 
     * CurrentCall currentCall = new CurrentCall();
     * 
     * currentCall.setStartTime(callsDTO.getStartTime());
     * currentCall.setEndTime(callsDTO.getEndTime());
     * currentCall.setUser(user);
     * 
     * 
     * // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
     * 
     * 
     * Call call = new Call();
     * 
     * //LocalDateTime endTime = LocalDateTime.parse(callsDTO.getEndTime(),
     * formatter);
     * 
     * // LocalDateTime setTime =
     * formatter.parseLocalDateTime(callsDTO.getEndTime());
     * 
     * String endTime = LocalDateTime.now().toString();
     * 
     * call.setStartTime(callsDTO.getStartTime());
     * call.setEndTime(callsDTO.getEndTime());
     * call.setDuration(callsDTO.getDuration());
     * // call.setSetEndTime(callsDTO.getEndTime());
     * call.setCostPerMinute(callsDTO.getCostPerMinute());
     * call.setDiscountForCalls(callsDTO.getDiscountForCalls());
     * call.setCallDate(callsDTO.getCallDate());
     * call.setVat(callsDTO.getVat());
     * call.setNetCost(callsDTO.getNetCost());
     * call.setGrossCost(callsDTO.getGrossCost());
     * call.setTotalCost(callsDTO.getTotalCost());
     * call.setUser(user);
     * call.setReceiver(callReceiver);
     * currentCall.getCalls().add(call);
     * 
     * currentCallRepository.save(currentCall);
     * 
     * // Uncomment the following line if you want to add CallUsers to the set
     * // callUsers.add(UserRepository.findByCallUser(callUsers).get());
     * 
     * System.out.println("Call details: " + call);
     * System.out.println("User details: " + user);
     * 
     * return callRepository.save(call);
     * } catch (UserDoesNotExistException | CallReceiverNotFoundException e) {
     * throw e; // Rethrow the exception to be handled at a higher level
     * } catch (Exception e) {
     * e.printStackTrace(); // Log the exception details
     * throw new CallCreationException();
     * }
     * 
     * }
     */

    /*
     * public Call makeCall(String username, String telephone, CallDto callsDTO) {
     * try {
     * Users user = userRepository.findByUsername(username)
     * .orElseThrow(() -> new UserDoesNotExistException());
     * 
     * CallReceiver callReceiver = callReceiverRepository.findByTelephone(telephone)
     * .orElseThrow(() -> new CallReceiverNotFoundException());
     * 
     * // Parse start and end times into LocalTime objects
     * LocalTime startTime = LocalTime.parse(callsDTO.getStartTime());
     * LocalTime endTime = LocalTime.parse(callsDTO.getEndTime());
     * 
     * // Calculate the duration between start and end times
     * Duration duration = Duration.between(startTime, endTime);
     * 
     * // Convert the duration to the desired format, e.g., minutes
     * long durationMinutes = duration.toMinutes();
     * 
     * CurrentCall currentCall = new CurrentCall();
     * currentCall.setStartTime(callsDTO.getStartTime());
     * currentCall.setEndTime(callsDTO.getEndTime());
     * currentCall.setUser(user);
     * 
     * Call call = new Call();
     * call.setStartTime(callsDTO.getStartTime());
     * call.setEndTime(callsDTO.getEndTime());
     * call.setDuration(String.valueOf(durationMinutes)); // Set the duration in
     * minutes
     * call.setCostPerMinute(callsDTO.getCostPerMinute());
     * call.setDiscountForCalls(callsDTO.getDiscountForCalls());
     * call.setCallDate(callsDTO.getCallDate());
     * call.setVat(callsDTO.getVat());
     * call.setNetCost(callsDTO.getNetCost());
     * call.setGrossCost(callsDTO.getGrossCost());
     * call.setTotalCost(callsDTO.getTotalCost());
     * call.setUser(user);
     * call.setReceiver(callReceiver);
     * currentCall.getCalls().add(call);
     * 
     * currentCallRepository.save(currentCall);
     * 
     * System.out.println("Call details: " + call);
     * System.out.println("User details: " + user);
     * 
     * return callRepository.save(call);
     * } catch (UserDoesNotExistException | CallReceiverNotFoundException e) {
     * throw e; // Rethrow the exception to be handled at a higher level
     * } catch (Exception e) {
     * e.printStackTrace(); // Log the exception details
     * throw new CallCreationException();
     * }
     * }
     * 
     */

}
