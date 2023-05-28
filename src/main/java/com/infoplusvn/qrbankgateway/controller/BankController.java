package com.infoplusvn.qrbankgateway.controller;

import com.infoplusvn.qrbankgateway.entity.BankEntity;
import com.infoplusvn.qrbankgateway.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/infogw/qr/v1")
public class BankController {

    @Autowired
    BankService bankService;

    @GetMapping(value = "/banks")
    public List<BankEntity> getAll(){
        return bankService.getAllbank();
    }


    @GetMapping(value = "/getBank/{bin}")
    public BankEntity getBankNameByBin(@PathVariable("bin") String bin){
        return bankService.getBankNameByBin(bin);
    }

    @GetMapping(value = "/getBin/{shortName}")
    public BankEntity getBinByShortName(@PathVariable("shortName") String shortName){
        return bankService.getBinByShortName(shortName);
    }
}
