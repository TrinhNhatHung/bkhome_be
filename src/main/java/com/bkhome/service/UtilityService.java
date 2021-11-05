package com.bkhome.service;

import com.bkhome.dao.UtilityDao;
import com.bkhome.persistence.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    @Autowired
    private UtilityDao utilityDao;

    public Utility getById (Integer utilityId){
        return  utilityDao.getById(Utility.class, utilityId);
    }
}
