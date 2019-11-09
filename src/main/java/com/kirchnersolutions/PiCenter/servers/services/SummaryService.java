package com.kirchnersolutions.PiCenter.servers.services;

import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    @Autowired
    private ReadingRepository readingRepository;



}
