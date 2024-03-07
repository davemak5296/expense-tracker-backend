package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.CashFlowEntryPostReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public interface CashFlowEntryService {

    CashFlowEntryRespDTO createNewEntry(CashFlowEntryPostReqDTO reqDTO, HttpServletRequest request) throws ParseException;

}
