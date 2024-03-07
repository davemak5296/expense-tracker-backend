package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.CashFlowEntryPostReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import com.codewithflow.exptracker.service.CashFlowEntryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
            @Valid @RequestBody CashFlowEntryPostReqDTO newEntryDTO
    ) throws ParseException {
        return cashFlowEntryService.createNewEntry(newEntryDTO, request);
    }
}