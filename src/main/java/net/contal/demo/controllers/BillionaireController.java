package net.contal.demo.controllers;

import net.contal.demo.modal.Billioner;
import net.contal.demo.services.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * @author Bo Li
 */
@RestController
@RequestMapping("/billionaire")
public class BillionaireController {

    final Logger logger = LoggerFactory.getLogger(BillionaireController.class);
    final DemoService dataService;

    public BillionaireController(DemoService dataService) {
        this.dataService = dataService;
    }


    @GetMapping("/all")
    public List<Billioner> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return  dataService.test();
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public  void createBillionaire(@RequestBody Billioner billioner){
        logger.info("{}" , billioner);
    }

}
