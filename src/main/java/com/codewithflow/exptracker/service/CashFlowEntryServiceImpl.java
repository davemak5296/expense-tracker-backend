package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.CashFlowEntryPostReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import com.codewithflow.exptracker.entity.CashFlowEntry;
import com.codewithflow.exptracker.repository.CashFlowEntryRepository;
import com.codewithflow.exptracker.util.exception.SubCategoryIdNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

@Service("cashFlowEntryService")
@Transactional
public class CashFlowEntryServiceImpl implements CashFlowEntryService{

    private final ModelMapper modelMapper;
    private final CashFlowEntryRepository cashFlowEntryRepository;

    public CashFlowEntryServiceImpl(
            ModelMapper modelMapper,
            CashFlowEntryRepository cashFlowEntryRepository
    ) {
        this.modelMapper = modelMapper;
        this.cashFlowEntryRepository = cashFlowEntryRepository;
    }

    @Override
    public CashFlowEntryRespDTO createNewEntry(
            CashFlowEntryPostReqDTO reqDTO,
            HttpServletRequest request
    ) throws ParseException {
        if (reqDTO.getSubCategoryUserId() != Long.parseLong(request.getParameter("jwt_user_id"))) {
            throw new SubCategoryIdNotMatchException("sub-category doesn't belong to user");
        }
        reqDTO.setUserId(Long.parseLong(request.getParameter("jwt_user_id")));

        CashFlowEntry entry = cashFlowEntryRepository.save(modelMapper.map(reqDTO, CashFlowEntry.class));
        return modelMapper.map(entry, CashFlowEntryRespDTO.class);
    }
}
