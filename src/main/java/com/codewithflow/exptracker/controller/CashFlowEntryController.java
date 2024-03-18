package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.newEntryReqDTO;
import com.codewithflow.exptracker.dto.updateEntryReqDTO;
import com.codewithflow.exptracker.dto.CashFlowEntryRespDTO;
import com.codewithflow.exptracker.entity.CashFlowEntry;
import com.codewithflow.exptracker.repository.CashFlowEntryRepository;
import com.codewithflow.exptracker.repository.entryWithUserIdSpec;
import com.codewithflow.exptracker.service.CashFlowEntryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final CashFlowEntryRepository cashFlowEntryRepository;

    public CashFlowEntryController(
            HttpServletRequest request,
            HttpServletResponse response,
            CashFlowEntryService cashFlowEntryService,
            ModelMapper modelMapper,
            CashFlowEntryRepository cashFlowEntryRepository
    ) {
        this.request = request;
        this.response = response;
        this.cashFlowEntryService = cashFlowEntryService;
        this.modelMapper = modelMapper;
        this.cashFlowEntryRepository = cashFlowEntryRepository;
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
    public CashFlowEntryRespDTO updateCashFlowEntry(
            @PathVariable Long entryId,
            @Valid @RequestBody updateEntryReqDTO updatedEntry,
            BindingResult bindingResult
    ) throws MethodArgumentNotValidException {
        return cashFlowEntryService.updateEntry(updatedEntry, entryId, Long.parseLong(request.getParameter("jwt_user_id")), bindingResult);
    }

    @GetMapping("/entry/{entryId}")
    public CashFlowEntryRespDTO getCashFlowEntry(@PathVariable Long entryId) {
        return cashFlowEntryService.getEntry(entryId, Long.parseLong(request.getParameter("jwt_user_id")));
    }

    @GetMapping("/entries")
    public Page<CashFlowEntryRespDTO> getCashFlowEntries(
            @Join(path = "subCategory", alias = "sc")
            @Join(path = "sc.mainCategory", alias = "mc")
            @And({
                    @Spec(path = "type", params = "type", spec = Equal.class),
                    @Spec(path = "bookDate", params = "start", spec = GreaterThanOrEqual.class, config = "yyyy-MM-dd"),
                    @Spec(path = "bookDate", params = "end", spec = LessThanOrEqual.class, config = "yyyy-MM-dd"),
                    @Spec(path = "mc.id", params = "mainCategoryId", spec = Equal.class),
                    @Spec(path = "sc.id", params = "subCategoryId", spec = Equal.class)
            }) Specification<CashFlowEntry> entrySpec,
            Pageable pageable
    ) {
        Specification<CashFlowEntry> modifiedSpec = entrySpec.and(new entryWithUserIdSpec(request.getParameter("jwt_user_id")));
        Page<CashFlowEntry> pageEntries = cashFlowEntryRepository.findAll(modifiedSpec, pageable);
        return pageEntries.map(entry -> modelMapper.map(entry, CashFlowEntryRespDTO.class));
    }

    @DeleteMapping("/entry/{entryId}")
    public void deleteCashFlowEntry(@PathVariable Long entryId) {
        cashFlowEntryService.deleteEntry(entryId, Long.parseLong(request.getParameter("jwt_user_id")));
    }
}