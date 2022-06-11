package net.contal.demo.services;

import net.contal.demo.DbUtils;
import net.contal.demo.modal.Billioner;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Bo Li
 */
@Service
@Transactional
public class DemoService {


    private DbUtils dbUtils;
    @Autowired
    public DemoService(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public   List<Billioner> test(){
        Session session = this.dbUtils.openASession();
       return session.createQuery("select bs from Billioner bs ", Billioner.class)
                .getResultList();
    }

    public void saveBillioner(Billioner billioner){
        this.dbUtils.openASession().save(billioner);

    }


}
