package com.example.AgendaFinalBot.bl;

import com.example.AgendaFinalBot.dao.NumeroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NumeroBl {

    NumeroRepository numeroRepository;
}
