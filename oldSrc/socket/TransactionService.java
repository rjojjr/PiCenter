package com.kirchnersolutions.PiCenter.servers.socket;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.ReadRepository;
import com.kirchnersolutions.PiCenter.servers.objects.Results;
import com.kirchnersolutions.PiCenter.servers.objects.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    DebuggingService debuggingService;
    @Autowired
    private ReadRepository readRepository;

    Results submitTransaction(Transaction transaction){
        String room = transaction.getOperation();
        return null;
    }

}
