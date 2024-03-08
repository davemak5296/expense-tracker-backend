package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.CashFlowEntryPostReqDTO;
import com.codewithflow.exptracker.dto.updateEntryReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import com.codewithflow.exptracker.entity.CashFlowEntry;
import com.codewithflow.exptracker.entity.SubCategory;
import com.codewithflow.exptracker.repository.CashFlowEntryRepository;
import com.codewithflow.exptracker.repository.SubCategoryRepository;
import com.codewithflow.exptracker.util.exception.ResourceNotFoundException;
import com.codewithflow.exptracker.util.exception.SubCategoryIdNotMatchException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("cashFlowEntryService")
@Transactional
public class CashFlowEntryServiceImpl implements CashFlowEntryService{

    private final ModelMapper modelMapper;
    private final CashFlowEntryRepository cashFlowEntryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final EntityManager entityManager;

    public CashFlowEntryServiceImpl(
            ModelMapper modelMapper,
            CashFlowEntryRepository cashFlowEntryRepository,
            SubCategoryRepository subCategoryRepository,
            EntityManager entityManager
    ) {
        this.modelMapper = modelMapper;
        this.cashFlowEntryRepository = cashFlowEntryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.entityManager = entityManager;
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

    public CashFlowEntryRespDTO updateEntry(
            updateEntryReqDTO reqDTO,
            Long entryId,
            Long jwtUserId
    ) {
        Long newSubCatId = reqDTO.getSubCategoryId();

        // check if entry exists
        Optional<CashFlowEntry> existingEntry = cashFlowEntryRepository.findByIdAndUserId(entryId, jwtUserId);
        if (existingEntry.isEmpty()) {
            throw new ResourceNotFoundException("entry not found");
        }

        // clear sub-category id in reqDTO to avoid incorrect mapping when calling BeanUtils.copyProperties
        reqDTO.setSubCategoryId(null);
        BeanUtils.copyProperties(reqDTO, existingEntry.get(), getNullPropertyNames(reqDTO));

        // if new sub-category is provided, check if it exists
        if (newSubCatId != null) {
            if (!isSubCatExist(newSubCatId, jwtUserId)) {
                System.out.println("true");
                throw new ResourceNotFoundException("sub-category not found");
            }
            SubCategory sub = new SubCategory();
            sub.setId(newSubCatId);
            existingEntry.get().setSubCategory(sub);
        }

        CashFlowEntry updatedEntry = cashFlowEntryRepository.saveAndFlush(existingEntry.get());
        entityManager.refresh(updatedEntry);

        return modelMapper.map(updatedEntry, CashFlowEntryRespDTO.class);
    }

    private boolean isSubCatExist(Long subCatId, Long userId) {
        Optional<SubCategory> subCategory = subCategoryRepository.findByIdAndUserId(subCatId, userId);
        return subCategory.isPresent();
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
