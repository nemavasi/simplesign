package ru.hlynov.oit.simplesign.rest;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import ru.hlynov.oit.simplesign.customfields.Carrier;
import ru.hlynov.oit.simplesign.tools.JsonConvertor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * A resource of message.
 */
@Path("/message")
public class SignRest {

    private static final Logger log = LoggerFactory.getLogger(SignRest.class);

    // 1 - возврат имен вложений перечисленных в подписи
    // на входе нужен будет идентификатор задачи

    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getattachnames/{issueid}/{fieldid}")
    public Response getAttachNames(@PathParam("issueid") String issueId,
                                   @PathParam("fieldid") String fieldId
                                   )
    {
        String retVal = "[]"; // return value

        MutableIssue issue = ComponentAccessor.getIssueManager().getIssueObject(Long.valueOf(issueId));
//        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(Long.valueOf(fieldId));
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(fieldId);

        log.warn("issueId: " + issueId);
        log.warn("fieldId: " + fieldId);

        log.warn("issue: " + issue);
        log.warn("customField: " + customField);

        Carrier carrier = (Carrier) issue.getCustomFieldValue(customField);
        log.warn("carrier: " + carrier.toString());

        retVal = JsonConvertor.getAttachmentNames(carrier.getHashcalc());

//        return Response.ok(new SignRestModel("Hello World")).build();
        return Response.ok(retVal).build();
    }
}