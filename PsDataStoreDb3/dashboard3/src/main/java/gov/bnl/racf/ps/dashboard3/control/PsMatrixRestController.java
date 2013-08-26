/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsMatrixJson;
import gov.bnl.racf.ps.dashboard3.operators.PsMatrixOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing REST interface to matrices
 * @author tomw
 */
@Controller
@RequestMapping(value = "/matrices")
public class PsMatrixRestController {
    
    // === dependency injection part === //
    
    @Autowired
    private PsMatrixOperator psMatrixOperator;

    public void setPsMatrixOperator(PsMatrixOperator psMatrixOperator) {
        this.psMatrixOperator = psMatrixOperator;
    }
    
    
    
    
    // === main part ===//
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String matrixGet(@RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        
        List<PsMatrix> listOfMatrices = this.psMatrixOperator.getAll();
        
        if(detailLevel==null){
            detailLevel = PsParameters.DETAIL_LEVEL_LOW;
        }
        JSONArray listOfMatricesJson = this.psMatrixOperator.toJson(listOfMatrices,detailLevel);
        
        return listOfMatricesJson.toString();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String matrixGetById(
            @PathVariable int id,
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        try {
            PsMatrix matrix = this.psMatrixOperator.getById(id);
            if (detailLevel == null) {
                detailLevel = PsParameters.DETAIL_LEVEL_LOW;
            }
            return this.psMatrixOperator.toJson(matrix, detailLevel).toString();

        } catch (PsMatrixNotFoundException ex) {
            Logger.getLogger(PsHostsRestController.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(PsHostsRestController.class.getName()).log(Level.SEVERE, null, "host with id=" + id + " not found");
            throw new RuntimeException("matrix with id=" + id + " not found");
        }
    }
    
    

    
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String matrixPost(@RequestBody String requestBody) {
       String message;

        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert site object based on the request
            PsMatrix newMatrix = this.psMatrixOperator.insertNewMatrixFromJson(jsonInput);

            JSONObject jsonOutput = this.psMatrixOperator.toJson(newMatrix);

            // finally return the newly created host
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(PsHostsRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsHostsRestController.class.getName()).log(Level.SEVERE, null, ex);
            return message;
        }

    }
    

    
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String matrixPut(@PathVariable int id, @RequestBody String requestBody) {
        String message = "we are in matrixPut, id = " + id + " requestBody=" + requestBody;
        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert site object based on the request
            PsMatrix matrix = this.psMatrixOperator.updateMarixFromJson(id, jsonInput);

            JSONObject jsonOutput = this.psMatrixOperator.toJson(matrix);
            // finally return the newly created site
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(PsMatrixRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsMatrixRestController.class.getName()).log(Level.SEVERE, null, ex);
            return message;
        } catch (PsMatrixNotFoundException ex) {
            message = "Unknown matrix with id=" + id;
            Logger.getLogger(PsMatrixRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsMatrixRestController.class.getName()).log(Level.SEVERE, null, ex);
            return message;
        }
    }
    
    @RequestMapping(value = "/{id}/{command}", method = RequestMethod.PUT)
    @ResponseBody
    public String sitePutCommand(@PathVariable int id, @PathVariable String command, @RequestBody String requestBody) {
        String message = "we are in sitePut, id = " + id
                + " command=" + command + " requestBody=" + requestBody;
        try {

            PsMatrix updatedMatrix = this.psMatrixOperator.executeCommand(id, command, requestBody);

            JSONObject siteAsJson =
                    this.psMatrixOperator.toJson(updatedMatrix, PsParameters.DETAIL_LEVEL_HIGH);

            return siteAsJson.toString();


        } catch (PsMatrixNotFoundException ex) {
            message = message + " matrix id="+id+" not found";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return message;
        } catch (PsHostNotFoundException ex) {
            message = message + " host not found";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return message;
        } catch (PsUnknownCommandException ex) {
            message = message + " unknown command command="+command;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return message;
        } catch (ParseException ex) {
            message = message + " error parsing request body: "+requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return message;
        }
    }
    
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String matrixDeleteById(@PathVariable int id) {
        String message = "we are in matrixDeleteById, delete matrix id = " + id;
        this.psMatrixOperator.delete(id);
        message = message + " Ordered deletion of matrix id=" + id;
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String matrixDelete() {
        String message = "we are matrixDelete()";
        //TODO finish the method
        return message;
    }
}
