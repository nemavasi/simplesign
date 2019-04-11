package ru.hlynov.oit.simplesign.rest;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import ru.hlynov.oit.simplesign.customfields.Carrier;
import ru.hlynov.oit.simplesign.tools.AttacmentsTools;
import ru.hlynov.oit.simplesign.tools.JsonConvertor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hlynov.oit.simplesign.tools.SignCalculator;

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

        Carrier carrier = (Carrier) issue.getCustomFieldValue(customField);

        retVal = JsonConvertor.getAttachmentNames(carrier.getHashcalc());

//        return Response.ok(new SignRestModel("Hello World")).build();
        return Response.ok(retVal).build();
    }

    // 2 - возврат цифровой подписи конкретной компоненты поля
    // на входе нужен идентификатор задачи
    // тип объекта (заголовок, описание, вложение)
    // идентификатор вложения, если вложение

    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getsignature/{issueid}/{fieldid}/{objName}/{attachid}")
    public Response getAttachNames(@PathParam("issueid") String issueId,
                                   @PathParam("fieldid") String fieldId,
                                   @PathParam("objName") String objName,
                                   @PathParam("attachid") String attachId
    )
    {
        String retVal = "{}"; // return value

//        log.warn("issueId: " + issueId);
//        log.warn("fieldid: " + fieldId);
//        log.warn("objid: " + objId);
//        log.warn("attachId: " + attachId);

        MutableIssue issue = ComponentAccessor.getIssueManager().getIssueObject(Long.valueOf(issueId));
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(fieldId);

        Carrier carrier = (Carrier) issue.getCustomFieldValue(customField);

        String signhash = "";
        String realhash = "";



        if (objName.equals("summary")) {
            signhash = JsonConvertor.getStoredSignatureByName("summary", null, carrier.getHashcalc());
            realhash = SignCalculator.getCalculatedSignatureByName("summary", issue.getSummary());

//            log.warn(signhash);
//            log.warn(realhash);

            retVal = "{\"objname\" : \"summary\", \"signhash\":\"" + signhash + "\", \"realhash\":\"" + realhash + "\"}";
        }

        if (objName.equals("description")) {
            signhash = JsonConvertor.getStoredSignatureByName("description", null, carrier.getHashcalc());
            realhash = SignCalculator.getCalculatedSignatureByName("description", issue.getDescription());

            retVal = "{\"objname\" : \"description\", \"signhash\":\"" + signhash + "\", \"realhash\":\"" + realhash + "\"}";
        }

        if (objName.equals("attachment")) {

            // тут надо получить имя файла где сохранено вложение
            String pathToAttachFile = AttacmentsTools.getPathToAttachment(issue, attachId);

            signhash = JsonConvertor.getStoredSignatureByName("attachment", attachId, carrier.getHashcalc());
            realhash = SignCalculator.getCalculatedSignatureByName("attachment", pathToAttachFile);

            retVal = "{\"objname\" : \"attachment\", \"signhash\":\"" + signhash + "\", \"realhash\":\"" + realhash + "\"}";

        }

        return Response.ok(retVal).build();


    }

}