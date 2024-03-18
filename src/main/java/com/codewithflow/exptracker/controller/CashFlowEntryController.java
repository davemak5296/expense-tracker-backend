package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.newEntryReqDTO;
import com.codewithflow.exptracker.dto.updateEntryReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import com.codewithflow.exptracker.service.CashFlowEntryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class CashFlowEntryController {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final CashFlowEntryService cashFlowEntryService;
    private final ModelMapper modelMapper;

    public CashFlowEntryController(
            HttpServletRequest request,
            HttpServletResponse response,
            CashFlowEntryService cashFlowEntryService,
            ModelMapper modelMapper
    ) {
        this.request = request;
        this.response = response;
        this.cashFlowEntryService = cashFlowEntryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/entry")
    @ResponseStatus(HttpStatus.CREATED)
    public CashFlowEntryRespDTO createCashFlowEntry(
            @Valid @RequestBody newEntryReqDTO newEntryDTO,
            BindingResult bindingResult
    ) throws ParseException, MethodArgumentNotValidException {
        return cashFlowEntryService.createNewEntry(newEntryDTO, Long.parseLong(request.getParameter("jwt_user_id")), bindingResult);
    }

    @PutMapping("/entry/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    public CashFlowEntryRespDTO updateCashFlowEntry(
       @PathVariable Long entryId,
       @Valid @RequestBody updateEntryReqDTO updatedEntry,
       BindingResult bindingResult
    ) throws MethodArgumentNotValidException {
        return cashFlowEntryService.updateEntry(updatedEntry, entryId, Long.parseLong(request.getParameter("jwt_user_id")), bindingResult);
    }
}