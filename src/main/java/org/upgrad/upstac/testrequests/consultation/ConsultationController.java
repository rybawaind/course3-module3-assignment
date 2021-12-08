package org.upgrad.upstac.testrequests.consultation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);
    @Autowired
    private TestRequestUpdateService testRequestUpdateService;
    @Autowired
    private TestRequestQueryService testRequestQueryService;
    @Autowired
    TestRequestFlowService  testRequestFlowService;
    @Autowired
    private UserLoggedInService userLoggedInService;


    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {

        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED);  // Implement this method
        //Implement this method to get the list of test requests having status as 'LAB_TEST_COMPLETED'
        // make use of the findBy() method from testRequestQueryService class
        //return the result
        // For reference check the method getForTests() method from LabRequestController class
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        User doctor = userLoggedInService.getLoggedInUser();  //Implement this method

        // Create an object of User class and store the current logged in user first
        //Implement this method to return the list of test requests assigned to current doctor(make use of the above created User object)
        //Make use of the findByDoctor() method from testRequestQueryService class to get the list
        // For reference check the method getForTests() method from LabRequestController class

        return testRequestQueryService.findByDoctor(doctor); // replace this line of code with your implementation
    }


    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

        try {
            User doctor = userLoggedInService.getLoggedInUser(); // replace this line of code with your implementation
            TestRequest assignedTestReq = testRequestUpdateService.assignForConsultation(id, doctor);
            return assignedTestReq;

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }


    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        try {
            // replace this line of code with your implementation
            User doctor = userLoggedInService.getLoggedInUser();

            //Updates consultation with request body (CreateConsultationRequest object)
            TestRequest updatedTestReq = testRequestUpdateService.updateConsultation(id, testResult, doctor);

            //returns the updated TestRequest
            return updatedTestReq;
        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }

}
