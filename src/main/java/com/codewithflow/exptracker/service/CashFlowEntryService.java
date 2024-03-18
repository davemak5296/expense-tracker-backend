package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.newEntryReqDTO;
import com.codewithflow.exptracker.dto.updateEntryReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.text.ParseException;

public interface CashFlowEntryService {

    CashFlowEntryRespDTO createNewEntry(
            newEntryReqDTO reqDTO,
            Long jwtUserId,
            BindingResult bindingResult
    ) throws ParseException, MethodArgumentNotValidException;

    CashFlowEntryRespDTO updateEntry(
            updateEntryReqDTO reqDTO,
            Long entryId,
            Long jwtUserId,
            BindingResult bindingResult
    ) throws MethodArgumentNotValidException;
}
