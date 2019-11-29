package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.AppUserRepository;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.entites.UserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CSVService {

    @Autowired
    ReadingRepository readingRepository;
    @Autowired
    UserLogRepository userLogRepository;
    @Autowired
    AppUserRepository appUserRepository;

    

}
