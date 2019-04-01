package ru.hlynov.oit.simplesign.customfields;

import com.atlassian.jira.component.ComponentAccessor;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.view.CustomFieldParams;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.sal.api.user.UserManager;
import com.opensymphony.util.TextUtils;
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
import com.atlassian.sal.api.user.UserManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;


// смотреть тут
// https://stackoverflow.com/questions/17925377/cant-understand-what-does-method-getsingularobjectfromstring-do
// https://bitbucket.org/mdoar/practical-jira-plugins/src/e0a24da546cb9eae3d5d803747ded171e65208b3/multiplevalues/src/main/java/com/mycompany/jira/plugins/multiple/MultipleValuesCFType.java?at=default&fileviewer=file-view-default


//public class SimpleSignature extends TextCFType {
//public class SimpleSignature extends AbstractCustomFieldType<Carrier, String>  {
public class SimpleSignature extends AbstractSingleFieldType<Carrier> {
    private static final Logger log = LoggerFactory.getLogger(SimpleSignature.class);

    private JiraAuthenticationContext jiraAuthenticationContext;
//    private CustomFieldManager customFieldManager;
//    private IssueManager issueManager;
    private UserManager userManager;
    private CustomFieldValuePersister customFieldValuePersister;
    private GenericConfigManager genericConfigManager;


    // The type of data in the database, one entry per value in this field
    private static final PersistenceFieldType DB_TYPE = PersistenceFieldType.TYPE_UNLIMITED_TEXT;

    /**
     * Used in the database representation of a singular value.
     * Treated as a regex when checking text input.
     */
    public static final String DB_SEP = "###";

    protected SimpleSignature(@JiraImport CustomFieldValuePersister customFieldValuePersister,
                              @JiraImport GenericConfigManager genericConfigManager,
                              @JiraImport JiraAuthenticationContext jiraAuthenticationContext,
                              @JiraImport UserManager userManager
    ) {
        super(customFieldValuePersister, genericConfigManager);
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.userManager = userManager;
    }

    /* перевод из транспортного объекта в строку - в вид для сохранения в бд*/
    @Override
    public String getStringFromSingularObject(Carrier carrier) {
        return carrier.toString();
    }

    /**
     * Convert a database representation of a Carrier object into
     * a Carrier object. This method is also used for bulk moves and imports.
     */
    /* перевод из строки в транспортный объект (из представления бд в объектный вид для работы)*/
    @Override
    public Carrier getSingularObjectFromString(String s) throws FieldValidationException {
        // s = dbvalue
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        String[] parts = s.split(DB_SEP);
        if (parts.length == 0 || parts.length > 2) {
            log.warn("Invalid database value is ignored: " + s);
            // If this should not be allowed, then throw a
            // FieldValidationException instead
            return null;
        }
        String username = "++";
        String hashcalc = "--";
        if (parts.length == 2) {
            username = parts[0];
            hashcalc = parts[1];
        }
        return new Carrier(username, hashcalc);
    }


    @Nonnull
    @Override
    protected PersistenceFieldType getDatabaseType() {
        return DB_TYPE;
    }

    @Nullable
    @Override
    protected Object getDbValueFromObject(Carrier carrier) {

        if (carrier == null) {
            log.warn("carrier: null");
            return null;
        }

        return carrier.toString();
    }

    @Nullable
    @Override
    protected Carrier getObjectFromDbValue(@Nonnull Object o) throws FieldValidationException {
        return getSingularObjectFromString((String) o);
    }


    @Override
    public Carrier getValueFromCustomFieldParams(CustomFieldParams relevantParams) throws FieldValidationException {

        if (relevantParams == null) {
            return null;
        }

//        Collection values = relevantParams.getAllValues();
//        if ((values != null) && !values.isEmpty()) {
//            Collection<Carrier> value = new ArrayList<Carrier>();
//        }


//        if (relevantParams == null) {
//            return null;
//        } else {
//            Collection normalParams = relevantParams.getValuesForKey((String)null);
//            if (normalParams != null && !normalParams.isEmpty()) {
//                String singleParam = (String)normalParams.iterator().next();
//                return TextUtils.stringSet(singleParam) ? this.getSingularObjectFromString(singleParam) : null;
//            } else {
//                return null;
//            }
//        }


        if ((relevantParams != null) && (!relevantParams.isEmpty())) {

            log.warn("=================== getValueFromCustomFieldParams");


            Collection<String> usernameCl = relevantParams.getValuesForNullKey();
            Collection<String> passwordCl = relevantParams.getValuesForKey("1");

            if ((usernameCl == null) || (passwordCl == null)) {
                return null;
            }

            String username = null;
            String password = null;

            Iterator<String> iter = usernameCl.iterator();

            if (iter.hasNext()) {
                username = iter.next();
            }

            iter = passwordCl.iterator();

            if (iter.hasNext()) {
                password = iter.next();
            }

            if ((username == null) || (password == null)) {
                return null;
            }

            log.warn("getValuesForNullKey: " + relevantParams.getValuesForNullKey().toString());
            log.warn("getValuesForKey_1: " + relevantParams.getValuesForKey("1").toString());



            Carrier carrier = new Carrier(username, "hash_string");

            return carrier;
        }
        return null;

    }


    @Override
    public void validateFromParams(CustomFieldParams relevantParams, ErrorCollection errorCollectionToAddTo, FieldConfig config) {
//        super.validateFromParams(relevantParams, errorCollectionToAddTo, config);


        Collection<String> usernameCl = relevantParams.getValuesForNullKey();
        Collection<String> passwordCl = relevantParams.getValuesForKey("1");

        if ((usernameCl == null) || (passwordCl == null)) {
            errorCollectionToAddTo.addError(config.getFieldId(), "не введены учетные данные");
            return;
        }

        String username = null;
        String password = null;


        Iterator<String> iter = usernameCl.iterator();

        if (iter.hasNext()) {
            username = iter.next();
        }

        if (username == null) {
            errorCollectionToAddTo.addError(config.getFieldId(), "не введено имя пользователя");
            return;
        }


        iter = passwordCl.iterator();

        if (iter.hasNext()) {
            password = iter.next();
        }

        if (password == null) {
            errorCollectionToAddTo.addError(config.getFieldId(), "не введен пароль");
            return;
        }

        ApplicationUser curUser = jiraAuthenticationContext.getLoggedInUser();

        if (!username.equals(curUser.getName())) {
            errorCollectionToAddTo.addError(config.getFieldId(),  username +" не является текущим пользователем");
            return;
        }

        boolean isAuthenticated = this.userManager.authenticate(username, password);
        if (!isAuthenticated) {
            errorCollectionToAddTo.addError(config.getFieldId(),  username +" аутентификация не пройдена. Неверное имя пользователя или пароль");
            return;
        }

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


        log.warn("============= getVelocityParameters =============");
        log.warn("issue: " + issue.toString());
        log.warn("field: " + field.toString());
        log.warn("fieldLayoutItem: " + fieldLayoutItem.toString());

        FieldConfig fieldConfig = field.getRelevantConfig(issue);
        //add what you need to the map here

        map.put("username", jiraAuthenticationContext.getLoggedInUser().getName());
        map.put("baseurl",ComponentAccessor.getApplicationProperties().getString("jira.baseurl"));
        map.put("sometext", "cool");

        return map;
    }
}