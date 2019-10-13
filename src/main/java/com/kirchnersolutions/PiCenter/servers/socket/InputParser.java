package com.kirchnersolutions.PiCenter.servers.socket;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.objects.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Component
public class InputParser {

    @Autowired
    private DebuggingService debuggingService;
    @Autowired
    private TransactionService transactionService;


}
