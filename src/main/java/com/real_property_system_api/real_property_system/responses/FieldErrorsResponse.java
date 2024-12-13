package com.real_property_system_api.real_property_system.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.real_property_system_api.real_property_system.bodies.PrettyError;

public class FieldErrorsResponse 
{
    @JsonProperty("meta")
    private GenericMessage metadata;
    
    @JsonProperty("errors")
    private List<PrettyError> errors;


    public FieldErrorsResponse()
    {
        metadata = new GenericMessage();
        errors = null;
    }


    public GenericMessage getMetadata() {
        return metadata;
    }


    public void setMetadata(GenericMessage metadata) {
        this.metadata = metadata;
    }


    public List<PrettyError> getErrors() {
        return errors;
    }


    public void setErrors(List<PrettyError> errors) {
        this.errors = errors;
    }

    

}
