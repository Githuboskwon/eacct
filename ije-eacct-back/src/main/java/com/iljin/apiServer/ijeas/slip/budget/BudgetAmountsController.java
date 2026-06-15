package com.iljin.apiServer.ijeas.slip.budget;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetAmountsController {
    private final BudgetService budgetService;

    @PostMapping("/amount/list/{periodName}/{budgetDeptCode}")
    public ResponseEntity<List<BudgetAmountsDto>> getBudgetAmountList(@PathVariable String periodName,
                                                                      @PathVariable String budgetDeptCode){
        List<BudgetAmountsDto> list = budgetService.getBudgetAmountList(periodName,budgetDeptCode);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
