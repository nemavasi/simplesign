package ru.hlynov.oit.simplesign.customfields;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.view.CustomFieldParams;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.sal.api.user.UserManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.issue.customfields.impl.TextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.customfields.impl.AbstractCustomFieldType;
import com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;

import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


// смотреть тут
// https://stackoverflow.com/questions/17925377/cant-understand-what-does-method-getsingularobjectfromstring-do
// https://bitbucket.org/mdoar/practical-jira-plugins/src/e0a24da546cb9eae3d5d803747ded171e65208b3/multiplevalues/src/main/java/com/mycompany/jira/plugins/multiple/MultipleValuesCFType.java?at=default&fileviewer=file-view-default


//public class SimpleSignature extends TextCFType {
public class SimpleSignature extends AbstractCustomFieldType<Carrier, String>  {
//public class SimpleSignature extends AbstractSingleFieldType<String> {
    private static final Logger log = LoggerFactory.getLogger(SimpleSignature.class);

    private JiraAuthenticationContext jiraAuthenticationContext;
    private CustomFieldManager customFieldManager;
    private IssueManager issueManager;
    private UserManager userManager;
    private CustomFieldValuePersister customFieldValuePersister;

    // The type of data in the database, one entry per value in this field
    private static final PersistenceFieldType DB_TYPE = PersistenceFieldType.TYPE_UNLIMITED_TEXT;

    /**
     * Used in the database representation of a singular value.
     * Treated as a regex when checking text input.
     */
    public static final String DB_SEP = "###";


    public SimpleSignature(@JiraImport JiraAuthenticationContext jiraAuthenticationContext,
                           @JiraImport CustomFieldManager customFieldManager,
                           @JiraImport IssueManager issueManager,
                           @JiraImport UserManager userManager,
                           @JiraImport CustomFieldValuePersister customFieldValuePersister
    ) {

        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.customFieldManager = customFieldManager;
        this.issueManager = issueManager;
        this.userManager = userManager;
        this.customFieldValuePersister = customFieldValuePersister;

    }

//    /* перевод из транспортного объекта в строку*/
//    @Override
//    public String getStringFromSingularObject(Carrier carrier) {
//        return carrier.toString();
//    }
//
//    /**
//     * Convert a database representation of a Carrier object into
//     * a Carrier object. This method is also used for bulk moves and imports.
//     */
//    /* перевод из строки в транспортный объект (при передаче данных от пользователя)*/
//    @Override
//    public Carrier getSingularObjectFromString(String s) throws FieldValidationException {
//        // s = dbvalue
//        if (StringUtils.isEmpty(s)) {
//            return null;
//        }
//        String[] parts = s.split(DB_SEP);
//        if (parts.length == 0 || parts.length > 2) {
//            log.warn("Invalid database value for MultipleValuesCFType ignored: " + s);
//            // If this should not be allowed, then throw a
//            // FieldValidationException instead
//            return null;
//        }
//        String username = "";
//        String hashcalc = "";
//        if (parts.length == 2) {
//            username = parts[0];
//            hashcalc = parts[1];
//        }
//        return new Carrier(username, hashcalc);
//    }


    @Override
    public String getStringFromSingularObject(String s) {
        return null;
    }

    @Override
    public String getSingularObjectFromString(String s) throws FieldValidationException {
        return null;
    }

    @Override
    public Set<Long> remove(CustomField customField) {
        return null;
    }

    @Override
    public void validateFromParams(CustomFieldParams customFieldParams, ErrorCollection errorCollection, FieldConfig fieldConfig) {

    }

    @Override
    public void createValue(CustomField customField, Issue issue, @Nonnull Carrier carrier) {

    }

    @Override
    public void updateValue(CustomField customField, Issue issue, Carrier carrier) {

    }

    @Override
    public Carrier getValueFromCustomFieldParams(CustomFieldParams customFieldParams) throws FieldValidationException {
        return null;
    }

    @Override
    public Object getStringValueFromCustomFieldParams(CustomFieldParams customFieldParams) {
        return null;
    }

    @Nullable
    @Override
    public Carrier getValueFromIssue(CustomField customField, Issue issue) {
        return null;
    }

    @Override
    public Carrier getDefaultValue(FieldConfig fieldConfig) {
        return null;
    }

    @Override
    public void setDefaultValue(FieldConfig fieldConfig, Carrier carrier) {

    }

    @Nullable
    @Override
    public String getChangelogValue(CustomField customField, Carrier carrier) {
        return null;
    }

    @Override
    public Map<String, Object> getVelocityParameters(final Issue issue,
                                                     final CustomField field,
                                                     final FieldLayoutItem fieldLayoutItem) {
        final Map<String, Object> map = super.getVelocityParameters(issue, field, fieldLayoutItem);

        // This method is also called to get the default value, in
        // which case issue is null so we can't use it to add currencyLocale
        if (issue == null) {
            return map;
        }

         FieldConfig fieldConfig = field.getRelevantConfig(issue);
         //add what you need to the map here

        return map;
    }

 }