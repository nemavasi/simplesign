package ru.hlynov.oit.simplesign.tools;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.config.util.AttachmentPathManager;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.issue.attachment.FileAttachments;
import com.atlassian.jira.issue.AttachmentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttacmentsTools {

    private static final Logger log = LoggerFactory.getLogger(AttacmentsTools.class);

    // возвращает значения <ид вложения, путь к файлу>
    public static Map<String, Map<String, String>> getAttachmentsInfo(Issue issue) {

        Map<String, Map<String, String>> attachs = new HashMap<String, Map<String, String>>();


        AttachmentPathManager attachmentPathManager = ComponentAccessor.getAttachmentPathManager();
        File rootDir = new File(attachmentPathManager.getAttachmentPath());

        // каталог где лежат вложения
        File attachDir = FileAttachments.getAttachmentDirectoryForIssue(rootDir,  issue.getProjectObject().getKey(), issue.getKey());
        String attachPathStr = attachDir.getPath();
//        filePathStr = filePathStr + "/" + attachmentId;

        String hash = null;

        // получим вложения для задачи
        List<Attachment> attachmentList = ComponentAccessor.getAttachmentManager().getAttachments(issue);
        for (Attachment oneAttach : attachmentList) {

            Map<String, String> attachInfo = new HashMap<String, String>();
            attachInfo.put("name", oneAttach.getFilename());

            hash = null;

            try {
                hash = SignCalculator.getFileCheckusm(attachPathStr + "/" + String.valueOf(oneAttach.getId()));
            } catch (IOException e) {
//                e.printStackTrace();
                log.error("io exception ", e);
            }
            if (hash != null) {
                attachInfo.put("hash", hash);
            } else {
                attachInfo.put("hash", "");
            }

            attachs.put(String.valueOf(oneAttach.getId()), attachInfo);
        }


        return attachs;

    }
}
